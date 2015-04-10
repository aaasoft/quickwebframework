package com.quickwebframework.ioc;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.quickwebframework.framework.FrameworkContext;
import com.quickwebframework.ioc.impl.Activator;
import com.quickwebframework.util.BundleContextUtils;

public class IocContext extends FrameworkContext {
	private static IocContext instance;

	public static IocContext getInstance() {
		if (instance == null)
			instance = new IocContext();
		return instance;
	}

	// ======变量部分开始
	private static Log log = LogFactory.getLog(IocContext.class);

	private static IocFrameworkService iocFrameworkService;

	// ======变量部分结束

	public IocContext() {

	}

	@Override
	protected BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	protected void init(int arg) {
		super.addSimpleServiceStaticFieldLink(
				IocFrameworkService.class.getName(), "iocFrameworkService");
	}

	@Override
	protected void destory(int arg) {

	}

	@Override
	protected void bundleChanged(BundleEvent event) {
		Bundle bundle = event.getBundle();
		int bundleEventType = event.getType();
		// 如果是已经停止
		if (bundleEventType == BundleEvent.STOPPED) {
			if (iocFrameworkService == null)
				return;
			unregisterBundle(bundle);
		}
	}

	@Override
	protected void serviceChanged(ServiceEvent event) {

	}

	/**
	 * 注册Bundle到IoC框架中
	 * 
	 * @param bundle
	 */
	public static void registerBundle(Bundle bundle) {
		checkIocFrameworkExist();
		iocFrameworkService.registerBundle(bundle);
	}

	/**
	 * 取消注册Bundle到IoC框架中
	 * 
	 * @param bundle
	 */
	public static void unregisterBundle(Bundle bundle) {
		checkIocFrameworkExist();
		iocFrameworkService.unregisterBundle(bundle);
	}

	/**
	 * 从IoC框架中得到此Bundle对应的应用程序上下文
	 * 
	 * @param bundle
	 * @return
	 */
	@Deprecated
	public static Object getBundleApplicationContext(Bundle bundle) {
		checkIocFrameworkExist();
		return iocFrameworkService.getBundleApplicationContext(bundle);
	}

	private static void checkIocFrameworkExist() {
		if (iocFrameworkService == null) {
			// 再尝试获取一次
			Object obj = BundleContextUtils
					.getServiceObject(Activator.getContext(),
							IocFrameworkService.class.getName());
			if (obj == null) {
				log.error("未发现有注册的IocFrameworkService服务！");
				throw new RuntimeException("未发现有注册的IocFrameworkService服务！");
			}
			iocFrameworkService = (IocFrameworkService) obj;
		}
	}

	/**
	 * 是否已包含对应的Bundle
	 * 
	 * @param bundle
	 * @return
	 */
	public static boolean containsBundle(Bundle bundle) {
		checkIocFrameworkExist();
		return iocFrameworkService.containsBundle(bundle);
	}

	/**
	 * IoC容器中是否包含指定的bean名称
	 * 
	 * @param bundle
	 * @param beanName
	 * @return
	 */
	public static boolean containsBean(Bundle bundle, String beanName) {
		checkIocFrameworkExist();
		return iocFrameworkService.containsBean(bundle, beanName);
	}

	/**
	 * 根据类型得到bean的对象
	 * 
	 * @param bundle
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Bundle bundle, Class<T> clazz) {
		checkIocFrameworkExist();
		return iocFrameworkService.getBean(bundle, clazz);
	}

	/**
	 * 根据名称得到bean的对象
	 * 
	 * @param bundle
	 * @param beanName
	 * @return
	 */
	public static Object getBean(Bundle bundle, String beanName) {
		checkIocFrameworkExist();
		return iocFrameworkService.getBean(bundle, beanName);
	}

	/**
	 * 得到IoC容器中bean定义的数量
	 * 
	 * @param bundle
	 * @return
	 */
	public static int getBeanDefinitionCount(Bundle bundle) {
		checkIocFrameworkExist();
		return iocFrameworkService.getBeanDefinitionCount(bundle);
	}

	/**
	 * 得到IoC容器中所有定义的bean的名称
	 * 
	 * @param bundle
	 * @return
	 */
	public static String[] getBeanDefinitionNames(Bundle bundle) {
		checkIocFrameworkExist();
		return iocFrameworkService.getBeanDefinitionNames(bundle);
	}

	/**
	 * 得到指定类型的所有bean的名称
	 * 
	 * @param bundle
	 * @param clazz
	 * @return
	 */
	public static String[] getBeanNamesForType(Bundle bundle, Class<?> clazz) {
		checkIocFrameworkExist();
		return iocFrameworkService.getBeanNamesForType(bundle, clazz);
	}

	/**
	 * 得到指定类型的所有bean的Map
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> Map<String, T> getBeansOfType(Bundle bundle,
			Class<T> clazz) {
		checkIocFrameworkExist();
		return iocFrameworkService.getBeansOfType(bundle, clazz);
	}

	/**
	 * 得到指定注解类型的所有bean的Map
	 * 
	 * @param bundle
	 * @param annotationClazz
	 * @return
	 */
	public static Map<String, Object> getBeansWithAnnotation(Bundle bundle,
			Class<? extends Annotation> annotationClazz) {
		checkIocFrameworkExist();
		return iocFrameworkService.getBeansWithAnnotation(bundle,
				annotationClazz);
	}
}
