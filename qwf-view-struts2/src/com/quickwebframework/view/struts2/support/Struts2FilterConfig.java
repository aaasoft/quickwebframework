package com.quickwebframework.view.struts2.support;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;

public class Struts2FilterConfig implements FilterConfig {

	private ServletContext srcServletContext;
	private Dictionary<String, String> initDict;

	public Struts2FilterConfig(ServletConfig srcServletConfig,
			ServletContext srcServletContext, Bundle bundle) {
		this.srcServletContext = srcServletContext;
		initDict = new Hashtable<String, String>();
		Enumeration<String> initConfigEnum = srcServletConfig
				.getInitParameterNames();
		while (initConfigEnum.hasMoreElements()) {
			String key = initConfigEnum.nextElement();
			initDict.put(key, srcServletConfig.getInitParameter(key));
		}
		initDict.put("configProviders",
				"com.quickwebframework.view.struts2.support.PluginConfigurationProvider");
		initDict.put("bundle", bundle.getSymbolicName());
	}

	public String getInitParameter(String arg0) {
		return initDict.get(arg0);
	}

	public Enumeration<String> getInitParameterNames() {
		return initDict.keys();
	}

	public ServletContext getServletContext() {
		return srcServletContext;
	}

	public String getFilterName() {
		return "struts2";
	}
}
