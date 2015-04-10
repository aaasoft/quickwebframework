package com.quickwebframework.db.orm.spring.jdbc;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;
import org.springframework.jdbc.core.JdbcTemplate;

import com.quickwebframework.db.jdbc.DataSourceContext;
import com.quickwebframework.db.orm.spring.jdbc.support.Activator;
import com.quickwebframework.framework.FrameworkContext;

public class JdbcTemplateContext extends FrameworkContext {
	private static JdbcTemplateContext instance;

	public static JdbcTemplateContext getInstance() {
		if (instance == null)
			instance = new JdbcTemplateContext();
		return instance;
	}

	private static Map<String, JdbcTemplate> propertyNameObjectMap = new HashMap<String, JdbcTemplate>();

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
	 * 得到默认的JdbcTemplate对象
	 * 
	 * @return
	 */
	public static JdbcTemplate getDefaultJdbcTemplate() {
		return getJdbcTemplate("");
	}

	/**
	 * 得到指定的JdbcTemplate对象
	 * 
	 * @param propertyName
	 * @return
	 */
	public static JdbcTemplate getJdbcTemplate(String propertyName) {
		if (!propertyNameObjectMap.containsKey(propertyName)) {
			propertyNameObjectMap.put(propertyName,
					innerGetJdbcTemplate(propertyName));
		}
		return propertyNameObjectMap.get(propertyName);
	}

	private static JdbcTemplate innerGetJdbcTemplate(String propertyName) {
		DataSource dataSource = DataSourceContext.getDataSource(propertyName);
		JdbcTemplate template = new JdbcTemplate(dataSource);
		return template;
	}
}
