package com.quickwebframework.view.struts2.support;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.servlet.ViewTypeServlet;
import com.quickwebframework.view.struts2.servlet.Struts2ViewTypeServlet;

public class Activator implements BundleActivator {

	public final static String BUNDLE_NAME = "qwf-view-struts2";
	private static BundleContext context;
	private static ViewTypeServlet viewTypeServlet;

	public static BundleContext getContext() {
		return context;
	}

	public static ServletContext getServletContext() {
		return WebContext.getServletContext();
	}

	public static ViewTypeServlet getViewTypeServlet() {
		return viewTypeServlet;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		viewTypeServlet = new Struts2ViewTypeServlet();
		viewTypeServlet.register();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		viewTypeServlet.unregister();
		Activator.context = null;
	}
}
