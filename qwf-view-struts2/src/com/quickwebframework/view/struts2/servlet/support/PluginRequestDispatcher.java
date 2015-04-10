package com.quickwebframework.view.struts2.servlet.support;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickwebframework.viewrender.ViewRenderService;

public class PluginRequestDispatcher implements RequestDispatcher {

	private String viewName;
	private ViewRenderService viewRenderService;

	public PluginRequestDispatcher(String viewName,
			ViewRenderService viewRenderService) {
		this.viewName = viewName;
		this.viewRenderService = viewRenderService;
	}

	public void forward(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		include(arg0, arg1);
		arg1.flushBuffer();
	}

	public void include(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		if (viewRenderService == null) {
			response.sendError(500,
					String.format("[%s]未找到视图渲染器服务!", this.getClass().getName()));
			return;
		}
		viewRenderService.renderView(request, response, viewName, null);
	}
}
