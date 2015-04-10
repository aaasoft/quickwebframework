package com.quickwebframework.db.jdbc;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;

import com.quickwebframework.db.jdbc.support.Activator;
import com.quickwebframework.framework.FrameworkContext;
import com.quickwebframework.framework.WebContext;
import com.quickwebframework.util.PropertiesUtils;

public class DataSourceContext extends FrameworkContext {
	private static DataSourceContext instance;

	public static DataSourceContext getInstance() {
		if (instance == null)
			instance = new DataSourceContext();
		return instance;
	}

	// JDBC配置初始化器
	private static PropertiesInitializer propertiesInitializer;
	// 配置名称与数据源映射
	private static Map<String, DataSourceProxy> propertyNameDataSourceMap = new HashMap<String, DataSourceProxy>();

	public static PropertiesInitializer getPropertiesInitializer() {
		return propertiesInitializer;
	}

	/**
	 * 设置全局JDBC配置初始化器
	 * 
	 * @param propertiesInitializer
	 */
	public static void setPropertiesInitializer(
			PropertiesInitializer propertiesInitializer) {
		DataSourceContext.propertiesInitializer = propertiesInitializer;
	}

	@Override
	protected BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	protected void init(int arg) {
	}

	@Override
	protected void destory(int arg) {
	}

	@Override
	protected void bundleChanged(BundleEvent event) {
	}

	@Override
	protected void serviceChanged(ServiceEvent event) {
	}

	/**
	 * 得到默认的数据源
	 * 
	 * @return
	 */
	public static DataSource getDefaultDataSource() {
		return getDataSource("");
	}

	/**
	 * 根据配置名称得到数据源(使用全局JDBC配置初始化器)
	 * 
	 * @param propertyName
	 * @return
	 */
	public static DataSource getDataSource(String propertyName) {
		return getDataSource(propertyName, propertiesInitializer);
	}

	/**
	 * 根据配置名称得到数据源(使用指定的JDBC配置初始化器)
	 * 
	 * @param propertyName
	 * @param jdbcPropertiesInitializer
	 * @return
	 */
	public static DataSource getDataSource(String propertyName,
			PropertiesInitializer jdbcPropertiesInitializer) {
		if (!propertyNameDataSourceMap.containsKey(propertyName)) {
			DataSource dataSource = innerGetDataSource(propertyName,
					jdbcPropertiesInitializer);
			if (dataSource == null) {
				return null;
			}
			propertyNameDataSourceMap.put(propertyName, new DataSourceProxy(
					dataSource));
		}
		return propertyNameDataSourceMap.get(propertyName);
	}

	/**
	 * 得到所有数据源配置名称
	 * 
	 * @return
	 */
	public static String[] getDataSourcePropertyNames() {
		String head = "qwf-db-jdbc.";
		String tril = ".properties";
		String defaultName = (head + tril).replace("..", ".");
		String[] keys = WebContext.getQwfConfigKeys();
		List<String> strList = new ArrayList<String>();
		for (String key : keys) {
			if (key.startsWith(head) && key.endsWith(tril)) {
				if (defaultName.equals(key)) {
					strList.add("");
				} else {
					strList.add(key.substring(head.length(), key.length()
							- tril.length() - 1));
				}
			}
		}
		return strList.toArray(new String[strList.size()]);
	}

	/**
	 * 重新加载默认的数据源s
	 */
	public static void reloadDataSource() {
		reloadDataSource("", null);
	}

	/**
	 * 重新加载指定的数据源
	 * 
	 * @param propertyName
	 */
	public static void reloadDataSource(String propertyName) {
		reloadDataSource(propertyName, null);
	}

	/**
	 * 重新加载指定的数据源
	 * 
	 * @param propertyName
	 * @param jdbcPropertiesInitializer
	 */
	public static void reloadDataSource(String propertyName,
			PropertiesInitializer jdbcPropertiesInitializer) {
		DataSourceProxy dataSourceProxy = propertyNameDataSourceMap
				.get(propertyName);
		if (dataSourceProxy == null) {
			return;
		}
		DataSource newDataSource = innerGetDataSource(propertyName,
				jdbcPropertiesInitializer);
		dataSourceProxy.setTargetDataSource(newDataSource);
	}

	/**
	 * 重新加载所有的数据源
	 */
	public static void reloadAllDataSource() {
		reloadAllDataSource(null);
	}

	/**
	 * 重新加载所有的数据源
	 * 
	 * @param jdbcPropertiesInitializer
	 */
	public static void reloadAllDataSource(
			PropertiesInitializer jdbcPropertiesInitializer) {
		for (String propertyName : propertyNameDataSourceMap.keySet()) {
			reloadDataSource(propertyName, jdbcPropertiesInitializer);
		}
	}

	/**
	 * 移除指定的数据源
	 * 
	 * @param propertyName
	 */
	public static void removeDataSource(String propertyName) {
		DataSource dataSource = propertyNameDataSourceMap.get(propertyName);
		if (dataSource == null) {
			return;
		}
		propertyNameDataSourceMap.remove(propertyName);
	}

	/**
	 * 移除全部的数据源
	 */
	public static void removeAllDataSource() {
		String[] properyNames = propertyNameDataSourceMap.keySet().toArray(
				new String[propertyNameDataSourceMap.size()]);
		for (String properyName : properyNames) {
			removeDataSource(properyName);
		}
	}

	private static Log log = LogFactory.getLog(DataSourceContext.class);

	private static DataSource innerGetDataSource(String propertyName,
			PropertiesInitializer propertiesInitializer) {
		// 得到JDBC配置文件路径
		String configProperty = null;
		if (propertyName == null || propertyName.equals("")) {
			configProperty = "qwf-db-jdbc.properties";
		} else {
			configProperty = "qwf-db-jdbc." + propertyName + ".properties";
		}
		String jdbcPropertyFilePath = WebContext.getQwfConfig(configProperty);
		if (jdbcPropertyFilePath == null || jdbcPropertyFilePath.equals("")) {
			log.warn("在QuickWebFramework配置文件中未找到配置项：" + configProperty);
			return null;
		}
		jdbcPropertyFilePath = WebContext.getRealPath(jdbcPropertyFilePath);
		// 读取JDBC配置文件
		Properties prop = null;
		try {
			InputStream inputStream = new FileInputStream(jdbcPropertyFilePath);
			Reader reader = new InputStreamReader(inputStream, "utf-8");
			prop = PropertiesUtils.load(reader);
			reader.close();
			inputStream.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		// 初始化Datasource
		BasicDataSource basicDataSource = new BasicDataSource();
		// 设置驱动的ClassLoader
		basicDataSource.setDriverClassLoader(WebContext.getWebClassLoader());
		// 如果指定了JDBC配置初始化器，则执行初始化
		if (propertiesInitializer != null) {
			propertiesInitializer.init(prop);
		}
		if (prop.containsKey("jdbc.driver")) {
			basicDataSource.setDriverClassName(prop.getProperty("jdbc.driver"));
		}
		if (prop.containsKey("jdbc.url")) {
			basicDataSource.setUrl(prop.getProperty("jdbc.url"));
		}
		if (prop.containsKey("jdbc.username")) {
			basicDataSource.setUsername(prop.getProperty("jdbc.username"));
		}
		if (prop.containsKey("jdbc.password")) {
			basicDataSource.setPassword(prop.getProperty("jdbc.password"));
		}
		if (prop.containsKey("jdbc.initialSize")) {
			basicDataSource.setInitialSize(Integer.parseInt(prop
					.getProperty("jdbc.initialSize")));
		}
		if (prop.containsKey("jdbc.maxActive")) {
			basicDataSource.setMaxActive(Integer.parseInt(prop
					.getProperty("jdbc.maxActive")));
		}
		if (prop.containsKey("jdbc.maxIdle")) {
			basicDataSource.setMaxIdle(Integer.parseInt(prop
					.getProperty("jdbc.maxIdle")));
		}
		if (prop.containsKey("jdbc.minIdle")) {
			basicDataSource.setMinIdle(Integer.parseInt(prop
					.getProperty("jdbc.minIdle")));
		}
		if (prop.containsKey("jdbc.testOnBorrow")) {
			basicDataSource.setTestOnBorrow(Boolean.parseBoolean(prop
					.getProperty("jdbc.testOnBorrow")));
		}
		if (prop.containsKey("jdbc.timeBetweenEvictionRunsMillis")) {
			basicDataSource
					.setTimeBetweenEvictionRunsMillis(Integer.parseInt(prop
							.getProperty("jdbc.timeBetweenEvictionRunsMillis")));
		}
		return basicDataSource;
	}
}
