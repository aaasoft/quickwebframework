package com.quickwebframework.ioc.spring.util;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.UrlResource;

public class BundleScanner {

	private ApplicationContext doScan(Bundle bundle,
			ClassLoader bundleClassLoader) {
		// 检查插件的根路径下面是否有applicationContext.xml文件
		URL applicationContextUrl = bundle
				.getResource("applicationContext.xml");

		GenericApplicationContext applicationContext = null;

		List<ApplicationContextListener> listenerList = BundleApplicationContextUtils
				.getApplicationContextListenerList();

		Map<String, BeanDefinition> extraBeanDefinitionMap = new HashMap<String, BeanDefinition>();

		for (ApplicationContextListener listener : listenerList) {
			Map<String, BeanDefinition> tmpMap = listener
					.getExtraBeanDefinitions();
			if (tmpMap == null) {
				continue;
			}
			extraBeanDefinitionMap.putAll(tmpMap);
		}

		// 如果有xml文件，则初始化BundleGenericXmlApplicationContext类
		if (applicationContextUrl != null) {
			applicationContext = new BundleGenericXmlApplicationContext(bundle);
		}
		// 否则初始化BundleAnnotationConfigApplicationContext类
		else {
			applicationContext = new BundleAnnotationConfigApplicationContext(
					bundle);
		}

		// 将用户指定的Bean定义加入到Spring上下文中
		for (String beanName : extraBeanDefinitionMap.keySet()) {
			BeanDefinition beanDefinition = extraBeanDefinitionMap
					.get(beanName);
			// 注册Bean定义
			applicationContext.registerBeanDefinition(beanName, beanDefinition);
		}

		// 触发Starting事件
		for (ApplicationContextListener listener : listenerList)
			listener.contextStarting(applicationContext, bundle);

		// 如果有xml文件，则让Spring加载这个xml文件
		if (applicationContextUrl != null) {
			try {
				BundleGenericXmlApplicationContext bundleGenericXmlApplicationContext = (BundleGenericXmlApplicationContext) applicationContext;
				bundleGenericXmlApplicationContext
						.setNamespaceHandlerResolver(new BundleNamespaceHandlerResolver(
								this.getClass().getClassLoader(),
								bundleClassLoader));
				bundleGenericXmlApplicationContext
						.setClassLoader(bundleClassLoader);
				bundleGenericXmlApplicationContext.load(new UrlResource(
						applicationContextUrl));
				bundleGenericXmlApplicationContext.refresh();
				bundleGenericXmlApplicationContext.start();
			} catch (Exception ex) {
				throw new RuntimeException("Spring加载插件["
						+ bundle.getSymbolicName()
						+ "]中的applicationContext.xml文件时出错异常！", ex);
			}
		}
		// 否则扫描插件的所有文件
		else {
			// 初始化AnnotationConfigApplicationContext
			BundleAnnotationConfigApplicationContext bundleAnnotationConfigApplicationContext = (BundleAnnotationConfigApplicationContext) applicationContext;
			try {
				// 开始Spring扫描
				bundleAnnotationConfigApplicationContext
						.setClassLoader(bundleClassLoader);
				bundleAnnotationConfigApplicationContext.scan("*");
				bundleAnnotationConfigApplicationContext.refresh();
				bundleAnnotationConfigApplicationContext.start();
			} catch (Exception ex) {
				throw new RuntimeException("用Spring扫描插件["
						+ bundle.getSymbolicName() + "]时出错异常！", ex);
			}
		}

		// 触发Started事件
		for (ApplicationContextListener listener : listenerList)
			listener.contextStarted(applicationContext, bundle);

		return applicationContext;
	}

	public ApplicationContext scan(Bundle bundle, ClassLoader bundleClassLoader) {
		return doScan(bundle, bundleClassLoader);
	}
}
