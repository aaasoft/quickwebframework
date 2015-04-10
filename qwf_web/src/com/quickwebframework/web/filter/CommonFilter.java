package com.quickwebframework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.quickwebframework.web.listener.QuickWebFrameworkFactory;
import com.quickwebframework.web.listener.QuickWebFrameworkLoaderListener;
import com.quickwebframework.web.servlet.PluginManageServlet;
import com.quickwebframework.web.servlet.QwfServlet;

public class CommonFilter implements Filter {
	// QuickwebFramework的过滤器配置状态
	public static final String QUICKWEBFRAMEWORK_STATE_FILTERCONFIG = "com.quickwebframework.state.FILTERCONFIG";
	// 插件管理页映射的URL
	public static final String MAPPING_PROPERTY_KEY = "qwf.pluginManage.mapping";

	public void destroy() {
		Filter frameworkBridgeFilter = QuickWebFrameworkLoaderListener
				.getServletFilterBridgeObject();
		if (frameworkBridgeFilter != null)
			frameworkBridgeFilter.destroy();
	}

	public void init(FilterConfig arg0) throws ServletException {
		Filter frameworkBridgeFilter = QuickWebFrameworkLoaderListener
				.getServletFilterBridgeObject();
		if (frameworkBridgeFilter == null)
			arg0.getServletContext().setAttribute(
					QUICKWEBFRAMEWORK_STATE_FILTERCONFIG, arg0);
		else
			frameworkBridgeFilter.init(arg0);
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// 如果不是HTTP请求
		if (!"http".equals(arg0.getScheme().toLowerCase())) {
			arg2.doFilter(arg0, arg1);
			return;
		}

		HttpServletRequest request = (HttpServletRequest) arg0;
		String contextPath = request.getContextPath();
		if (!QuickWebFrameworkFactory.qwfServletList.isEmpty()) {
			String requestUriWithoutContextPath = request.getRequestURI()
					.substring(contextPath.length());
			if (StringUtils.isEmpty(requestUriWithoutContextPath)) {
				requestUriWithoutContextPath = "/"
						+ requestUriWithoutContextPath;
			}
			for (QwfServlet qwfServlet : QuickWebFrameworkFactory.qwfServletList) {
				if (qwfServlet.isUrlMatch(requestUriWithoutContextPath)) {
					qwfServlet.service(arg0, arg1);
					return;
				}
			}
		}

		Filter frameworkBridgeFilter = QuickWebFrameworkLoaderListener
				.getServletFilterBridgeObject();
		if (frameworkBridgeFilter == null) {
			HttpServletResponse response = (HttpServletResponse) arg1;
			response.setContentType("text/html;charset=utf-8");
			StringBuilder sb = new StringBuilder();
			sb.append("<html><head><title>Powered by QuickWebFramework</title></head><body>Welcome to use QuickWebFramework!");
			if (PluginManageServlet.getInstance() != null) {
				String bundleManageUrl = contextPath + "/"
						+ PluginManageServlet.getInstance().getMapping();
				while (bundleManageUrl.contains("//")) {
					bundleManageUrl = bundleManageUrl.replace("//", "/");
				}
				sb.append("You can manage bundles in the <a href=\""
						+ bundleManageUrl + "\">Bundle Manage Page</a>!");
			}
			sb.append("<p>QuickWebFrameweb's core bundle not installed or started,please install core bundle and start it first!</p>");
			sb.append("</body></html>");
			response.getWriter().write(sb.toString());
		} else {
			frameworkBridgeFilter.doFilter(arg0, arg1, arg2);
		}
	}
}
