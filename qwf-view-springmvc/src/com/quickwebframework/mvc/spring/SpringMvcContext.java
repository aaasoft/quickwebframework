package com.quickwebframework.mvc.spring;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;

import com.quickwebframework.framework.FrameworkContext;
import com.quickwebframework.mvc.spring.service.impl.SpringMvcFrameworkService;
import com.quickwebframework.mvc.spring.support.Activator;
import com.quickwebframework.mvc.spring.support.BundleControllerHandler;
import com.quickwebframework.mvc.spring.support.BundleFilterHandler;
import com.quickwebframework.mvc.spring.support.BundleListenerHandler;
import com.quickwebframework.mvc.spring.support.BundleThreadHandler;

public class SpringMvcContext extends FrameworkContext {
	private static SpringMvcContext instance;

	public static SpringMvcContext getInstance() {
		if (instance == null)
			instance = new SpringMvcContext();
		return instance;
	}

	private static SpringMvcFrameworkService mvcFrameworkService;

	/**
	 * 得到Spring MVC框架服务
	 * 
	 * @return
	 */
	public static SpringMvcFrameworkService getSpringMvcFrameworkService() {
		return mvcFrameworkService;
	}

	@Override
	protected BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	protected void init(int arg) {
		// 注册MVC框架服务
		mvcFrameworkService = new SpringMvcFrameworkService();

		// 注册Spring MVC框架的BundleHandler
		mvcFrameworkService
				.registerBundleHandler(new BundleControllerHandler());
		mvcFrameworkService.registerBundleHandler(new BundleFilterHandler());
		mvcFrameworkService.registerBundleHandler(new BundleListenerHandler());
		mvcFrameworkService.registerBundleHandler(new BundleThreadHandler());

		// 添加到MVC框架中
		for (Bundle bundle : this.getBundleContext().getBundles()) {
			// 如果状态是已激活
			if (bundle.getState() == Bundle.ACTIVE) {
				registerBundle(bundle);
			}
		}
	}

	@Override
	protected void destory(int arg) {
		// 从MVC框架中移除
		for (Bundle bundle : mvcFrameworkService.bundleApplicationContextMap
				.keySet().toArray(new Bundle[0])) {
			unregisterBundle(bundle);
		}
	}

	@Override
	protected void bundleChanged(BundleEvent event) {
		int bundleEventType = event.getType();
		Bundle bundle = event.getBundle();
		if (BundleEvent.STARTED == bundleEventType) {
			// 注册插件到Spring MVC上下文中
			registerBundle(bundle);
		} else if (BundleEvent.STOPPING == bundleEventType) {
			// 移除插件的控制器
			unregisterBundle(bundle);
		}
	}

	@Override
	protected void serviceChanged(ServiceEvent event) {

	}

	/**
	 * 注册Bundle到Spring MVC上下文中
	 * 
	 * @param bundle
	 */
	public static void registerBundle(Bundle bundle) {
		// 如果是系统Bundle，则不处理
		if (bundle.getBundleId() == 0) {
			return;
		}
		mvcFrameworkService.registerBundle(bundle);
	}

	/**
	 * 取消注册Bundle到Spring MVC上下文中
	 * 
	 * @param bundle
	 */
	public static void unregisterBundle(Bundle bundle) {
		mvcFrameworkService.unregisterBundle(bundle);
	}
}
