package com.quickwebframework.ioc.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.ioc.IocContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		IocContext.getInstance().init();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		IocContext.getInstance().destory();
		Activator.context = null;
	}

}
