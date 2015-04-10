package com.quickwebframework.ioc.spring.util;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.springframework.context.ApplicationContext;

import com.quickwebframework.ioc.spring.service.impl.SpringIocFrameworkService;

public class BundleApplicationContextUtils {

	/**
	 * 根据Bundle得到对应的ApplicationContext对象
	 * 
	 * @param bundle
	 * @return
	 */
	public static ApplicationContext getBundleApplicationContext(Bundle bundle) {
		return SpringIocFrameworkService.bundleApplicationContextMap
				.get(bundle);
	}

	private static List<ApplicationContextListener> applicationContextListenerList = new ArrayList<ApplicationContextListener>();

	public static List<ApplicationContextListener> getApplicationContextListenerList() {
		return applicationContextListenerList;
	}

	public static void addApplicationContextListener(
			ApplicationContextListener listener) {
		applicationContextListenerList.add(listener);
	}

	public static void removeApplicationContextListener(
			ApplicationContextListener listener) {
		applicationContextListenerList.remove(listener);
	}
}
