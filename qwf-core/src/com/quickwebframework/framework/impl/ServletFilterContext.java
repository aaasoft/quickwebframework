package com.quickwebframework.framework.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceRegistration;

import com.quickwebframework.bridge.ServletFilterBridge;
import com.quickwebframework.core.Activator;
import com.quickwebframework.framework.FrameworkContext;
import com.quickwebframework.framework.WebContext;
import com.quickwebframework.stereotype.FilterSetting;

public class ServletFilterContext extends FrameworkContext {
	private static Log log = LogFactory.getLog(ServletFilterContext.class);
	private static ServletFilterContext instance;

	public static ServletFilterContext getInstance() {
		if (instance == null)
			instance = new ServletFilterContext();
		return instance;
	}

	// QuickwebFramework的过滤器配置状态
	// ===== 常量开始
	public static final String QUICKWEBFRAMEWORK_STATE_FILTERCONFIG = "com.quickwebframework.state.FILTERCONFIG";
	// ===== 常量结束

	// ===== 过滤器变量部分开始
	// 从上层传递下来的过滤器配置
	private static FilterConfig filterConfig;
	private static List<Filter> filterList;
	private static Map<Bundle, List<Filter>> bundleFilterListMap;
	// 过滤器桥接对象
	private ServiceRegistration<?> servletFilterBridgeServiceRegistration;

	/**
	 * 得到过滤器配置
	 * 
	 * @return
	 */
	public static FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public ServletFilterContext() {
		filterList = new ArrayList<Filter>();
		bundleFilterListMap = new HashMap<Bundle, List<Filter>>();

	}

	/**
	 * 设置过滤器配置
	 * 
	 * @param filterConfig
	 */
	public static void setFilterConfig(FilterConfig filterConfig) {
		ServletFilterContext.filterConfig = filterConfig;
		if (filterConfig == null)
			return;
		for (Filter filter : filterList) {
			try {
				filter.init(filterConfig);
			} catch (ServletException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 得到所有过滤器
	 * 
	 * @return
	 */
	public static Filter[] getFilters() {
		return filterList.toArray(new Filter[0]);
	}

	// ===== 过滤器变量部分结束

	@Override
	protected BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	protected void init(int arg) {
		BundleContext bundleContext = Activator.getContext();

		// 启动时，从ServletContext中读取相关运行时状态
		Object filterConfigObject = WebContext.getServletContext()
				.getAttribute(QUICKWEBFRAMEWORK_STATE_FILTERCONFIG);
		if (filterConfigObject != null)
			setFilterConfig((FilterConfig) filterConfigObject);

		// 注册过滤器桥接对象
		servletFilterBridgeServiceRegistration = bundleContext.registerService(
				ServletFilterBridge.class.getName(), new ServletFilterBridge(),
				null);
	}

	@Override
	protected void destory(int arg) {
		servletFilterBridgeServiceRegistration.unregister();
		// 停止时，保存相关运行时状态到ServletContext中。
		WebContext.getServletContext().setAttribute(
				QUICKWEBFRAMEWORK_STATE_FILTERCONFIG, getFilterConfig());
	}

	@Override
	protected void bundleChanged(BundleEvent event) {
		Bundle bundle = event.getBundle();
		int bundleEventType = event.getType();

		BundleContext bundleContext = Activator.getContext();
		if (bundleContext == null)
			return;
		Bundle coreBundle = bundleContext.getBundle();
		if (bundleEventType == BundleEvent.STOPPING) {
			// 移除插件的过滤器
			if (bundle.equals(coreBundle)) {
				ServletFilterContext.unregisterAllFilter();
			} else {
				ServletFilterContext.unregisterBundleAllFilter(bundle);
			}
		}
	}

	@Override
	protected void serviceChanged(ServiceEvent event) {

	}

	/**
	 * 注册过滤器
	 * 
	 * @param bundle
	 * @param filter
	 */
	public static void registerFilter(Bundle bundle, Filter filter) {
		if (filterConfig != null) {
			try {
				filter.init(filterConfig);
			} catch (ServletException e) {
				throw new RuntimeException(e);
			}
		}
		String filterClassName = filter.getClass().getName();
		// 是否存在同类名实例
		boolean hasSameClassNameObject = false;
		for (Filter preFilter : filterList) {
			if (preFilter.getClass().getName().equals(filterClassName)) {
				hasSameClassNameObject = true;
				break;
			}
		}
		// 如果存在同类名实例，则抛出异常
		if (hasSameClassNameObject) {

			StringBuilder sb = new StringBuilder();
			sb.append(String.format(
					"警告：将Bundle[%s]的过滤器[类名:%s]加入到FilterContext中时，发现存在多个同类名实例！",
					bundle.getSymbolicName(), filterClassName));
			sb.append("\n--同类名实例列表如下：");
			synchronized (bundleFilterListMap) {
				for (Bundle tmpBundle : bundleFilterListMap.keySet()) {
					List<Filter> tmpBundleFilterList = bundleFilterListMap
							.get(tmpBundle);
					for (Filter tmpFilter : tmpBundleFilterList) {
						if (tmpFilter.getClass().getName()
								.equals(filterClassName)) {
							sb.append(String.format(
									"\n  --Bundle[%s],过滤器[%s ,类名:%s]",
									tmpBundle.getSymbolicName(),
									tmpFilter.toString(), filterClassName));
						}
					}
				}
			}
			String errorMessage = sb.toString();
			log.warn(errorMessage);
		}

		// 加入到Bundle对应的过滤器列表中
		List<Filter> bundleFilterList = null;
		if (bundleFilterListMap.containsKey(bundle)) {
			bundleFilterList = bundleFilterListMap.get(bundle);
		} else {
			bundleFilterList = new ArrayList<Filter>();
			bundleFilterListMap.put(bundle, bundleFilterList);
		}
		bundleFilterList.add(filter);

		// 加入到全部过滤器列表中
		filterList.add(filter);

		// 过滤器的类
		Class<?> filterClass = filter.getClass();
		// 过滤器的FilterSetting实例
		FilterSetting filterSetting = filterClass
				.getAnnotation(FilterSetting.class);
		// 如果此过滤器的类上有FilterSetting注解，则全部过滤器根据FilterSetting注解的index的值进行排序
		if (filterSetting != null) {
			// 有属性的过滤器列表
			List<Filter> hasSettingFilterList = new ArrayList<Filter>();
			// 没有属性的过滤器列表
			List<Filter> noSettingFilterList = new ArrayList<Filter>();
			// 设置与过滤器的Map
			Map<FilterSetting, Filter> settingFilterMap = new HashMap<FilterSetting, Filter>();

			// 分离
			for (Filter tmpFilter : filterList) {
				Class<?> tmpFilterClass = tmpFilter.getClass();
				FilterSetting tmpFilterSetting = tmpFilterClass
						.getAnnotation(FilterSetting.class);
				// 如果没有设置
				if (tmpFilterSetting == null) {
					noSettingFilterList.add(tmpFilter);
				}// 否则有设置
				else {
					hasSettingFilterList.add(tmpFilter);
					settingFilterMap.put(tmpFilterSetting, tmpFilter);
				}
			}
			// 根据index排序
			FilterSetting[] filterSettings = settingFilterMap.keySet().toArray(
					new FilterSetting[0]);
			for (int j = 0; j < filterSettings.length; j++) {
				for (int i = 0; i < filterSettings.length; i++) {
					if (i == 0)
						continue;
					// 如果前面的index大于后面的index，则交换
					if (filterSettings[i - 1].index() > filterSettings[i]
							.index()) {
						FilterSetting tmpExchangeObject = filterSettings[i - 1];
						filterSettings[i - 1] = filterSettings[i];
						filterSettings[i] = tmpExchangeObject;
					}
				}
			}

			// 得到新的列表
			List<Filter> newFilterList = new ArrayList<Filter>();
			for (int i = 0; i < filterSettings.length; i++) {
				newFilterList.add(settingFilterMap.get(filterSettings[i]));
			}
			newFilterList.addAll(noSettingFilterList);
			filterList = newFilterList;
		}

		log.debug(String.format("已添加插件[%s]的过滤器[%s]！", bundle.getSymbolicName(),
				filter));
	}

	/**
	 * 取消注册所有的过滤器
	 */
	public static void unregisterAllFilter() {
		for (Bundle bundle : bundleFilterListMap.keySet()
				.toArray(new Bundle[0])) {
			unregisterBundleAllFilter(bundle);
		}
	}

	/**
	 * 取消注册某Bundle所有的过滤器
	 * 
	 * @param bundle
	 */
	public static void unregisterBundleAllFilter(Bundle bundle) {
		if (!bundleFilterListMap.containsKey(bundle))
			return;
		Filter[] bundleFilterArray = bundleFilterListMap.get(bundle).toArray(
				new Filter[0]);

		for (Filter filter : bundleFilterArray) {
			unregisterFilter(bundle, filter);
		}
		bundleFilterListMap.remove(bundle);
	}

	/**
	 * 取消注册过滤器
	 * 
	 * @param bundle
	 * @param filter
	 */
	public static void unregisterFilter(Bundle bundle, Filter filter) {

		// 从Bundle对应的过滤器列表中移除
		if (!bundleFilterListMap.containsKey(bundle))
			return;
		List<Filter> bundleFilterList = bundleFilterListMap.get(bundle);
		bundleFilterList.remove(filter);

		// 从所有的过滤器列表中移除
		filterList.remove(filter);
		// 销毁过滤器
		filter.destroy();
		log.debug(String.format("已成功移除插件[%s]的过滤器[%s]！",
				bundle.getSymbolicName(), filter));
	}
}
