package com.quickwebframework.viewrender;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

import com.quickwebframework.framework.FrameworkContext;
import com.quickwebframework.viewrender.impl.Activator;

public class ViewRenderContext extends FrameworkContext {
	private static ViewRenderContext instance;

	public static ViewRenderContext getInstance() {
		if (instance == null)
			instance = new ViewRenderContext();
		return instance;
	}

	// 视图渲染服务
	private static ViewRenderService defaultViewRenderService;

	public static ViewRenderService getDefaultViewRenderService() {
		return defaultViewRenderService;
	}

	public static ViewRenderService getViewRenderService(String viewRenderName) {
		BundleContext bundleContext = Activator.getContext();
		try {
			ServiceReference<?>[] serviceReferences = bundleContext
					.getServiceReferences(ViewRenderService.class.getName(),
							"(bundle=" + viewRenderName + ")");
			if (serviceReferences == null || serviceReferences.length == 0) {
				return null;
			}
			return (ViewRenderService) bundleContext
					.getService(serviceReferences[0]);
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	protected void init(int arg0) {
		super.addSimpleServiceStaticFieldLink(
				ViewRenderService.class.getName(), "defaultViewRenderService");
	}

	@Override
	protected void destory(int arg0) {
	}

	@Override
	protected void bundleChanged(BundleEvent event) {

	}

	@Override
	protected void serviceChanged(ServiceEvent event) {

	}
}
