package qwf.test.core.controller;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quickwebframework.framework.WebContext;

@Controller
public class ListenerInfoController {
	@RequestMapping(value = "listener", method = RequestMethod.GET)
	public String get_listener(HttpServletRequest request,
			HttpServletResponse response) {

		@SuppressWarnings("unchecked")
		Class<? extends EventListener>[] listenerClasses = (Class<? extends EventListener>[]) new Class<?>[] {
				ServletContextListener.class, ServletContextListener.class,
				ServletContextAttributeListener.class,
				ServletRequestListener.class,
				ServletRequestAttributeListener.class,
				HttpSessionActivationListener.class,
				HttpSessionActivationListener.class,
				HttpSessionAttributeListener.class,
				HttpSessionBindingListener.class, HttpSessionListener.class };

		Map<String, Object[]> listenersMap = new HashMap<String, Object[]>();
		for (Class<? extends EventListener> listenerClass : listenerClasses) {
			listenersMap.put(listenerClass.getName(),
					WebContext.getListeners(listenerClass));
		}
		request.setAttribute("listenersMap", listenersMap);
		return "listener";
	}
}
