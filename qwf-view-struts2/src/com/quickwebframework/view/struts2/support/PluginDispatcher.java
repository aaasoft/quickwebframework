package com.quickwebframework.view.struts2.support;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

public class PluginDispatcher extends Dispatcher {
	private static final Logger LOG = LoggerFactory
			.getLogger(PluginDispatcher.class);
	private ActionProxyFactory actionProxyFactory = new PluginActionProxyFactory();

	public PluginDispatcher(ServletContext servletContext,
			Map<String, String> initParams) {
		super(servletContext, initParams);
	}

	@Override
	public void init() {
		super.init();

		try {
			ConfigurationManager configurationManager;
			configurationManager = (ConfigurationManager) FieldUtils.readField(
					this, "configurationManager", true);
			Configuration config = configurationManager.getConfiguration();
			config.getContainer().inject(actionProxyFactory);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void serviceAction(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			ActionMapping mapping) throws ServletException {

		ValueStackFactory valueStackFactory;
		boolean devMode;
		boolean handleException;

		try {
			valueStackFactory = (ValueStackFactory) FieldUtils.readField(this,
					"valueStackFactory", true);

			devMode = (Boolean) FieldUtils.readField(this, "devMode", true);
			handleException = (Boolean) FieldUtils.readField(this,
					"handleException", true);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		Map<String, Object> extraContext = createContextMap(request, response,
				mapping, context);

		// If there was a previous value stack, then create a new copy and pass
		// it in to be used by the new Action
		ValueStack stack = (ValueStack) request
				.getAttribute(ServletActionContext.STRUTS_VALUESTACK_KEY);
		boolean nullStack = stack == null;
		if (nullStack) {
			ActionContext ctx = ActionContext.getContext();
			if (ctx != null) {
				stack = ctx.getValueStack();
			}
		}
		if (stack != null) {
			extraContext.put(ActionContext.VALUE_STACK,
					valueStackFactory.createValueStack(stack));
		}

		String timerKey = "Handling request from Dispatcher";
		try {
			UtilTimerStack.push(timerKey);
			String namespace = mapping.getNamespace();
			String name = mapping.getName();
			String method = mapping.getMethod();

			ActionProxy proxy = actionProxyFactory.createActionProxy(namespace,
					name, method, extraContext, true, false);

			request.setAttribute(ServletActionContext.STRUTS_VALUESTACK_KEY,
					proxy.getInvocation().getStack());

			// if the ActionMapping says to go straight to a result, do it!
			if (mapping.getResult() != null) {
				Result result = mapping.getResult();
				result.execute(proxy.getInvocation());
			} else {
				proxy.execute();
			}

			// If there was a previous value stack then set it back onto the
			// request
			if (!nullStack) {
				request.setAttribute(
						ServletActionContext.STRUTS_VALUESTACK_KEY, stack);
			}
		} catch (ConfigurationException e) {
			// WW-2874 Only log error if in devMode
			if (devMode) {
				String reqStr = request.getRequestURI();
				if (request.getQueryString() != null) {
					reqStr = reqStr + "?" + request.getQueryString();
				}
				LOG.error("Could not find action or result\n" + reqStr, e);
			} else {
				if (LOG.isWarnEnabled()) {
					LOG.warn("Could not find action or result", e);
				}
			}
			sendError(request, response, context,
					HttpServletResponse.SC_NOT_FOUND, e);
		} catch (Exception e) {
			if (handleException || devMode) {
				sendError(request, response, context,
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
			} else {
				throw new ServletException(e);
			}
		} finally {
			UtilTimerStack.pop(timerKey);
		}
	}
}
