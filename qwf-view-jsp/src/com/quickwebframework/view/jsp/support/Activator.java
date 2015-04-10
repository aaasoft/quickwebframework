package com.quickwebframework.view.jsp.support;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.servlet.ViewTypeServlet;
import com.quickwebframework.view.jsp.servlet.JspViewTypeServlet;

public class Activator implements BundleActivator {

	public final static String BUNDLE_NAME = "qwf-view-jsp";
	private static BundleContext context;
	private ViewTypeServlet viewTypeServlet;

	public static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		viewTypeServlet = new JspViewTypeServlet();
		viewTypeServlet.register();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		viewTypeServlet.unregister();
		Activator.context = null;
	}

}
