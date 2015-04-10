package com.quickwebframework.servlet.support;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.servlet.ViewTypeServlet;

/**
 * 默认处理根路径的Servlet
 * 
 * @author AAA
 * 
 */
public class DefaultRootServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8318707530041789685L;
	private static HttpServlet instance;

	/**
	 * 得到实例
	 * 
	 * @return
	 */
	public static HttpServlet getInstance() {
		if (instance == null) {
			instance = new DefaultRootServlet();
		}
		return instance;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String contextPath = request.getContextPath();
		if (contextPath == "/") {
			contextPath = "";
		}

		response.setContentType("text/html;charset=utf-8");
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head><title>Powered by QuickWebFramework</title></head><body>Welcome to use QuickWebFramework!You can manage bundles in the <a href=\"qwf/index\">Bundle Manage Page</a>!");
		String[] allServletPaths = WebContext.getAllServletPaths();
		if (allServletPaths != null && allServletPaths.length > 0) {
			sb.append("<table>");
			sb.append("<tr><td><b>==Java Servlet部分==</b></td></tr>");
			for (String servletPath : allServletPaths) {
				sb.append("<tr><td><a style=\"margin-left:20px\" href=\""
						+ contextPath + servletPath + "\">" + contextPath
						+ servletPath + "</a></td></tr>");
			}
			sb.append("</table>");
		}

		ViewTypeServlet[] viewTypeServlets = WebContext
				.getViewTypeServletServlets();
		if (viewTypeServlets != null && viewTypeServlets.length > 0) {
			for (ViewTypeServlet viewTypeServlet : viewTypeServlets) {
				String[] urls = viewTypeServlet.getUrls();
				if (urls == null) {
					continue;
				}
				sb.append("<table>");
				sb.append("<tr><td><b>==视图类型["
						+ viewTypeServlet.getViewTypeName()
						+ "]部分==</b></td></tr>");
				for (String url : urls) {
					sb.append("<tr><td><a style=\"margin-left:20px\" href=\""
							+ contextPath + url + "\">" + contextPath + url
							+ "</a></td></tr>");
				}
				sb.append("</table>");
			}
		}
		sb.append("</body></html>");
		response.getWriter().write(sb.toString());
	}
}
