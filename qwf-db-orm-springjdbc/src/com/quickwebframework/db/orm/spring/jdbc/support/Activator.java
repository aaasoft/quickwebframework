package com.quickwebframework.db.orm.spring.jdbc.support;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.db.orm.spring.jdbc.JdbcTemplateContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		JdbcTemplateContext.getInstance().init();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		JdbcTemplateContext.getInstance().destory();
		Activator.context = null;
	}
}
