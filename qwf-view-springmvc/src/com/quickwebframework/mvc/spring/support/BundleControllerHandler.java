package com.quickwebframework.mvc.spring.support;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.quickwebframework.mvc.spring.BundleHandler;
import com.quickwebframework.mvc.spring.PluginPathMatcher;
import com.quickwebframework.mvc.spring.PluginUrlPathHelper;
import com.quickwebframework.mvc.spring.SpringMvcContext;
import com.quickwebframework.mvc.spring.entity.impl.PluginControllerInfo;

public class BundleControllerHandler implements BundleHandler {

	private static Log log = LogFactory.getLog(BundleControllerHandler.class);

	public void registerBundle(Bundle bundle,
			ApplicationContext applicationContext) {
		PluginControllerInfo pluginControllerInfo = new PluginControllerInfo(
				bundle);
		// Bundle的名称
		String bundleName = bundle.getSymbolicName();
		// 从ApplicationContext得到MVC控制器列表
		final Map<String, Object> handlerMap = applicationContext
				.getBeansWithAnnotation(Controller.class);

		Collection<Object> handlers = handlerMap.values();

		if (handlers != null) {
			for (Object handler : handlers) {
				Class<?> controllerClazz = handler.getClass();
				Method[] methods = controllerClazz.getMethods();
				for (Method method : methods) {
					// 查找RequestMapping注解
					RequestMapping requestMapping = method
							.getAnnotation(RequestMapping.class);
					if (requestMapping == null)
						continue;

					for (String methodName : requestMapping.value()) {
						// 内部URL，仅用于 Spring MVC内部匹配到控制器方法
						// 要获取外部URL，请调用WebContext.getBundleMethodUrl方法
						String innerMappingUrl = methodName;
						if (!innerMappingUrl.startsWith("/")) {
							innerMappingUrl = "/" + innerMappingUrl;
						}
						innerMappingUrl = "/" + bundleName + innerMappingUrl;

						RequestMethod[] requestMethods = requestMapping
								.method();
						// 如果方法为空，则映射所有的HTTP方法
						if (requestMethods == null
								|| requestMethods.length == 0) {
							requestMethods = RequestMethod.values();
						}

						StringBuilder sb = new StringBuilder();

						for (RequestMethod requestMethod : requestMethods) {
							sb.append(requestMethod.name());
							sb.append(",");

							// 添加到映射MAP中
							String tmpMappingUrl = requestMethod.name()
									.toUpperCase() + "_" + innerMappingUrl;
							pluginControllerInfo.getMappingUrlHandlerMap().put(
									tmpMappingUrl, handler);
						}

						if (sb.length() == 0)
							// 正常情况下，这儿不可能被执行到。
							sb.append("所有");
						else
							sb.setLength(sb.length() - 1);

						log.debug(String.format(
								"Spring MVC:映射内部URL路径[%s]的[%s]HTTP请求到处理器'%s'",
								innerMappingUrl, sb.toString(), handler
										.getClass().getName()));

						// 将处理器与对应的适配器放入映射中
						if (!pluginControllerInfo.getHandlerAdapterMap()
								.containsKey(handler)) {
							AnnotationMethodHandlerAdapter adapter = new AnnotationMethodHandlerAdapter();
							adapter.setPathMatcher(new PluginPathMatcher(bundle
									.getSymbolicName()));
							adapter.setUrlPathHelper(new PluginUrlPathHelper());
							pluginControllerInfo.getHandlerAdapterMap().put(
									handler, adapter);
						}
					}
				}
			}
		}
		SpringMvcContext.getSpringMvcFrameworkService()
				.addBundleControllerInfo(bundleName, pluginControllerInfo);
		log.debug("插件[" + bundleName + "]已注册为Spring MVC的Web App.");
	}

	public void unregisterBundle(Bundle bundle,
			ApplicationContext applicationContext) {
		// Bundle的名称
		String bundleName = bundle.getSymbolicName();
		SpringMvcContext.getSpringMvcFrameworkService()
				.removeBundleControllerInfo(bundleName);
		log.debug("插件[" + bundleName + "]注册在Spring MVC的Web App已经移除！");
	}
}
