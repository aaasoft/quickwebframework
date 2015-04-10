package com.quickwebframework.mvc.spring.support;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.mvc.spring.SpringMvcContext;
import com.quickwebframework.mvc.spring.servlet.SpringMvcViewTypeServlet;
import com.quickwebframework.servlet.ViewTypeServlet;

public class Activator implements BundleActivator {

	public final static String BUNDLE_NAME = "qwf-view-springmvc";
	private static BundleContext context;
	private static ViewTypeServlet viewTypeServlet;

	public static BundleContext getContext() {
		return context;
	}

	public static ViewTypeServlet getViewTypeServlet() {
		return viewTypeServlet;
	}

	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		SpringMvcContext.getInstance().init();
		viewTypeServlet = new SpringMvcViewTypeServlet();
		viewTypeServlet.register();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		viewTypeServlet.unregister();
		SpringMvcContext.getInstance().destory();
		Activator.context = null;
	}
}
