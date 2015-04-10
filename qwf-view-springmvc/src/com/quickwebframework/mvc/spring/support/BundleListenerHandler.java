package com.quickwebframework.mvc.spring.support;

import java.util.EventListener;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.springframework.context.ApplicationContext;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.mvc.spring.BundleHandler;

public class BundleListenerHandler implements BundleHandler {

	public void registerBundle(Bundle bundle,
			ApplicationContext applicationContext) {
		// 从ApplicationContext得到监听器列表
		Map<String, EventListener> listenerMap = applicationContext
				.getBeansOfType(EventListener.class);
		for (EventListener listener : listenerMap.values()) {
			WebContext.registerListener(bundle, listener);
		}
	}

	public void unregisterBundle(Bundle bundle,
			ApplicationContext applicationContext) {
		// 从ApplicationContext得到监听器列表
		Map<String, EventListener> listenerMap = applicationContext
				.getBeansOfType(EventListener.class);
		for (EventListener listener : listenerMap.values()) {
			WebContext.unregisterListener(bundle, listener);
		}
	}

}
