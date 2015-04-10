package com.quickwebframework.view.struts2.support;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionEventListener;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.DefaultActionProxy;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.UnknownHandlerManager;
import com.opensymphony.xwork2.XWorkMessages;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

public class PluginActionProxy implements ActionProxy, Serializable {

	private static final long serialVersionUID = 3293074152487468527L;

	private static final Logger LOG = LoggerFactory
			.getLogger(DefaultActionProxy.class);

	protected Configuration configuration;
	protected ActionConfig config;
	protected ActionInvocation invocation;
	protected UnknownHandlerManager unknownHandlerManager;
	protected String actionName;
	protected String namespace;
	protected String method;
	protected boolean executeResult;
	protected boolean cleanupContext;

	protected ObjectFactory objectFactory;

	protected ActionEventListener actionEventListener;

	private boolean methodSpecified = true;

	/**
	 * This constructor is private so the builder methods (create*) should be
	 * used to create an DefaultActionProxy.
	 * <p/>
	 * The reason for the builder methods is so that you can use a subclass to
	 * create your own DefaultActionProxy instance (like a RMIActionProxy).
	 */
	protected PluginActionProxy(ActionInvocation inv, String namespace,
			String actionName, String methodName, boolean executeResult,
			boolean cleanupContext) {

		this.invocation = inv;
		this.cleanupContext = cleanupContext;
		if (LOG.isDebugEnabled()) {
			LOG.debug("Creating an DefaultActionProxy for namespace "
					+ namespace + " and action name " + actionName);
		}

		this.actionName = StringEscapeUtils.escapeHtml4(actionName);
		this.namespace = namespace;
		this.executeResult = executeResult;
		this.method = StringEscapeUtils.escapeEcmaScript(StringEscapeUtils
				.escapeHtml4(methodName));
	}

	@Inject
	public void setObjectFactory(ObjectFactory factory) {
		this.objectFactory = factory;
	}

	@Inject
	public void setConfiguration(Configuration config) {
		this.configuration = config;
	}

	@Inject
	public void setUnknownHandler(UnknownHandlerManager unknownHandlerManager) {
		this.unknownHandlerManager = unknownHandlerManager;
	}

	@Inject(required = false)
	public void setActionEventListener(ActionEventListener listener) {
		this.actionEventListener = listener;
	}

	public Object getAction() {
		return invocation.getAction();
	}

	public String getActionName() {
		return actionName;
	}

	public ActionConfig getConfig() {
		return config;
	}

	public void setExecuteResult(boolean executeResult) {
		this.executeResult = executeResult;
	}

	public boolean getExecuteResult() {
		return executeResult;
	}

	public ActionInvocation getInvocation() {
		return invocation;
	}

	public String getNamespace() {
		return namespace;
	}

	public String execute() throws Exception {
		ActionContext nestedContext = ActionContext.getContext();
		ActionContext.setContext(invocation.getInvocationContext());

		String retCode = null;

		String profileKey = "execute: ";
		try {
			UtilTimerStack.push(profileKey);

			retCode = invocation.invoke();
		} finally {
			if (cleanupContext) {
				ActionContext.setContext(nestedContext);
			}
			UtilTimerStack.pop(profileKey);
		}

		return retCode;
	}

	public String getMethod() {
		return method;
	}

	private void resolveMethod() {
		// if the method is set to null, use the one from the configuration
		// if the one from the configuration is also null, use "execute"
		if (StringUtils.isEmpty(this.method)) {
			this.method = config.getMethodName();
			if (StringUtils.isEmpty(this.method)) {
				this.method = ActionConfig.DEFAULT_METHOD;
			}
			methodSpecified = false;
		}
	}

	protected void prepare() {
		String profileKey = "create DefaultActionProxy: ";
		try {
			UtilTimerStack.push(profileKey);
			config = configuration.getRuntimeConfiguration().getActionConfig(
					namespace, actionName);

			if (config == null && unknownHandlerManager.hasUnknownHandlers()) {
				config = unknownHandlerManager.handleUnknownAction(namespace,
						actionName);
			}
			if (config == null) {
				throw new ConfigurationException(getErrorMessage());
			}

			resolveMethod();

			if (!config.isAllowedMethod(method)) {
				throw new ConfigurationException("Invalid method: " + method
						+ " for action " + actionName);
			}

			invocation.init(this);

		} finally {
			UtilTimerStack.pop(profileKey);
		}
	}

	protected String getErrorMessage() {
		if ((namespace != null) && (namespace.trim().length() > 0)) {
			return LocalizedTextUtil
					.findDefaultText(
							XWorkMessages.MISSING_PACKAGE_ACTION_EXCEPTION,
							Locale.getDefault(), new String[] { namespace,
									actionName });
		} else {
			return LocalizedTextUtil.findDefaultText(
					XWorkMessages.MISSING_ACTION_EXCEPTION,
					Locale.getDefault(), new String[] { actionName });
		}
	}

	public boolean isMethodSpecified() {
		return methodSpecified;
	}
}