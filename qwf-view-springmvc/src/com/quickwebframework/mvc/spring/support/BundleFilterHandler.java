package com.quickwebframework.mvc.spring.support;

import java.util.Map;

import javax.servlet.Filter;

import org.osgi.framework.Bundle;
import org.springframework.context.ApplicationContext;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.mvc.spring.BundleHandler;

/**
 * Bundle的过滤器处理器
 * 
 * @author AAA
 * 
 */
public class BundleFilterHandler implements BundleHandler {

	public void registerBundle(Bundle bundle,
			ApplicationContext applicationContext) {
		// 从ApplicationContext得到过滤器列表
		Map<String, Filter> filterMap = applicationContext
				.getBeansOfType(Filter.class);
		for (Filter filter : filterMap.values()) {
			WebContext.registerFilter(bundle, filter);
		}
	}

	public void unregisterBundle(Bundle bundle,
			ApplicationContext applicationContext) {
		// 从ApplicationContext得到过滤器列表
		Map<String, Filter> filterMap = applicationContext
				.getBeansOfType(Filter.class);
		for (Filter filter : filterMap.values()) {
			WebContext.unregisterFilter(bundle, filter);
		}
	}
}
