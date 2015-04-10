package com.quickwebframework.web.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class QuickWebFrameworkLoaderListener extends QuickWebFrameworkFactory
		implements ServletContextListener, ServletContextAttributeListener,
		ServletRequestListener, ServletRequestAttributeListener,
		HttpSessionActivationListener, HttpSessionAttributeListener,
		HttpSessionBindingListener, HttpSessionListener {

	// 开始 ServletContextListener

	public void contextInitialized(ServletContextEvent arg0) {
		// 启动OSGi框架
		startOSGiFreamwork(arg0.getServletContext());

		if (getServletListenerBridgeObject() != null)
			((ServletContextListener) getServletListenerBridgeObject())
					.contextInitialized(arg0);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletContextListener) getServletListenerBridgeObject())
					.contextDestroyed(arg0);

		// 停止OSGi框架
		stopOSGiFramework();
	}

	// 停止 ServletContextListener

	// 开始 ServletContextAttributeListener

	public void attributeAdded(ServletContextAttributeEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletContextAttributeListener) getServletListenerBridgeObject())
					.attributeAdded(arg0);
	}

	public void attributeRemoved(ServletContextAttributeEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletContextAttributeListener) getServletListenerBridgeObject())
					.attributeRemoved(arg0);
	}

	public void attributeReplaced(ServletContextAttributeEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletContextAttributeListener) getServletListenerBridgeObject())
					.attributeReplaced(arg0);
	}

	// 结束 ServletContextAttributeListener

	// 开始 ServletRequestListener

	public void requestDestroyed(ServletRequestEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletRequestListener) getServletListenerBridgeObject())
					.requestDestroyed(arg0);
	}

	public void requestInitialized(ServletRequestEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletRequestListener) getServletListenerBridgeObject())
					.requestInitialized(arg0);
	}

	// 结束 ServletRequestListener

	// 开始 ServletRequestAttributeListener

	public void attributeAdded(ServletRequestAttributeEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletRequestAttributeListener) getServletListenerBridgeObject())
					.attributeAdded(arg0);
	}

	public void attributeRemoved(ServletRequestAttributeEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletRequestAttributeListener) getServletListenerBridgeObject())
					.attributeRemoved(arg0);
	}

	public void attributeReplaced(ServletRequestAttributeEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((ServletRequestAttributeListener) getServletListenerBridgeObject())
					.attributeReplaced(arg0);
	}

	// 结束 ServletRequestAttributeListener

	// 开始 HttpSessionActivationListener

	public void sessionDidActivate(HttpSessionEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionActivationListener) getServletListenerBridgeObject())
					.sessionDidActivate(arg0);
	}

	public void sessionWillPassivate(HttpSessionEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionActivationListener) getServletListenerBridgeObject())
					.sessionWillPassivate(arg0);
	}

	// 结束 HttpSessionActivationListener

	// 开始 HttpSessionAttributeListener

	public void attributeAdded(HttpSessionBindingEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionAttributeListener) getServletListenerBridgeObject())
					.attributeAdded(arg0);
	}

	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionAttributeListener) getServletListenerBridgeObject())
					.attributeRemoved(arg0);
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionAttributeListener) getServletListenerBridgeObject())
					.attributeReplaced(arg0);
	}

	// 结束 HttpSessionAttributeListener

	// 开始 HttpSessionBindingListener

	public void valueBound(HttpSessionBindingEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionBindingListener) getServletListenerBridgeObject())
					.valueBound(arg0);
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionBindingListener) getServletListenerBridgeObject())
					.valueUnbound(arg0);
	}

	// 结束 HttpSessionBindingListener

	// 开始 HttpSessionListener

	public void sessionCreated(HttpSessionEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionListener) getServletListenerBridgeObject())
					.sessionCreated(arg0);
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		if (getServletListenerBridgeObject() != null)
			((HttpSessionListener) getServletListenerBridgeObject())
					.sessionDestroyed(arg0);
	}
	// 结束 HttpSessionListener
}
