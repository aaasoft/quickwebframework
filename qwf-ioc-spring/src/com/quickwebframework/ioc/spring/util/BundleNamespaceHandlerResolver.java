package com.quickwebframework.ioc.spring.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

public class BundleNamespaceHandlerResolver implements NamespaceHandlerResolver {

	/**
	 * The location to look for the mapping files. Can be present in multiple
	 * JAR files.
	 */
	public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/** Spring的类加载器 */
	private final ClassLoader springClassLoader;
	/** 插件的类加载器 */
	private final ClassLoader bundleClassLoader;

	/** Resource location to search for */
	private final String handlerMappingsLocation;

	/**
	 * Stores the mappings from namespace URI to NamespaceHandler class name /
	 * instance
	 */
	private volatile Map<String, Object> handlerMappings;

	public BundleNamespaceHandlerResolver(ClassLoader bundleClassLoader) {
		this(BundleNamespaceHandlerResolver.class.getClassLoader(),
				bundleClassLoader, DEFAULT_HANDLER_MAPPINGS_LOCATION);
	}

	public BundleNamespaceHandlerResolver(ClassLoader springClassLoader,
			ClassLoader bundleClassLoader) {
		this(springClassLoader, bundleClassLoader,
				DEFAULT_HANDLER_MAPPINGS_LOCATION);
	}

	public BundleNamespaceHandlerResolver(ClassLoader springClassLoader,
			ClassLoader bundleClassLoader, String handlerMappingsLocation) {
		this.springClassLoader = springClassLoader;
		this.bundleClassLoader = bundleClassLoader;
		this.handlerMappingsLocation = handlerMappingsLocation;
	}

	/**
	 * Locate the {@link NamespaceHandler} for the supplied namespace URI from
	 * the configured mappings.
	 * 
	 * @param namespaceUri
	 *            the relevant namespace URI
	 * @return the located {@link NamespaceHandler}, or <code>null</code> if
	 *         none found
	 */
	public NamespaceHandler resolve(String namespaceUri) {
		Map<String, Object> handlerMappings = getHandlerMappings();
		Object handlerOrClassName = handlerMappings.get(namespaceUri);
		if (handlerOrClassName == null) {
			return null;
		} else if (handlerOrClassName instanceof NamespaceHandler) {
			return (NamespaceHandler) handlerOrClassName;
		} else {
			String className = (String) handlerOrClassName;
			try {
				Class<?> handlerClass = null;
				try {
					handlerClass = ClassUtils.forName(className,
							this.springClassLoader);
				} catch (ClassNotFoundException ex) {
					handlerClass = ClassUtils.forName(className,
							this.bundleClassLoader);
				}

				if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
					throw new FatalBeanException("Class [" + className
							+ "] for namespace [" + namespaceUri
							+ "] does not implement the ["
							+ NamespaceHandler.class.getName() + "] interface");
				}
				NamespaceHandler namespaceHandler = (NamespaceHandler) BeanUtils
						.instantiateClass(handlerClass);
				namespaceHandler.init();
				handlerMappings.put(namespaceUri, namespaceHandler);
				return namespaceHandler;
			} catch (ClassNotFoundException ex) {
				throw new FatalBeanException("NamespaceHandler class ["
						+ className + "] for namespace [" + namespaceUri
						+ "] not found", ex);
			} catch (LinkageError err) {
				throw new FatalBeanException(
						"Invalid NamespaceHandler class ["
								+ className
								+ "] for namespace ["
								+ namespaceUri
								+ "]: problem with handler class file or dependent class",
						err);
			}
		}
	}

	/**
	 * Load the specified NamespaceHandler mappings lazily.
	 */
	private Map<String, Object> getHandlerMappings() {
		if (this.handlerMappings == null) {
			synchronized (this) {
				if (this.handlerMappings == null) {
					try {
						// 加载本类的类加载器的配置文件
						Properties springMappings = PropertiesLoaderUtils
								.loadAllProperties(
										this.handlerMappingsLocation,
										springClassLoader);
						if (logger.isDebugEnabled()) {
							logger.debug("Loaded Spring NamespaceHandler mappings: "
									+ springMappings);
						}
						// 加载插件的类加载器的配置文件
						Properties bundleMappings = PropertiesLoaderUtils
								.loadAllProperties(
										this.handlerMappingsLocation,
										this.bundleClassLoader);
						if (logger.isDebugEnabled()) {
							logger.debug("Loaded Bundle NamespaceHandler mappings: "
									+ bundleMappings);
						}

						Map<String, Object> handlerMappings = new ConcurrentHashMap<String, Object>(
								springMappings.size() + bundleMappings.size());
						CollectionUtils.mergePropertiesIntoMap(springMappings,
								handlerMappings);
						CollectionUtils.mergePropertiesIntoMap(bundleMappings,
								handlerMappings);

						this.handlerMappings = handlerMappings;
					} catch (IOException ex) {
						throw new IllegalStateException(
								"Unable to load NamespaceHandler mappings from location ["
										+ this.handlerMappingsLocation + "]",
								ex);
					}
				}
			}
		}
		return this.handlerMappings;
	}

	@Override
	public String toString() {
		return "BundleNamespaceHandlerResolver using mappings "
				+ getHandlerMappings();
	}
}
