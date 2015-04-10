package com.quickwebframework.view.struts2.filter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.ng.ExecuteOperations;
import org.apache.struts2.dispatcher.ng.PrepareOperations;
import org.apache.struts2.dispatcher.ng.filter.FilterHostConfig;

import com.quickwebframework.view.struts2.support.PluginInitOperations;

public class PluginStrutsPrepareAndExecuteFilter implements StrutsStatics,
		Filter {
	protected PrepareOperations prepare;
	protected ExecuteOperations execute;
	protected List<Pattern> excludedPatterns = null;
	protected Dispatcher dispatcher;

	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		PluginInitOperations init = new PluginInitOperations();
		try {
			FilterHostConfig config = new FilterHostConfig(filterConfig);
			init.initLogging(config);
			dispatcher = init.initDispatcher(config);
			init.initStaticContentLoader(config, dispatcher);

			prepare = new PrepareOperations(filterConfig.getServletContext(),
					dispatcher);
			execute = new ExecuteOperations(filterConfig.getServletContext(),
					dispatcher);
			this.excludedPatterns = init.buildExcludedPatternsList(dispatcher);

			postInit(dispatcher, filterConfig);
		} finally {
			init.cleanup();
		}

	}

	/**
	 * Callback for post initialization
	 */
	protected void postInit(Dispatcher dispatcher, FilterConfig filterConfig) {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		try {
			prepare.setEncodingAndLocale(request, response);
			prepare.createActionContext(request, response);
			prepare.assignDispatcherToThread();
			if (excludedPatterns != null
					&& prepare.isUrlExcluded(request, excludedPatterns)) {
				chain.doFilter(request, response);
			} else {
				request = prepare.wrapRequest(request);
				ActionMapping mapping = prepare.findActionMapping(request,
						response, true);
				if (mapping == null) {
					boolean handled = execute.executeStaticResourceRequest(
							request, response);
					if (!handled) {
						chain.doFilter(request, response);
					}
				} else {
					execute.executeAction(request, response, mapping);
				}
			}
		} finally {
			prepare.cleanupRequest(request);
		}
	}

	public void destroy() {
		prepare.cleanupDispatcher();
	}
}