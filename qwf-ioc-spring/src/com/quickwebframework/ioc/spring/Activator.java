package com.quickwebframework.ioc.spring;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.SynchronousBundleListener;

import com.quickwebframework.ioc.IocFrameworkService;
import com.quickwebframework.ioc.spring.service.impl.SpringIocFrameworkService;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private SpringIocFrameworkService springIocFrameworkService;
	private ServiceRegistration<?> iocFrameworkServiceRegistration;
	private BundleListener bundleListener;

	static BundleContext getContext() {
		return context;
	}

	public Activator() {
		bundleListener = new SynchronousBundleListener() {

			public void bundleChanged(BundleEvent event) {
				Bundle startingBundle = event.getBundle();
				int bundleEventType = event.getType();
				if (BundleEvent.STARTING == bundleEventType) {
					springIocFrameworkService.registerBundle(startingBundle);
				} else if (BundleEvent.STOPPED == bundleEventType) {
					springIocFrameworkService.unregisterBundle(startingBundle);
				}
			}
		};
	}

	public void start(BundleContext context) throws Exception {
		Activator.context = context;

		// 注册依赖注入服务
		springIocFrameworkService = new SpringIocFrameworkService();
		iocFrameworkServiceRegistration = context.registerService(
				IocFrameworkService.class.getName(), springIocFrameworkService,
				null);
		// 添加插件监听器
		context.addBundleListener(bundleListener);
	}

	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
		// 取消注册依赖注入服务
		iocFrameworkServiceRegistration.unregister();
		// 移除插件监听器
		context.removeBundleListener(bundleListener);
	}

}
