package com.quickwebframework.ioc;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.osgi.framework.Bundle;

/**
 * IoC框架服务接口
 * 
 * @author aaa
 * 
 */
public interface IocFrameworkService {

	/**
	 * 注册Bundle到IoC框架中
	 * 
	 * @param bundle
	 */
	public void registerBundle(Bundle bundle);

	/**
	 * 取消注册Bundle到IoC框架中
	 * 
	 * @param bundle
	 */
	public void unregisterBundle(Bundle bundle);

	/**
	 * (已过时)从IoC框架中得到此Bundle对应的应用程序上下文
	 * 
	 * @param bundle
	 * @return
	 */
	@Deprecated
	public Object getBundleApplicationContext(Bundle bundle);

	/**
	 * IoC框架中是否已包含指定的Bundle
	 * 
	 * @param bundle
	 * @return
	 */
	public boolean containsBundle(Bundle bundle);

	/**
	 * IoC容器中是否包含指定的bean名称
	 * 
	 * @param bundle
	 * @param beanName
	 * @return
	 */
	public boolean containsBean(Bundle bundle, String beanName);

	/**
	 * 根据类型得到bean的对象
	 * 
	 * @param bundle
	 * @param clazz
	 * @return
	 */
	public <T> T getBean(Bundle bundle, Class<T> clazz);

	/**
	 * 根据名称得到bean的对象
	 * 
	 * @param bundle
	 * @param beanName
	 * @return
	 */
	public Object getBean(Bundle bundle, String beanName);

	/**
	 * 得到IoC容器中bean定义的数量
	 * 
	 * @param bundle
	 * @return
	 */
	public int getBeanDefinitionCount(Bundle bundle);

	/**
	 * 得到IoC容器中所有定义的bean的名称
	 * 
	 * @param bundle
	 * @return
	 */
	public String[] getBeanDefinitionNames(Bundle bundle);

	/**
	 * 得到指定类型的所有bean的名称
	 * 
	 * @param bundle
	 * @param clazz
	 * @return
	 */
	public String[] getBeanNamesForType(Bundle bundle, Class<?> clazz);

	/**
	 * 得到指定类型的所有bean的Map
	 * 
	 * @param clazz
	 * @return
	 */
	public <T> Map<String, T> getBeansOfType(Bundle bundle, Class<T> clazz);

	/**
	 * 得到指定注解类型的所有bean的Map
	 * 
	 * @param bundle
	 * @param annotationClazz
	 * @return
	 */
	public Map<String, Object> getBeansWithAnnotation(Bundle bundle,
			Class<? extends Annotation> annotationClazz);
}
