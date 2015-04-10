package com.quickwebframework.ioc.spring.util;

import org.osgi.framework.Bundle;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class BundleGenericXmlApplicationContext extends
		GenericApplicationContext {

	private final BundleResourceResolver resolver;
	private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(
			this);
	private Bundle bundle;

	public BundleGenericXmlApplicationContext(Bundle bundle) {
		this(bundle, null);
	}

	public BundleGenericXmlApplicationContext(Bundle bundle,
			ApplicationContext parentApplicationContext) {
		super(parentApplicationContext);
		this.bundle = bundle;
		resolver = new BundleResourceResolver(this.bundle);
	}

	/**
	 * Set namespaceHandlerResolver
	 * 
	 * @param namespaceHandlerResolver
	 */
	public void setNamespaceHandlerResolver(
			NamespaceHandlerResolver namespaceHandlerResolver) {
		reader.setNamespaceHandlerResolver(namespaceHandlerResolver);
	}

	/**
	 * Set whether to use XML validation. Default is <code>true</code>.
	 */
	public void setValidating(boolean validating) {
		this.reader.setValidating(validating);
	}

	/**
	 * Load bean definitions from the given XML resources.
	 * 
	 * @param resources
	 *            one or more resources to load from
	 */
	public void load(Resource... resources) {
		this.reader.loadBeanDefinitions(resources);
	}

	/**
	 * Load bean definitions from the given XML resources.
	 * 
	 * @param resourceLocations
	 *            one or more resource locations to load from
	 */
	public void load(String... resourceLocations) {
		this.reader.loadBeanDefinitions(resourceLocations);
	}

	/**
	 * Load bean definitions from the given XML resources.
	 * 
	 * @param relativeClass
	 *            class whose package will be used as a prefix when loading each
	 *            specified resource name
	 * @param resourceNames
	 *            relatively-qualified names of resources to load
	 */
	public void load(Class<?> relativeClass, String... resourceNames) {
		Resource[] resources = new Resource[resourceNames.length];
		for (int i = 0; i < resourceNames.length; i++) {
			resources[i] = new ClassPathResource(resourceNames[i],
					relativeClass);
		}
		this.load(resources);
	}

	@Override
	public Resource[] getResources(String path) {
		return resolver.getResources(path);
	}
}
