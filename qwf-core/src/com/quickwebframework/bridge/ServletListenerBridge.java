package com.quickwebframework.bridge;

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

import com.quickwebframework.framework.WebContext;

public class ServletListenerBridge implements ServletContextListener,
		ServletContextAttributeListener, ServletRequestListener,
		ServletRequestAttributeListener, HttpSessionActivationListener,
		HttpSessionAttributeListener, HttpSessionBindingListener,
		HttpSessionListener {

	// 开始 ServletContextListener

	public void contextInitialized(ServletContextEvent arg0) {
		ServletContextListener[] listeners = WebContext
				.getListeners(ServletContextListener.class);
		if (listeners == null)
			return;
		for (ServletContextListener listener : listeners) {
			listener.contextInitialized(arg0);
		}
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		ServletContextListener[] listeners = WebContext
				.getListeners(ServletContextListener.class);
		if (listeners == null)
			return;
		for (ServletContextListener listener : listeners) {
			listener.contextDestroyed(arg0);
		}
	}

	// 停止 ServletContextListener

	// 开始 ServletContextAttributeListener

	public void attributeAdded(ServletContextAttributeEvent arg0) {
		ServletContextAttributeListener[] listeners = WebContext
				.getListeners(ServletContextAttributeListener.class);
		if (listeners == null)
			return;
		for (ServletContextAttributeListener listener : listeners) {
			listener.attributeAdded(arg0);
		}
	}

	public void attributeRemoved(ServletContextAttributeEvent arg0) {
		ServletContextAttributeListener[] listeners = WebContext
				.getListeners(ServletContextAttributeListener.class);
		if (listeners == null)
			return;
		for (ServletContextAttributeListener listener : listeners) {
			listener.attributeRemoved(arg0);
		}
	}

	public void attributeReplaced(ServletContextAttributeEvent arg0) {
		ServletContextAttributeListener[] listeners = WebContext
				.getListeners(ServletContextAttributeListener.class);
		if (listeners == null)
			return;
		for (ServletContextAttributeListener listener : listeners) {
			listener.attributeReplaced(arg0);
		}
	}

	// 结束 ServletContextAttributeListener

	// 开始 ServletRequestListener

	public void requestDestroyed(ServletRequestEvent arg0) {
		ServletRequestListener[] listeners = WebContext
				.getListeners(ServletRequestListener.class);
		if (listeners == null)
			return;
		for (ServletRequestListener listener : listeners) {
			listener.requestDestroyed(arg0);
		}
	}

	public void requestInitialized(ServletRequestEvent arg0) {
		ServletRequestListener[] listeners = WebContext
				.getListeners(ServletRequestListener.class);
		if (listeners == null)
			return;
		for (ServletRequestListener listener : listeners) {
			listener.requestInitialized(arg0);
		}
	}

	// 结束 ServletRequestListener

	// 开始 ServletRequestAttributeListener

	public void attributeAdded(ServletRequestAttributeEvent arg0) {
		ServletRequestAttributeListener[] listeners = WebContext
				.getListeners(ServletRequestAttributeListener.class);
		if (listeners == null)
			return;
		for (ServletRequestAttributeListener listener : listeners) {
			listener.attributeAdded(arg0);
		}
	}

	public void attributeRemoved(ServletRequestAttributeEvent arg0) {
		ServletRequestAttributeListener[] listeners = WebContext
				.getListeners(ServletRequestAttributeListener.class);
		if (listeners == null)
			return;
		for (ServletRequestAttributeListener listener : listeners) {
			listener.attributeRemoved(arg0);
		}
	}

	public void attributeReplaced(ServletRequestAttributeEvent arg0) {
		ServletRequestAttributeListener[] listeners = WebContext
				.getListeners(ServletRequestAttributeListener.class);
		if (listeners == null)
			return;
		for (ServletRequestAttributeListener listener : listeners) {
			listener.attributeReplaced(arg0);
		}
	}

	// 结束 ServletRequestAttributeListener

	// 开始 HttpSessionActivationListener

	public void sessionDidActivate(HttpSessionEvent arg0) {
		HttpSessionActivationListener[] listeners = WebContext
				.getListeners(HttpSessionActivationListener.class);
		if (listeners == null)
			return;
		for (HttpSessionActivationListener listener : listeners) {
			listener.sessionDidActivate(arg0);
		}
	}

	public void sessionWillPassivate(HttpSessionEvent arg0) {
		HttpSessionActivationListener[] listeners = WebContext
				.getListeners(HttpSessionActivationListener.class);
		if (listeners == null)
			return;
		for (HttpSessionActivationListener listener : listeners) {
			listener.sessionWillPassivate(arg0);
		}
	}

	// 结束 HttpSessionActivationListener

	// 开始 HttpSessionAttributeListener

	public void attributeAdded(HttpSessionBindingEvent arg0) {
		HttpSessionAttributeListener[] listeners = WebContext
				.getListeners(HttpSessionAttributeListener.class);
		if (listeners == null)
			return;
		for (HttpSessionAttributeListener listener : listeners) {
			listener.attributeAdded(arg0);
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		HttpSessionAttributeListener[] listeners = WebContext
				.getListeners(HttpSessionAttributeListener.class);
		if (listeners == null)
			return;
		for (HttpSessionAttributeListener listener : listeners) {
			listener.attributeRemoved(arg0);
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		HttpSessionAttributeListener[] listeners = WebContext
				.getListeners(HttpSessionAttributeListener.class);
		if (listeners == null)
			return;
		for (HttpSessionAttributeListener listener : listeners) {
			listener.attributeReplaced(arg0);
		}
	}

	// 结束 HttpSessionAttributeListener

	// 开始 HttpSessionBindingListener

	public void valueBound(HttpSessionBindingEvent arg0) {
		HttpSessionBindingListener[] listeners = WebContext
				.getListeners(HttpSessionBindingListener.class);
		if (listeners == null)
			return;
		for (HttpSessionBindingListener listener : listeners) {
			listener.valueBound(arg0);
		}
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) {
		HttpSessionBindingListener[] listeners = WebContext
				.getListeners(HttpSessionBindingListener.class);
		if (listeners == null)
			return;
		for (HttpSessionBindingListener listener : listeners) {
			listener.valueUnbound(arg0);
		}
	}

	// 结束 HttpSessionBindingListener

	// 开始 HttpSessionListener

	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSessionListener[] listeners = WebContext
				.getListeners(HttpSessionListener.class);
		if (listeners == null)
			return;
		for (HttpSessionListener listener : listeners) {
			listener.sessionCreated(arg0);
		}
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSessionListener[] listeners = WebContext
				.getListeners(HttpSessionListener.class);
		if (listeners == null)
			return;
		for (HttpSessionListener listener : listeners) {
			listener.sessionDestroyed(arg0);
		}
	}
	// 结束 HttpSessionListener
}