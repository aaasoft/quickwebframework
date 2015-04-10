package com.quickwebframework.ioc.spring.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;

public class BundleClassLoaderProxy extends ClassLoader {
	private static Log log = LogFactory.getLog(BundleClassLoaderProxy.class);

	private Bundle bundle;

	private ClassLoader bundleClassLoader;
	private ClassLoader springClassLoader;

	public BundleClassLoaderProxy(Bundle bundle, ClassLoader bundleClassLoader) {
		this(bundle, bundleClassLoader, BundleClassLoaderProxy.class
				.getClassLoader());
	}

	public BundleClassLoaderProxy(Bundle bundle, ClassLoader bundleClassLoader,
			ClassLoader springClassLoader) {
		super(bundleClassLoader);

		this.bundle = bundle;
		this.bundleClassLoader = bundleClassLoader;
		this.springClassLoader = springClassLoader;
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		log.warn("BundleClassLoaderProxy.getResources:" + name + "  at Bundle:"
				+ bundle.getSymbolicName());
		// 如果是读取class文件，则交给真正的ClassLoader去处理
		if (name.endsWith(".class")) {
			return super.getResources(name);
		}

		List<URL> list = new ArrayList<URL>();

		Enumeration<URL> urlEnum = bundleClassLoader.getResources(name);
		if (urlEnum != null) {
			while (urlEnum.hasMoreElements()) {
				list.add(urlEnum.nextElement());
			}
		}
		urlEnum = springClassLoader.getResources(name);
		if (urlEnum != null) {
			while (urlEnum.hasMoreElements()) {
				list.add(urlEnum.nextElement());
			}
		}
		return Collections.enumeration(list);
	}
}