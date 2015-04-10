package com.quickwebframework.web.servlet;

import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public abstract class QwfServlet extends HttpServlet {
	/**
	 * 请求的URL是否匹配此Servlet
	 * 
	 * @param requestUrlWithoutContextPath
	 * @return
	 */
	public abstract boolean isUrlMatch(String requestUrlWithoutContextPath);
}
