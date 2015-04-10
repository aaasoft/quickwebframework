package com.quickwebframework.ioc.spring.util;

import java.util.EventListener;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;

public interface ApplicationContextListener extends EventListener {

	/**
	 * 得到预加载Beans
	 * 
	 * @return
	 */
	public Map<String, BeanDefinition> getExtraBeanDefinitions();

	/**
	 * ApplicationContext 开始之前
	 * 
	 * @param applicationContext
	 * @param bundle
	 */
	public void contextStarting(ApplicationContext applicationContext,
			Bundle bundle);

	/**
	 * ApplicationContext 开始之后
	 * 
	 * @param applicationContext
	 * @param bundle
	 */
	public void contextStarted(ApplicationContext applicationContext,
			Bundle bundle);
}
