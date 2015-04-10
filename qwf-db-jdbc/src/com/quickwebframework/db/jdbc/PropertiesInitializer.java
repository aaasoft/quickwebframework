package com.quickwebframework.db.jdbc;

import java.util.Properties;

/**
 * JDBC配置初始化器
 * 
 * @author aaa
 * 
 */
public interface PropertiesInitializer {
	/**
	 * 初始化JDBC配置
	 * 
	 * @param prop
	 *            JDBC配置
	 */
	public void init(Properties prop);
}
