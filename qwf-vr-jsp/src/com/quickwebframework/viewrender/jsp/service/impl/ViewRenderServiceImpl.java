package com.quickwebframework.viewrender.jsp.service.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;

import com.quickwebframework.framework.OsgiContext;
import com.quickwebframework.framework.WebContext;
import com.quickwebframework.viewrender.ViewRenderService;
import com.quickwebframework.viewrender.jsp.servlet.PluginJspDispatchServlet;
import com.quickwebframework.viewrender.jsp.support.Activator;

public class ViewRenderServiceImpl extends ViewRenderService {

	private Map<String, PluginJspDispatchServlet> pluginNameServletMap = new HashMap<String, PluginJspDispatchServlet>();

	public String getBundleName() {
		return Activator.BUNDLE_NAME;
	}

	public void bundleChanged(BundleEvent event) {
		if (BundleEvent.STOPPING == event.getType()
				|| BundleEvent.STOPPED == event.getType()) {
			String pluginName = event.getBundle().getSymbolicName();
			if (pluginNameServletMap.containsKey(pluginName)) {
				pluginNameServletMap.remove(pluginName);
			}
		}
	}

	public void renderView(HttpServletRequest request,
			HttpServletResponse response, String viewName,
			Map<String, Object> model) {

		String[] tmpArray = viewName.split(this
				.getPluginNameAndPathSplitString());
		if (tmpArray.length < 2) {
			throw new RuntimeException("视图名称[" + viewName + "]不符合规则：“[插件名]"
					+ this.getPluginNameAndPathSplitString() + "[路径]”");
		}
		String pluginName = tmpArray[0];
		String path = tmpArray[1];
		// 对视图名称进行处理(添加前后缀)
		// path = this.getViewNamePrefix() + path + this.getViewNameSuffix();
		request.setAttribute(WebContext.CONST_PATH_NAME, path);

		PluginJspDispatchServlet pluginJspDispatchServlet = pluginNameServletMap
				.get(pluginName);
		if (pluginJspDispatchServlet == null) {
			Bundle bundle = OsgiContext.getBundleByName(pluginName);
			pluginJspDispatchServlet = createNewPluginJspDispatchServlet(bundle);
			pluginNameServletMap.put(pluginName, pluginJspDispatchServlet);
		}
		try {
			pluginJspDispatchServlet.service(request, response);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private PluginJspDispatchServlet createNewPluginJspDispatchServlet(
			Bundle bundle) {
		PluginJspDispatchServlet servlet = new PluginJspDispatchServlet(bundle);
		final String bundleName = bundle.getSymbolicName();
		try {
			servlet.init(new ServletConfig() {

				public String getServletName() {
					return bundleName;
				}

				public ServletContext getServletContext() {
					return WebContext.getServletContext();
				}

				public Enumeration<String> getInitParameterNames() {
					return getServletContext().getInitParameterNames();
				}

				public String getInitParameter(String arg0) {
					return getServletContext().getInitParameter(arg0);
				}
			});
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
		return servlet;
	}
}
