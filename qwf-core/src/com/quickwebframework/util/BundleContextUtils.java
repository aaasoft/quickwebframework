package com.quickwebframework.util;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class BundleContextUtils {
	// 得到OSGi服务对象
	public static Object getServiceObject(BundleContext bundleContext,
			String serviceName, String filter) {
		ServiceReference<?>[] serviceReferences = null;
		try {
			serviceReferences = bundleContext.getServiceReferences(serviceName,
					filter);
		} catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}

		if (serviceReferences != null && serviceReferences.length > 0) {
			return bundleContext.getService(serviceReferences[0]);
		}
		return null;
	}

	// 得到OSGi服务对象
	public static Object getServiceObject(BundleContext bundleContext,
			String serviceName) {
		if (bundleContext == null) {
			throw new RuntimeException("参数bundleContext为null!");
		}
		ServiceReference<?> serviceReference = bundleContext
				.getServiceReference(serviceName);
		if (serviceReference == null)
			return null;
		return bundleContext.getService(serviceReference);
	}
}
