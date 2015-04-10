package com.qwf.school.mis.core;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.framework.WebContext;
import com.qwf.school.mis.core.util.MisMenuUtils;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Map<String, String> subMenuMap = new HashMap<String, String>();
		String contextPath = WebContext.getServletContext().getContextPath();
		subMenuMap
				.put("欢迎页", contextPath + "/qwf-school-mis-core/spring/index");
		subMenuMap.put("退出", "#");
		MisMenuUtils.registerMenu("主菜单", subMenuMap);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		MisMenuUtils.removeMenu("主菜单", null);
		Activator.context = null;
	}
}
