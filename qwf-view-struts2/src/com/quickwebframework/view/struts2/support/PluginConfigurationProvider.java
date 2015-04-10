package com.quickwebframework.view.struts2.support;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationUtil;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.config.entities.ResultTypeConfig;
import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork2.config.providers.XmlHelper;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Context;
import com.opensymphony.xwork2.inject.Factory;
import com.opensymphony.xwork2.util.DomHelper;
import com.opensymphony.xwork2.util.location.LocatableProperties;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.quickwebframework.util.BundleUtils;
import com.quickwebframework.view.struts2.servlet.PluginStruts2DispatchServlet;

public class PluginConfigurationProvider extends XmlConfigurationProvider {

	private static final Logger LOG = LoggerFactory
			.getLogger(PluginConfigurationProvider.class);
	private String filename;
	private String reloadKey;
	private ServletContext servletContext;
	private Bundle bundle;

	/**
	 * Constructs the configuration provider
	 * 
	 * @param errorIfMissing
	 *            If we should throw an exception if the file can't be found
	 */
	public PluginConfigurationProvider() {
		this("/struts.xml", PluginStruts2DispatchServlet
				.getCurrentServletContext(), PluginStruts2DispatchServlet
				.getCurrentBundle());
	}

	/**
	 * Constructs the configuration provider
	 * 
	 * @param filename
	 *            The filename to look for
	 * @param errorIfMissing
	 *            If we should throw an exception if the file can't be found
	 * @param ctx
	 *            Our ServletContext
	 */
	public PluginConfigurationProvider(String filename, ServletContext ctx,
			Bundle bundle) {
		super(filename, false);
		this.servletContext = ctx;
		this.filename = filename;
		this.bundle = bundle;
		reloadKey = "configurationReload-" + filename;
		Map<String, String> dtdMappings = new HashMap<String, String>(
				getDtdMappings());
		dtdMappings
				.put("-//Apache Software Foundation//DTD Struts Configuration 2.0//EN",
						"struts-2.0.dtd");
		dtdMappings
				.put("-//Apache Software Foundation//DTD Struts Configuration 2.1//EN",
						"struts-2.1.dtd");
		dtdMappings
				.put("-//Apache Software Foundation//DTD Struts Configuration 2.1.7//EN",
						"struts-2.1.7.dtd");
		dtdMappings
				.put("-//Apache Software Foundation//DTD Struts Configuration 2.3//EN",
						"struts-2.3.dtd");
		setDtdMappings(dtdMappings);
	}

	private void refreshObjectFactory() {
		ClassLoader bundleClassLoader = BundleUtils.getBundleClassLoader(bundle);
		ObjectFactory objectFactory = new ObjectFactory();
		objectFactory.setClassLoader(bundleClassLoader);
		this.setObjectFactory(objectFactory);
	}

	@Override
	public void init(Configuration configuration) {
		super.init(configuration);
		refreshObjectFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.opensymphony.xwork2.config.providers.XmlConfigurationProvider#register
	 * (com.opensymphony.xwork2.inject.ContainerBuilder, java.util.Properties)
	 */
	@Override
	public void register(ContainerBuilder containerBuilder,
			LocatableProperties props) throws ConfigurationException {
		if (servletContext != null
				&& !containerBuilder.contains(ServletContext.class)) {
			containerBuilder.factory(ServletContext.class,
					new Factory<ServletContext>() {
						public ServletContext create(Context context)
								throws Exception {
							return servletContext;
						}
					});
		}
		super.register(containerBuilder, props);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.opensymphony.xwork2.config.providers.XmlConfigurationProvider#init
	 * (com.opensymphony.xwork2.config.Configuration)
	 */
	@Override
	public void loadPackages() {
		refreshObjectFactory();
		ActionContext ctx = ActionContext.getContext();
		ctx.put(reloadKey, Boolean.TRUE);
		super.loadPackages();
	}

	// 处理命名空间(加入插件名以及视图类型名)
	private String handleNamespace(String namespace) {
		namespace = "/" + bundle.getSymbolicName() + "/"
				+ Activator.getViewTypeServlet().getViewTypeName() + "/"
				+ namespace;
		while (namespace.contains("//")) {
			namespace = namespace.replace("//", "/");
		}
		if (namespace.endsWith("/")) {
			namespace = namespace.substring(0, namespace.length() - 1);
		}
		return namespace;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PackageConfig.Builder buildPackageContext(Element packageElement) {
		// Struts2的API设计得没法扩展，只有用反射来扩展了……
		Configuration configuration;
		Map<String, Element> declaredPackages;
		try {
			Class<?> clazz = XmlConfigurationProvider.class;
			Field configurationField = clazz.getDeclaredField("configuration");
			configurationField.setAccessible(true);
			configuration = (Configuration) configurationField.get(this);

			Field declaredPackagesField = clazz
					.getDeclaredField("declaredPackages");
			declaredPackagesField.setAccessible(true);
			declaredPackages = (Map<String, Element>) declaredPackagesField
					.get(this);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		String parent = packageElement.getAttribute("extends");
		String abstractVal = packageElement.getAttribute("abstract");
		boolean isAbstract = Boolean.parseBoolean(abstractVal);
		String name = StringUtils.defaultString(packageElement
				.getAttribute("name"));
		String namespace = handleNamespace(StringUtils
				.defaultString(packageElement.getAttribute("namespace")));
		String strictDMIVal = StringUtils.defaultString(packageElement
				.getAttribute("strict-method-invocation"));
		boolean strictDMI = Boolean.parseBoolean(strictDMIVal);

		if (StringUtils.isNotEmpty(packageElement
				.getAttribute("externalReferenceResolver"))) {
			throw new ConfigurationException(
					"The 'externalReferenceResolver' attribute has been removed.  Please use "
							+ "a custom ObjectFactory or Interceptor.",
					packageElement);
		}

		PackageConfig.Builder cfg = new PackageConfig.Builder(name)
				.namespace(namespace).isAbstract(isAbstract)
				.strictMethodInvocation(strictDMI)
				.location(DomHelper.getLocationObject(packageElement));

		if (StringUtils.isNotEmpty(StringUtils.defaultString(parent))) { // has
																			// parents,
																			// let's
																			// look
																			// it
																			// up
			List<PackageConfig> parents = new ArrayList<PackageConfig>();
			for (String parentPackageName : ConfigurationUtil
					.buildParentListFromString(parent)) {

				if (configuration.getPackageConfigNames().contains(
						parentPackageName)) {
					parents.add(configuration
							.getPackageConfig(parentPackageName));
				} else if (declaredPackages.containsKey(parentPackageName)) {
					if (configuration.getPackageConfig(parentPackageName) == null) {
						addPackage(declaredPackages.get(parentPackageName));
					}
					parents.add(configuration
							.getPackageConfig(parentPackageName));
				} else {
					throw new ConfigurationException(
							"Parent package is not defined: "
									+ parentPackageName);
				}

			}

			if (parents.size() <= 0) {
				cfg.needsRefresh(true);
			} else {
				cfg.addParents(parents);
			}
		}

		return cfg;
	}

	@Override
	protected Map<String, ResultConfig> buildResults(Element element,
			PackageConfig.Builder packageContext) {
		NodeList resultEls = element.getElementsByTagName("result");

		Map<String, ResultConfig> results = new LinkedHashMap<String, ResultConfig>();

		for (int i = 0; i < resultEls.getLength(); i++) {
			Element resultElement = (Element) resultEls.item(i);

			if (resultElement.getParentNode().equals(element)
					|| resultElement.getParentNode().getNodeName()
							.equals(element.getNodeName())) {
				String resultName = resultElement.getAttribute("name");
				String resultType = resultElement.getAttribute("type");

				// if you don't specify a name on <result/>, it defaults to
				// "success"
				if (StringUtils.isEmpty(resultName)) {
					resultName = Action.SUCCESS;
				}

				// there is no result type, so let's inherit from the parent
				// package
				if (StringUtils.isEmpty(resultType)) {
					resultType = packageContext.getFullDefaultResultType();

					// now check if there is a result type now
					if (StringUtils.isEmpty(resultType)) {
						// uh-oh, we have a problem
						throw new ConfigurationException(
								"No result type specified for result named '"
										+ resultName
										+ "', perhaps the parent package does not specify the result type?",
								resultElement);
					}
				}

				ResultTypeConfig config = packageContext
						.getResultType(resultType);

				if (config == null) {
					throw new ConfigurationException(
							"There is no result type defined for type '"
									+ resultType + "' mapped with name '"
									+ resultName + "'." + "  Did you mean '"
									+ guessResultType(resultType) + "'?",
							resultElement);
				}

				@SuppressWarnings("deprecation")
				String resultClass = config.getClazz();

				// invalid result type specified in result definition
				if (resultClass == null) {
					throw new ConfigurationException("Result type '"
							+ resultType + "' is invalid");
				}

				Map<String, String> resultParams = XmlHelper
						.getParams(resultElement);

				if (resultParams.size() == 0) // maybe we just have a body -
												// therefore a default parameter
				{
					// if <result ...>something</result> then we add a parameter
					// of 'something' as this is the most used result param
					if (resultElement.getChildNodes().getLength() >= 1) {
						resultParams = new LinkedHashMap<String, String>();

						String paramName = config.getDefaultResultParam();
						if (paramName != null) {
							StringBuilder paramValue = new StringBuilder();
							for (int j = 0; j < resultElement.getChildNodes()
									.getLength(); j++) {
								if (resultElement.getChildNodes().item(j)
										.getNodeType() == Node.TEXT_NODE) {
									String val = resultElement.getChildNodes()
											.item(j).getNodeValue();
									if (val != null) {
										paramValue.append(val);
									}
								}
							}
							String val = paramValue.toString().trim();
							if (val.length() > 0) {
								resultParams.put(paramName, val);
							}
						} else {
							if (LOG.isWarnEnabled()) {
								LOG.warn("no default parameter defined for result of type "
										+ config.getName());
							}
						}
					}
				}

				// 处理resultParams
				if ("redirectAction".equals(resultType)) {
					String namespace = resultParams.get("namespace");
					if (namespace != null) {
						namespace = handleNamespace(namespace);
					}
					resultParams.put("namespace", namespace);
				}

				// create new param map, so that the result param can override
				// the config param
				Map<String, String> params = new LinkedHashMap<String, String>();
				Map<String, String> configParams = config.getParams();
				if (configParams != null) {
					params.putAll(configParams);
				}
				params.putAll(resultParams);

				ResultConfig resultConfig = new ResultConfig.Builder(
						resultName, resultClass).addParams(params)
						.location(DomHelper.getLocationObject(element)).build();
				results.put(resultConfig.getName(), resultConfig);
			}
		}

		return results;
	}

	/**
	 * Look for the configuration file on the classpath and in the file system
	 * 
	 * @param fileName
	 *            The file name to retrieve
	 * @see com.opensymphony.xwork2.config.providers.XmlConfigurationProvider#getConfigurationUrls
	 */
	@Override
	protected Iterator<URL> getConfigurationUrls(String fileName)
			throws IOException {
		URL url = null;
		url = findInBundle(fileName);
		if (url == null) {
			return super.getConfigurationUrls(fileName);
		} else {
			List<URL> list = new ArrayList<URL>();
			list.add(url);
			return list.iterator();
		}
	}

	protected URL findInBundle(String fileName) throws IOException {
		URL url = null;
		if (LOG.isDebugEnabled()) {
			LOG.debug("Trying to load resource " + fileName
					+ " from OSGi bundle.");
		}
		url = bundle.getResource(fileName);
		return url;
	}

	/**
	 * Overrides needs reload to ensure it is only checked once per request
	 */
	@Override
	public boolean needsReload() {
		ActionContext ctx = ActionContext.getContext();
		if (ctx != null) {
			return ctx.get(reloadKey) == null && super.needsReload();
		} else {
			return super.needsReload();
		}

	}

	public String toString() {
		return ("qwf-view-struts2 XML configuration provider (" + filename + ")");
	}
}