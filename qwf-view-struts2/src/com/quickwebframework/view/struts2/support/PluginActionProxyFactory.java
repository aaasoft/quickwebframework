package com.quickwebframework.view.struts2.support;

import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.osgi.framework.Bundle;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.DefaultActionInvocation;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.quickwebframework.framework.OsgiContext;
import com.quickwebframework.framework.WebContext;
import com.quickwebframework.util.BundleUtils;

public class PluginActionProxyFactory implements ActionProxyFactory {

	protected Container container;

	public PluginActionProxyFactory() {
		super();
	}

	@Inject
	public void setContainer(Container container) {
		this.container = container;
	}

	public ActionProxy createActionProxy(String namespace, String actionName,
			Map<String, Object> extraContext) {
		return createActionProxy(namespace, actionName, null, extraContext,
				true, true);
	}

	public ActionProxy createActionProxy(String namespace, String actionName,
			String methodName, Map<String, Object> extraContext) {
		return createActionProxy(namespace, actionName, methodName,
				extraContext, true, true);
	}

	public ActionProxy createActionProxy(String namespace, String actionName,
			Map<String, Object> extraContext, boolean executeResult,
			boolean cleanupContext) {
		return createActionProxy(namespace, actionName, null, extraContext,
				executeResult, cleanupContext);
	}

	public ActionProxy createActionProxy(String namespace, String actionName,
			String methodName, Map<String, Object> extraContext,
			boolean executeResult, boolean cleanupContext) {

		DefaultActionInvocation inv = new DefaultActionInvocation(extraContext,
				true);
		container.inject(inv);
		// 将Bundle的ClassLoader注入到inv的objectFactory中
		try {
			ObjectFactory objectFactory = (ObjectFactory) FieldUtils.readField(
					inv, "objectFactory", true);
			@SuppressWarnings("rawtypes")
			Map requestMap = (Map) extraContext.get("request");
			String pluginName = (String) requestMap
					.get(WebContext.CONST_PLUGIN_NAME);
			Bundle bundle = OsgiContext.getBundleByName(pluginName);
			ClassLoader bundleClassLoader = BundleUtils
					.getBundleClassLoader(bundle);
			objectFactory.setClassLoader(bundleClassLoader);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return createActionProxy(inv, namespace, actionName, methodName,
				executeResult, cleanupContext);
	}

	public ActionProxy createActionProxy(ActionInvocation inv,
			String namespace, String actionName, boolean executeResult,
			boolean cleanupContext) {

		return createActionProxy(inv, namespace, actionName, null,
				executeResult, cleanupContext);
	}

	public ActionProxy createActionProxy(ActionInvocation inv,
			String namespace, String actionName, String methodName,
			boolean executeResult, boolean cleanupContext) {
		PluginActionProxy proxy = new PluginActionProxy(inv, namespace,
				actionName, methodName, executeResult, cleanupContext);
		container.inject(proxy);
		proxy.prepare();
		return proxy;
	}
}
