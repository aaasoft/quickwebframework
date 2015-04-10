package com.quickwebframework.framework;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.SynchronousBundleListener;

import com.quickwebframework.core.Activator;
import org.apache.commons.logging.LogFactory;
import com.quickwebframework.util.BundleContextUtils;

public abstract class FrameworkContext {

	private ServiceListener serviceListener;
	private Map<String, Field> serviceFieldMap;
	private BundleListener bundleListener;

	/**
	 * 得到插件上下文
	 * 
	 * @return
	 */
	protected abstract BundleContext getBundleContext();

	/**
	 * 初始化方法(参数只是为了重载，不起作用)
	 */
	protected abstract void init(int arg);

	/**
	 * 销毁时方法(参数只是为了重载，不起作用)
	 */
	protected abstract void destory(int arg);

	/**
	 * 插件改变时执行方法
	 * 
	 * @param event
	 */
	protected abstract void bundleChanged(BundleEvent event);

	/**
	 * 服务改变时执行方法
	 * 
	 * @param event
	 */
	protected abstract void serviceChanged(ServiceEvent event);

	public FrameworkContext() {
		serviceFieldMap = new HashMap<String, Field>();
		serviceListener = new ServiceListener() {

			public void serviceChanged(ServiceEvent event) {
				String changedServiceName = event.getServiceReference()
						.toString();
				for (String serviceName : serviceFieldMap.keySet()) {
					if (changedServiceName.contains(serviceName)) {
						Field field = serviceFieldMap.get(serviceName);
						setServiceObjectToStaticField(serviceName, field);
					}
				}
				serviceChangedWarper(event);
			}
		};
		bundleListener = new SynchronousBundleListener() {

			public void bundleChanged(BundleEvent event) {
				bundleChangedWarper(event);
			}
		};
	}

	private void bundleChangedWarper(BundleEvent event) {
		bundleChanged(event);
	}

	private void serviceChangedWarper(ServiceEvent event) {
		serviceChanged(event);
	}

	public void init() {
		BundleContext bundleContext = this.getBundleContext();
		bundleContext.addServiceListener(serviceListener);
		bundleContext.addBundleListener(bundleListener);
		init(0);
	}

	public void destory() {
		BundleContext bundleContext = this.getBundleContext();
		bundleContext.removeServiceListener(serviceListener);
		bundleContext.removeBundleListener(bundleListener);
		destory(0);
	}

	private void setServiceObjectToStaticField(String serviceName, Field field) {
		field.setAccessible(true);
		try {
			field.set(null, BundleContextUtils.getServiceObject(
					Activator.getContext(), serviceName));
		} catch (Exception ex) {
			LogFactory.getLog(FrameworkContext.class.getName()).error(
					"给绑定OSGi服务的字段赋值时出现异常：" + ex.getMessage(), ex);
		}
	}

	protected void addSimpleServiceStaticFieldLink(String serviceName,
			String fieldName) {
		try {
			Class<?> clazz = this.getClass();
			Field field = clazz.getDeclaredField(fieldName);
			serviceFieldMap.put(serviceName, field);
			setServiceObjectToStaticField(serviceName, field);
		} catch (Exception ex) {
			LogFactory.getLog(FrameworkContext.class.getName()).error(
					"得到类的字段时出错，原因：" + ex.getMessage(), ex);
			ex.printStackTrace();
		}
	}
}
