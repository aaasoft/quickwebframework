package com.quickwebframework.mvc.spring;

import org.osgi.framework.Bundle;
import org.springframework.context.ApplicationContext;

public interface BundleHandler {
	/**
	 * 注册Bundle
	 * 
	 * @param bundle
	 * @param applicationContext
	 */
	public void registerBundle(Bundle bundle,
			ApplicationContext applicationContext);

	/**
	 * 取消注册Bundle
	 * 
	 * @param bundle
	 * @param applicationContext
	 */
	public void unregisterBundle(Bundle bundle,
			ApplicationContext applicationContext);
}
