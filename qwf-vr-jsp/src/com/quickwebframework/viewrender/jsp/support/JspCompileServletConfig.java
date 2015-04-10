package com.quickwebframework.viewrender.jsp.support;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class JspCompileServletConfig implements ServletConfig {

	private ServletConfig srcServletConfig;
	private ServletContext srcServletContext;

	public JspCompileServletConfig(ServletConfig srcServletConfig,
			ServletContext srcServletContext) {
		this.srcServletConfig = srcServletConfig;
		this.srcServletContext = srcServletContext;
	}

	public String getInitParameter(String arg0) {
		return srcServletConfig.getInitParameter(arg0);
	}

	public Enumeration<String> getInitParameterNames() {
		return srcServletConfig.getInitParameterNames();
	}

	public ServletContext getServletContext() {
		return srcServletContext;
	}

	public String getServletName() {
		return srcServletConfig.getServletName();
	}

}
