package com.quickwebframework.view.jsp.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.view.jsp.support.Activator;
import com.quickwebframework.viewrender.ViewRenderService;
import com.quickwebframework.viewrender.servlet.VrViewTypeServlet;

public class JspViewTypeServlet extends VrViewTypeServlet {
	private static final long serialVersionUID = 3719762515648054933L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public String getBundleName() {
		return Activator.BUNDLE_NAME;
	}

	@Override
	public BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	public String[] getUrls() {
		List<String> rtnUrlList = new ArrayList<String>();
		ViewRenderService viewRenderService = this.getViewRenderService();
		for (Bundle bundle : Activator.getContext().getBundles()) {
			// OSGi框架插件不扫描
			if (bundle.getBundleId() == 0) {
				continue;
			}
			String bundleName = bundle.getSymbolicName();
			try {
				Enumeration<URL> resources = bundle.findEntries(
						viewRenderService.getViewNamePrefix(), "*"
								+ viewRenderService.getViewNameSuffix(), true);
				if (resources == null) {
					continue;
				}
				while (resources.hasMoreElements()) {
					String entryPath = resources.nextElement().getPath();
					String methodName = entryPath.substring(viewRenderService
							.getViewNamePrefix().length());
					methodName = methodName.substring(0, methodName.length()
							- viewRenderService.getViewNameSuffix().length());
					String url = "/" + bundleName + "/"
							+ this.getViewTypeName() + "/" + methodName;
					while (url.contains("//")) {
						url = url.replace("//", "/");
					}
					rtnUrlList.add(url);
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		return rtnUrlList.toArray(new String[rtnUrlList.size()]);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String pluginName = request.getAttribute(WebContext.CONST_PLUGIN_NAME)
				.toString();
		String pathName = request.getAttribute(WebContext.CONST_PATH_NAME)
				.toString();

		ViewRenderService viewRenderService = this.getViewRenderService();
		if (viewRenderService.getViewNamePrefix() != null) {
			pathName = viewRenderService.getViewNamePrefix() + pathName;
		}
		if (viewRenderService.getViewNameSuffix() != null) {
			pathName = pathName + viewRenderService.getViewNameSuffix();
		}

		// 得到视图名称：例 qwf.test.core:/jsp/test.jsp
		String viewName = pluginName
				+ viewRenderService.getPluginNameAndPathSplitString()
				+ pathName;
		viewRenderService.renderView(request, response, viewName, null);
	}
}
