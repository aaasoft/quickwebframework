package com.quickwebframework.viewrender.servlet;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.servlet.ViewTypeServlet;
import com.quickwebframework.viewrender.ViewRenderContext;
import com.quickwebframework.viewrender.ViewRenderService;

public abstract class VrViewTypeServlet extends ViewTypeServlet {

	private static final long serialVersionUID = 6988769575885793931L;

	private String viewRenderName;
	private ViewRenderService viewRenderService;
	private ServiceListener serviceListener;

	/**
	 * 获取视图渲染器名称
	 * 
	 * @return
	 */
	public String getViewRenderName() {
		return viewRenderName;
	}

	/**
	 * 设置视图渲染器名称
	 * 
	 * @param viewRenderName
	 */
	public void setViewRenderName(String viewRenderName) {
		this.viewRenderName = viewRenderName;
		viewRenderService = ViewRenderContext
				.getViewRenderService(viewRenderName);
	}

	public ViewRenderService getViewRenderService() {
		return viewRenderService;
	}

	public VrViewTypeServlet() {
		String bundleName = getBundleName();
		viewRenderName = WebContext
				.getQwfConfig(bundleName + ".viewRenderName");
		viewRenderService = ViewRenderContext
				.getViewRenderService(viewRenderName);
		serviceListener = new ServiceListener() {

			public void serviceChanged(ServiceEvent event) {
				ServiceReference<?> serviceReference = event
						.getServiceReference();
				if (serviceReference.getBundle().getSymbolicName()
						.equals(viewRenderName)
						&& serviceReference.toString().contains(
								ViewRenderService.class.getName())) {
					viewRenderService = ViewRenderContext
							.getViewRenderService(viewRenderName);
				}
			}
		};
	}

	@Override
	// 注册
	public void register() {
		BundleContext bundleContext = getBundleContext();
		bundleContext.addServiceListener(serviceListener);
		super.register();
	}

	// 取消注册
	@Override
	public void unregister() {
		super.unregister();
		BundleContext bundleContext = getBundleContext();
		bundleContext.removeServiceListener(serviceListener);
	}

	/**
	 * 得到插件的BundleContext对象
	 * 
	 * @return
	 */
	public abstract BundleContext getBundleContext();
}
