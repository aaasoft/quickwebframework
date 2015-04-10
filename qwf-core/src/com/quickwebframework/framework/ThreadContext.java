package com.quickwebframework.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;

import com.quickwebframework.core.Activator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadContext extends FrameworkContext {
	private static Log log = LogFactory.getLog(ThreadContext.class);
	private static ThreadContext instance;

	protected static ThreadContext getInstance() {
		if (instance == null)
			instance = new ThreadContext();
		return instance;
	}

	// ====== 变量部分开始
	private static List<Thread> threadList;
	private static Map<Bundle, List<Thread>> bundleThreadListMap;

	/**
	 * 得到所有线程
	 * 
	 * @return
	 */
	public static Thread[] getThreads() {
		return threadList.toArray(new Thread[0]);
	}

	// ====== 变量部分结束

	public ThreadContext() {
		threadList = new ArrayList<Thread>();
		bundleThreadListMap = new HashMap<Bundle, List<Thread>>();
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
		int eventType = event.getType();
		Bundle bundle = event.getBundle();
		BundleContext bundleContext = getBundleContext();
		if (bundleContext == null)
			return;
		Bundle coreBundle = bundleContext.getBundle();
		// 如果插件的状态是正在停止或已经停止
		if (eventType == BundleEvent.STOPPED
				|| eventType == BundleEvent.STOPPING) {
			if (bundle.equals(coreBundle)) {
				removeAllThread();
			} else {
				removeBundleAllThread(bundle);
			}
		}
	}

	@Override
	protected void serviceChanged(ServiceEvent event) {

	}

	/**
	 * 移除所有的线程
	 */
	public static void removeAllThread() {
		for (Bundle bundle : bundleThreadListMap.keySet()
				.toArray(new Bundle[0])) {
			removeBundleAllThread(bundle);
		}
	}

	/**
	 * 移除某Bundle所有的线程
	 * 
	 * @param bundle
	 */
	public static void removeBundleAllThread(Bundle bundle) {
		if (!bundleThreadListMap.containsKey(bundle))
			return;
		Thread[] bundleThreadArray = bundleThreadListMap.get(bundle).toArray(
				new Thread[0]);

		for (Thread thread : bundleThreadArray) {
			removeThread(bundle, thread);
		}
		bundleThreadListMap.remove(bundle);
	}

	/**
	 * 移除线程
	 * 
	 * @param bundle
	 * @param thread
	 */
	public static void removeThread(Bundle bundle, Thread thread) {

		// 从Bundle对应的线程列表中移除
		if (!bundleThreadListMap.containsKey(bundle))
			return;
		List<Thread> bundleThreadList = bundleThreadListMap.get(bundle);
		bundleThreadList.remove(thread);

		// 从所有的线程列表中移除
		threadList.remove(thread);

		String bundleName = bundle.getSymbolicName();

		String threadName = String.format(
				"[Thread Id:%s ,Name:%s ,Class:%s ,Hashcode:%s]",
				thread.getId(), thread.getName(), thread.getClass().getName(),
				Integer.toHexString(thread.hashCode()));
		try {
			thread.interrupt();
			log.debug(String.format("已成功向插件[%s]的线程[%s]发送中断命令！", bundleName,
					threadName));
		} catch (Exception ex) {
			log.warn(String.format("向插件[%s]的线程[%s]发送中断命令失败！", bundleName,
					threadName));
			ex.printStackTrace();
		}
	}

	/**
	 * 添加线程
	 * 
	 * @param bundle
	 * @param thread
	 */
	public static void addThread(Bundle bundle, Thread thread) {

		String threadClassName = thread.getClass().getName();
		// 是否存在同类名实例
		boolean hasSameClassNameObject = false;
		for (Thread preThread : threadList) {
			if (preThread.getClass().getName().equals(threadClassName)) {
				hasSameClassNameObject = true;
				break;
			}
		}
		// 如果存在同类名实例，则抛出异常
		if (hasSameClassNameObject) {

			StringBuilder sb = new StringBuilder();
			sb.append(String.format(
					"警告：将Bundle[%s]的线程[类名:%s]加入到ThreadContext中时，发现存在多个同类名实例！",
					bundle.getSymbolicName(), threadClassName));
			sb.append("\n--同类名实例列表如下：");
			synchronized (bundleThreadListMap) {
				for (Bundle tmpBundle : bundleThreadListMap.keySet()) {
					List<Thread> tmpBundleThreadList = bundleThreadListMap
							.get(tmpBundle);
					for (Thread tmpThread : tmpBundleThreadList) {
						if (tmpThread.getClass().getName()
								.equals(threadClassName)) {
							sb.append(String.format(
									"\n  --Bundle[%s],线程[ID:%s ,类名:%s]",
									tmpBundle.getSymbolicName(),
									tmpThread.getId(), threadClassName));
						}
					}
				}
			}
			String errorMessage = sb.toString();
			log.warn(errorMessage);
		}

		// 加入到Bundle对应的线程列表中
		List<Thread> bundleThreadList = null;
		if (bundleThreadListMap.containsKey(bundle)) {
			bundleThreadList = bundleThreadListMap.get(bundle);
		} else {
			bundleThreadList = new ArrayList<Thread>();
			bundleThreadListMap.put(bundle, bundleThreadList);
		}
		bundleThreadList.add(thread);

		// 加入到全部线程列表中
		threadList.add(thread);

		// 启动线程
		try {
			thread.start();
			log.debug(String.format("已成功启动插件[%s]的线程[%s]！",
					bundle.getSymbolicName(), thread));
		} catch (Exception ex) {
			log.warn(String.format("启动插件[%s]的线程[%s]失败！",
					bundle.getSymbolicName(), thread));
		}
	}
}
