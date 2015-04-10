package com.quickwebframework.db.jdbc.support;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.db.jdbc.DataSourceContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		DataSourceContext.getInstance().init();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		DataSourceContext.getInstance().destory();
		Activator.context = null;
	}

}
