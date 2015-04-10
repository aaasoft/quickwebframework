package com.quickwebframework.db.jdbc.spring.impl;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.quickwebframework.db.jdbc.DataSourceContext;
import com.quickwebframework.db.jdbc.DataSourceProxy;
import com.quickwebframework.ioc.spring.util.ApplicationContextListener;
import com.quickwebframework.ioc.spring.util.BundleApplicationContextUtils;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private ApplicationContextListener applicationContextListener;

	static BundleContext getContext() {
		return context;
	}

	public Activator() {
		applicationContextListener = new ApplicationContextListener() {

			public Map<String, BeanDefinition> getExtraBeanDefinitions() {
				Map<String, BeanDefinition> rtnMap = new HashMap<String, BeanDefinition>();
				String[] propertyNames = DataSourceContext
						.getDataSourcePropertyNames();
				for (String propertyName : propertyNames) {
					DataSource dataSource = DataSourceContext
							.getDataSource(propertyName);
					if (dataSource == null) {
						continue;
					}

					String beanName;
					if (propertyName.equals("")) {
						beanName = "dataSource";
					} else {
						beanName = "dataSource_" + propertyName;
					}

					// 生成Bean定义
					BeanDefinitionBuilder dataSourceBeanDefinitionBuilder = BeanDefinitionBuilder
							.genericBeanDefinition(DataSourceProxy.class);
					AbstractBeanDefinition beanDefinition = dataSourceBeanDefinitionBuilder
							.getBeanDefinition();
					ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
					constructorArgumentValues.addIndexedArgumentValue(0,
							dataSource);
					beanDefinition
							.setConstructorArgumentValues(constructorArgumentValues);
					rtnMap.put(beanName, beanDefinition);
				}
				return rtnMap;
			}

			public void contextStarting(ApplicationContext applicationContext,
					Bundle bundle) {
			}

			public void contextStarted(ApplicationContext applicationContext,
					Bundle bundle) {
			}
		};
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		BundleApplicationContextUtils
				.addApplicationContextListener(applicationContextListener);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		BundleApplicationContextUtils
				.removeApplicationContextListener(applicationContextListener);
		Activator.context = null;
	}
}
