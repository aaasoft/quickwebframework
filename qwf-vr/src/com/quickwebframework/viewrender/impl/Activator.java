package com.quickwebframework.viewrender.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.viewrender.ViewRenderContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		ViewRenderContext.getInstance().init();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		ViewRenderContext.getInstance().destory();

		Activator.context = null;
	}
}
