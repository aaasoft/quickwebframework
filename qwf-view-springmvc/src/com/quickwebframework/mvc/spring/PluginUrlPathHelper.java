package com.quickwebframework.mvc.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UrlPathHelper;

import com.quickwebframework.framework.WebContext;

public class PluginUrlPathHelper extends UrlPathHelper {

	public String getLookupPathForRequest(HttpServletRequest request) {
		return getRequestUri(request);
	}

	public String getRequestUri(HttpServletRequest request) {
		String result = "/"
				+ request.getAttribute(WebContext.CONST_PLUGIN_NAME) + "/"
				+ request.getAttribute(WebContext.CONST_PATH_NAME);
		return result;
	}
}
