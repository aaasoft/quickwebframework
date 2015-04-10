package qwf.test.core.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpSessionEvent;

import org.springframework.stereotype.Component;

@Component
public class TestListener implements javax.servlet.ServletRequestListener,
		javax.servlet.ServletContextListener,
		javax.servlet.http.HttpSessionListener {

	public void requestDestroyed(ServletRequestEvent arg0) {
		// System.out.println("request销毁:" + arg0.getServletRequest());
	}

	public void requestInitialized(ServletRequestEvent arg0) {
		// System.out.println("request初始化:" + arg0.getServletRequest());
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("context销毁:" + arg0.getServletContext());
	}

	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("context初始化:" + arg0.getServletContext());
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("session创建:" + arg0.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("session销毁:" + arg0.getSession());
	}
}
