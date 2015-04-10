package com.quickwebframework.viewrender.jsp.support;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import com.quickwebframework.viewrender.ViewRenderService;
import com.quickwebframework.viewrender.jsp.service.impl.ViewRenderServiceImpl;

public class Activator implements BundleActivator {
	public final static String BUNDLE_NAME = "qwf-vr-jsp";
	private static BundleContext context;
	private static ViewRenderService viewRenderService;

	public static ViewRenderService getViewRenderService() {
		return viewRenderService;
	}

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// 注册视图渲染服务
		viewRenderService = new ViewRenderServiceImpl();
		viewRenderService.registerService(bundleContext);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		// 取消注册视图渲染服务
		viewRenderService.unregisterService(bundleContext);
		Activator.context = null;
	}
}