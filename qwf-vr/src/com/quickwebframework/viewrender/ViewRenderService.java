package com.quickwebframework.viewrender;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.SynchronousBundleListener;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.util.PropertiesUtils;

/**
 * 视图渲染服务
 * 
 * @author aaa
 * 
 */
public abstract class ViewRenderService {

	// 插件名称与路径分隔字符串配置键
	public final static String CONFIG_PLUGIN_NAME_AND_PATH_SPLIT_STRING = "%s.pluginNameAndPathSplitString";
	// 视图名称统一前缀配置键
	public final static String CONFIG_VIEW_NAME_PREFIX = "%s.viewNamePrefix";
	// 视图名称统一后缀配置键
	public final static String CONFIG_VIEW_NAME_SUFFIX = "%s.viewNameSuffix";
	// 配置文件键
	public final static String CONFIG_PROPERTIES = "%s.properties";

	private ServiceRegistration<?> viewRenderServiceRegistration;
	// 插件名称与路径分隔符
	private String pluginNameAndPathSplitString = ":";
	// 视图名称前缀
	private String viewNamePrefix = "";
	// 视图名称后缀
	private String viewNameSuffix = ".html";
	// 插件监听器
	private BundleListener bundleListener;

	public String getPluginNameAndPathSplitString() {
		return pluginNameAndPathSplitString;
	}

	public void setPluginNameAndPathSplitString(
			String pluginNameAndPathSplitString) {
		this.pluginNameAndPathSplitString = pluginNameAndPathSplitString;
	}

	public String getViewNamePrefix() {
		return viewNamePrefix;
	}

	public void setViewNamePrefix(String viewNamePrefix) {
		this.viewNamePrefix = viewNamePrefix;
	}

	public String getViewNameSuffix() {
		return viewNameSuffix;
	}

	public void setViewNameSuffix(String viewNameSuffix) {
		this.viewNameSuffix = viewNameSuffix;
	}

	public ViewRenderService() {
		String tmpStr = null;
		// 分隔符
		tmpStr = WebContext.getQwfConfig(String.format(
				CONFIG_PLUGIN_NAME_AND_PATH_SPLIT_STRING, getBundleName()));
		if (tmpStr != null && !tmpStr.equals("")) {
			this.setPluginNameAndPathSplitString(tmpStr);
		}
		// 前缀
		tmpStr = WebContext.getQwfConfig(String.format(CONFIG_VIEW_NAME_PREFIX,
				getBundleName()));
		if (tmpStr != null && !tmpStr.equals("")) {
			this.setViewNamePrefix(tmpStr);
		}
		// 后缀
		tmpStr = WebContext.getQwfConfig(String.format(CONFIG_VIEW_NAME_SUFFIX,
				getBundleName()));
		if (tmpStr != null && !tmpStr.equals("")) {
			this.setViewNameSuffix(tmpStr);
		}
		final ViewRenderService thisService = this;
		bundleListener = new SynchronousBundleListener() {

			public void bundleChanged(BundleEvent event) {
				thisService.bundleChanged(event);
			}
		};
	}

	/**
	 * 将ViewRender注册为服务
	 * 
	 * @param bundleContext
	 */
	public void registerService(BundleContext bundleContext) {
		Dictionary<String, String> dict = new Hashtable<String, String>();
		dict.put("bundle", this.getBundleName());
		viewRenderServiceRegistration = bundleContext.registerService(
				ViewRenderService.class.getName(), this, dict);
		bundleContext.addBundleListener(bundleListener);
	}

	/**
	 * 取消将ViewRender注册为服务
	 */
	public void unregisterService(BundleContext bundleContext) {
		bundleContext.removeBundleListener(bundleListener);
		viewRenderServiceRegistration.unregister();
	}

	// 得到配置信息
	public Properties getProperties() {
		String fileName = WebContext.getQwfConfig(String.format(
				CONFIG_PROPERTIES, getBundleName()));
		if (fileName == null || fileName.equals("")) {
			return null;
		}
		fileName = WebContext.getRealPath(fileName);
		File file = new File(fileName);
		if (!file.exists() || !file.isFile()) {
			String message = String.format("Properties file [%s] not exist!",
					fileName);
			throw new RuntimeException(message);
		}
		try {
			InputStream inputStream = new FileInputStream(fileName);
			Reader reader = new InputStreamReader(inputStream, "utf-8");
			Properties viewrenderProp = PropertiesUtils.load(reader);
			reader.close();
			inputStream.close();
			return viewrenderProp;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 得到插件插件
	 * 
	 * @return
	 */
	public abstract String getBundleName();

	/**
	 * 插件状态改变时
	 * 
	 * @param event
	 */
	public abstract void bundleChanged(BundleEvent event);

	/**
	 * 渲染视图
	 * 
	 * @param request
	 * @param response
	 * @param viewName
	 * @param model
	 */
	public abstract void renderView(HttpServletRequest request,
			HttpServletResponse response, String viewName,
			Map<String, Object> model);
}
