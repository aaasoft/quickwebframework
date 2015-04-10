package com.quickwebframework.framework.impl;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;

import com.quickwebframework.core.Activator;
import com.quickwebframework.framework.FrameworkContext;
import com.quickwebframework.framework.WebContext;
import com.quickwebframework.util.pattern.WildcardPattern;

public class ServletServletContext extends FrameworkContext {
	private static Log log = LogFactory.getLog(ServletServletContext.class);
	private static ServletServletContext instance;

	public static ServletServletContext getInstance() {
		if (instance == null)
			instance = new ServletServletContext();
		return instance;
	}

	// 路径与Servlet映射Map
	private static Map<String, Servlet> pathServletMap;
	// 路径与通配符模板对象映射Map
	private static Map<String, WildcardPattern> pathWildcardPatternMap;

	public ServletServletContext() {
		pathServletMap = new HashMap<String, Servlet>();
		pathWildcardPatternMap = new HashMap<String, WildcardPattern>();
	}

	@Override
	protected BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	protected void init(int arg) {

	}

	@Override
	protected void destory(int arg) {

	}

	@Override
	protected void bundleChanged(BundleEvent event) {

	}

	@Override
	protected void serviceChanged(ServiceEvent event) {

	}

	/**
	 * 根据别名(路径)找到Servlet
	 * 
	 * @param alias
	 * @return
	 */
	public static Servlet getServletByPath(String alias) {
		for (String path : pathServletMap.keySet()) {
			if (path.startsWith("*.") || path.endsWith("*")) {
				WildcardPattern pattern = pathWildcardPatternMap.get(path);
				if (pattern == null) {
					pattern = new WildcardPattern(path);
					pathWildcardPatternMap.put(path, pattern);
				}
				if (pattern.implies(alias)) {
					return pathServletMap.get(path);
				}
			} else {
				if (path.equals(alias)) {
					return pathServletMap.get(alias);
				}
			}
		}
		return null;
	}

	/**
	 * 获取所有Servlet所注册的路径数组
	 * 
	 * @return
	 */
	public static String[] getAllServletPaths() {
		return pathServletMap.keySet().toArray(new String[0]);
	}

	/**
	 * 注册Servlet
	 * 
	 * @param path
	 * @param servlet
	 * @param initparams
	 * @throws javax.servlet.ServletException
	 */
	public static void registerServlet(String path, Servlet servlet,
			Dictionary<String, Object> initparams) {
		if (pathServletMap.containsKey(path)) {
			throw new RuntimeException(String.format(
					"路径[%s]已经被映射到了Servlet[%s]", path, pathServletMap.get(path)));
		}
		final Servlet servletMirror = servlet;
		final Dictionary<String, Object> initparamsMirror = initparams;
		// 初始化Servlet
		try {
			servlet.init(new ServletConfig() {

				public String getInitParameter(String arg0) {
					if (initparamsMirror == null)
						return null;
					Object obj = initparamsMirror.get(arg0);
					if (obj == null)
						return null;
					return obj.toString();
				}

				public Enumeration<String> getInitParameterNames() {
					if (initparamsMirror == null)
						return null;
					return initparamsMirror.keys();
				}

				public ServletContext getServletContext() {
					return WebContext.getServletContext();
				}

				public String getServletName() {
					return servletMirror.toString();
				}
			});
		} catch (Exception ex) {
			throw new RuntimeException(String.format(
					"注册Servlet[%s]到路径[%s]时出错。", servlet, path), ex);
		}
		pathServletMap.put(path, servlet);
		log.debug(String.format("已注册路径[%s]到Servlet[%s].", path, servlet));
	}

	/**
	 * 注册资源
	 * 
	 * @param alias
	 * @param name
	 */
	public static void registerResources(String alias, String name) {
		throw new RuntimeException("此方法还未实现！");
	}

	/**
	 * 取消注册Servlet
	 * 
	 * @param path
	 */
	public static void unregisterServlet(String path) {
		if (!pathServletMap.containsKey(path))
			return;
		Servlet servlet = pathServletMap.get(path);
		pathServletMap.remove(path);
		log.debug(String.format("已取消注册路径为[%s]的Servlet[%s].", path, servlet));
	}
}
