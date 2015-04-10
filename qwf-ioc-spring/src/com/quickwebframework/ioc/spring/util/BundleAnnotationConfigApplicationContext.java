package com.quickwebframework.ioc.spring.util;

import org.osgi.framework.Bundle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;

public class BundleAnnotationConfigApplicationContext extends
		AnnotationConfigApplicationContext {

	private final BundleResourceResolver resolver;
	private Bundle bundle;

	public BundleAnnotationConfigApplicationContext(Bundle bundle) {
		this.bundle = bundle;
		resolver = new BundleResourceResolver(this.bundle);
	}

	@Override
	public Resource[] getResources(String path) {
		return resolver.getResources(path);
	}
}
