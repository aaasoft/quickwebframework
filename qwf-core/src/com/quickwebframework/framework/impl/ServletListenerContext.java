package com.quickwebframework.framework.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceRegistration;

import com.quickwebframework.bridge.ServletListenerBridge;
import com.quickwebframework.core.Activator;
import com.quickwebframework.framework.FrameworkContext;

public class ServletListenerContext extends FrameworkContext {
	private static Log log = LogFactory.getLog(ServletListenerContext.class);
	private static ServletListenerContext instance;

	public static ServletListenerContext getInstance() {
		if (instance == null)
			instance = new ServletListenerContext();
		return instance;
	}

	// ===== 监听器变量部分开始
	private static List<EventListener> listenerList;
	private static Map<String, List<EventListener>> typeNameListenerListMap;
	private static Map<Bundle, List<EventListener>> bundleListenerListMap;
	// 监听器桥接对象
	private ServiceRegistration<?> servletListenerBridgeServiceRegistration;

	// ===== 监听器变量部分结束

	public ServletListenerContext() {
		listenerList = new ArrayList<EventListener>();
		typeNameListenerListMap = new HashMap<String, List<EventListener>>();
		bundleListenerListMap = new HashMap<Bundle, List<EventListener>>();
	}

	@Override
	protected BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	protected void init(int arg) {
		BundleContext bundleContext = Activator.getContext();
		// 注册监听器桥接对象
		servletListenerBridgeServiceRegistration = bundleContext
				.registerService(ServletListenerBridge.class.getName(),
						new ServletListenerBridge(), null);
	}

	@Override
	protected void destory(int arg) {
		servletListenerBridgeServiceRegistration.unregister();
	}

	@Override
	protected void bundleChanged(BundleEvent event) {
		Bundle bundle = event.getBundle();
		int bundleEventType = event.getType();

		BundleContext bundleContext = Activator.getContext();
		if (bundleContext == null)
			return;
		Bundle coreBundle = bundleContext.getBundle();
		if (bundleEventType == BundleEvent.STOPPING) {
			// 移除插件的监听器
			if (bundle.equals(coreBundle)) {
				ServletListenerContext.unregisterAllListener();
			} else {
				ServletListenerContext.unregisterBundleAllListener(bundle);
			}
		}
	}

	@Override
	protected void serviceChanged(ServiceEvent event) {

	}

	@SuppressWarnings("unchecked")
	private static List<Class<? extends EventListener>> getServletInterfaceList(
			Class<? extends EventListener> clazz) {
		List<Class<? extends EventListener>> rtnList = new ArrayList<Class<? extends EventListener>>();
		if (clazz.getName().startsWith("javax.servlet."))
			rtnList.add(clazz);

		Class<?>[] interfaceClassArray = clazz.getInterfaces();
		for (Class<?> interfaceClass : interfaceClassArray) {
			if (interfaceClass.equals(EventListener.class))
				continue;
			if (EventListener.class.isAssignableFrom(interfaceClass)) {
				rtnList.addAll(getServletInterfaceList((Class<? extends EventListener>) interfaceClass));
			}
		}
		return rtnList;
	}

	private static Class<? extends EventListener> getServletInterface(
			Class<? extends EventListener> clazz) {
		List<Class<? extends EventListener>> rtnList = getServletInterfaceList(clazz);
		if (rtnList == null || rtnList.isEmpty())
			return EventListener.class;
		else
			return rtnList.get(0);
	}

	/**
	 * 添加监听器
	 * 
	 * @param bundle
	 *            监听器所属的Bundle
	 * @param listener
	 *            监听器
	 */
	public static void registerListener(Bundle bundle, EventListener listener) {

		String listenerClassName = listener.getClass().getName();
		// 是否存在同类名实例
		boolean hasSameClassNameObject = false;
		for (EventListener preListener : listenerList) {
			if (preListener.getClass().getName().equals(listenerClassName)) {
				hasSameClassNameObject = true;
				break;
			}
		}
		// 如果存在同类名实例，则抛出异常
		if (hasSameClassNameObject) {

			StringBuilder sb = new StringBuilder();
			sb.append(String.format(
					"警告：将Bundle[%s]的过滤器[类名:%s]加入到FilterContext中时，发现存在多个同类名实例！",
					bundle.getSymbolicName(), listenerClassName));
			sb.append("\n--同类名实例列表如下：");
			synchronized (bundleListenerListMap) {
				for (Bundle tmpBundle : bundleListenerListMap.keySet()) {
					List<EventListener> tmpBundleListenerList = bundleListenerListMap
							.get(tmpBundle);
					for (EventListener tmpListener : tmpBundleListenerList) {
						if (tmpListener.getClass().getName()
								.equals(listenerClassName)) {
							sb.append(String.format(
									"\n  --Bundle[%s],监听器[%s ,类名:%s]",
									tmpBundle.getSymbolicName(),
									tmpListener.toString(), listenerClassName));
						}
					}
				}
			}
			String errorMessage = sb.toString();
			log.warn(errorMessage);
		}

		// 加入到Bundle对应的监听器列表中
		List<EventListener> bundleListenerList = null;
		if (bundleListenerListMap.containsKey(bundle)) {
			bundleListenerList = bundleListenerListMap.get(bundle);
		} else {
			bundleListenerList = new ArrayList<EventListener>();
			bundleListenerListMap.put(bundle, bundleListenerList);
		}
		bundleListenerList.add(listener);

		List<Class<? extends EventListener>> listenerClassList = getServletInterfaceList(listener
				.getClass());
		for (Class<? extends EventListener> listenerClass : listenerClassList) {
			// 加入到所有监听器列表中
			String listenerTypeName = listenerClass.getName();
			List<EventListener> typeListenerList = null;
			if (typeNameListenerListMap.containsKey(listenerTypeName)) {
				typeListenerList = typeNameListenerListMap
						.get(listenerTypeName);
			} else {
				typeListenerList = new ArrayList<EventListener>();
				typeNameListenerListMap.put(listenerTypeName, typeListenerList);
			}
			typeListenerList.add(listener);
			log.debug(String.format("已添加插件[%s]的[%s]类型监听器[%s]！",
					bundle.getSymbolicName(), listenerClass.getName(), listener));
		}
		// 加入到全部监听器对象列表中
		listenerList.add(listener);
	}

	/**
	 * 得到所有监听器列表
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T extends EventListener> List<T> getListenerList(
			Class<T> clazz) {
		String listenerTypeName = getServletInterface(clazz).getName();
		if (!typeNameListenerListMap.containsKey(listenerTypeName))
			return null;
		return (List<T>) typeNameListenerListMap.get(listenerTypeName);
	}

	/**
	 * 得到所有监听器
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends EventListener> T[] getListeners(Class<T> clazz) {
		List<T> list = getListenerList(clazz);
		if (list == null)
			return null;
		return list.toArray((T[]) Array.newInstance(clazz, 0));
	}

	/**
	 * 移除所有监听器
	 */
	public static void unregisterAllListener() {
		for (Bundle bundle : bundleListenerListMap.keySet().toArray(
				new Bundle[0])) {
			unregisterBundleAllListener(bundle);
		}
	}

	/**
	 * 移除某Bundle的所有监听器
	 * 
	 * @param bundle
	 */
	public static void unregisterBundleAllListener(Bundle bundle) {
		if (!bundleListenerListMap.containsKey(bundle))
			return;
		EventListener[] bundleListenerArray = bundleListenerListMap.get(bundle)
				.toArray(new EventListener[0]);

		for (EventListener listener : bundleListenerArray) {
			unregisterListener(bundle, listener);
		}
		bundleListenerListMap.remove(bundle);
	}

	/**
	 * 移除监听器
	 * 
	 * @param listener
	 */
	public static void unregisterListener(Bundle bundle, EventListener listener) {

		// 从Bundle对应的监听器列表中移除
		if (!bundleListenerListMap.containsKey(bundle))
			return;
		List<? extends EventListener> bundleListenerList = bundleListenerListMap
				.get(bundle);
		bundleListenerList.remove(listener);

		List<Class<? extends EventListener>> listenerClassList = getServletInterfaceList(listener
				.getClass());
		for (Class<? extends EventListener> listenerClass : listenerClassList) {
			// 从所有监听器列表中移除
			List<? extends EventListener> listenerList = getListenerList(listenerClass);
			if (listenerList != null)
				listenerList.remove(listener);
			log.debug(String.format("已成功移除插件[%s]的[%s]类型监听器[%s]！",
					bundle.getSymbolicName(), listenerClass.getName(), listener));
		}
		// 从全部监听器对象列表中移除
		listenerList.remove(listener);
	}
}
