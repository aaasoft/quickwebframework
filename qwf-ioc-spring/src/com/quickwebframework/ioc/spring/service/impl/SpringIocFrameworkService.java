package com.quickwebframework.ioc.spring.service.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.springframework.context.ApplicationContext;

import com.quickwebframework.ioc.IocFrameworkService;
import com.quickwebframework.ioc.spring.util.BundleApplicationContextUtils;
import com.quickwebframework.ioc.spring.util.BundleClassLoaderProxy;
import com.quickwebframework.ioc.spring.util.BundleScanner;
import com.quickwebframework.util.BundleUtils;

public class SpringIocFrameworkService implements IocFrameworkService {

	private BundleScanner scanner = new BundleScanner();
	public static Map<Bundle, ApplicationContext> bundleApplicationContextMap = new HashMap<Bundle, ApplicationContext>();

	/**
	 * 注册Bundle到Spring IoC上下文中
	 * 
	 * @param bundle
	 */

	public void registerBundle(Bundle bundle) {
		if (!bundleApplicationContextMap.containsKey(bundle)) {
			ClassLoader bundleClassLoader = BundleUtils
					.getBundleClassLoader(bundle);
			// 加一个代理
			bundleClassLoader = new BundleClassLoaderProxy(bundle,
					bundleClassLoader);
			ApplicationContext applicationContext = scanner.scan(bundle,
					bundleClassLoader);
			bundleApplicationContextMap.put(bundle, applicationContext);
		} else {
			// Bundle already added,so here do nothing.
		}
	}

	/**
	 * 取消注册Bundle到Spring IoC上下文中
	 * 
	 * @param bundle
	 */

	public void unregisterBundle(Bundle bundle) {
		bundleApplicationContextMap.remove(bundle);
	}

	/**
	 * Spring MVC上下文中是否包含此Bundle
	 */

	public boolean containsBundle(Bundle bundle) {
		return bundleApplicationContextMap.containsKey(bundle);
	}

	/**
	 * 
	 */

	public Object getBundleApplicationContext(Bundle bundle) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle);
	}

	public boolean containsBean(Bundle bundle, String beanName) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle).containsBean(beanName);
	}

	public <T> T getBean(Bundle bundle, Class<T> clazz) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle).getBean(clazz);
	}

	public Object getBean(Bundle bundle, String beanName) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle).getBean(beanName);
	}

	public int getBeanDefinitionCount(Bundle bundle) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle).getBeanDefinitionCount();
	}

	public String[] getBeanDefinitionNames(Bundle bundle) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle).getBeanDefinitionNames();
	}

	public String[] getBeanNamesForType(Bundle bundle, Class<?> clazz) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle).getBeanNamesForType(clazz);
	}

	public <T> Map<String, T> getBeansOfType(Bundle bundle, Class<T> clazz) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle).getBeansOfType(clazz);
	}

	public Map<String, Object> getBeansWithAnnotation(Bundle bundle,
			Class<? extends Annotation> annotationClazz) {
		return BundleApplicationContextUtils
				.getBundleApplicationContext(bundle).getBeansWithAnnotation(
						annotationClazz);
	}
}
