package com.quickwebframework.mvc.spring.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.springframework.web.servlet.ModelAndView;

import com.quickwebframework.framework.WebContext;
import com.quickwebframework.mvc.spring.SpringMvcContext;
import com.quickwebframework.mvc.spring.support.Activator;
import com.quickwebframework.viewrender.ViewRenderService;
import com.quickwebframework.viewrender.servlet.VrViewTypeServlet;

public class SpringMvcViewTypeServlet extends VrViewTypeServlet {

	private static final long serialVersionUID = -6768041494697468584L;

	@Override
	public String getBundleName() {
		return Activator.BUNDLE_NAME;
	}

	@Override
	public BundleContext getBundleContext() {
		return Activator.getContext();
	}

	@Override
	public String[] getUrls() {
		Map<String, String[]> map = SpringMvcContext
				.getSpringMvcFrameworkService().getBundleUrlsMap();
		List<String> rtnList = new ArrayList<String>();
		for (String[] tmpUrls : map.values()) {
			for (String tmpUrl : tmpUrls) {
				rtnList.add(tmpUrl);
			}
		}
		return rtnList.toArray(new String[rtnList.size()]);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String pluginName = request.getAttribute(WebContext.CONST_PLUGIN_NAME)
				.toString();
		String pathName = request.getAttribute(WebContext.CONST_PATH_NAME)
				.toString();
		// 处理
		ModelAndView mav = SpringMvcContext.getSpringMvcFrameworkService()
				.handle(request, response, pluginName, pathName);

		if (mav != null) {
			// 如果mav中的Model是空的，那么将request中的attribute放到mav的Model中
			if (mav.getModel().isEmpty()) {
				Enumeration<String> atttributeNamesEnumeration = request
						.getAttributeNames();
				while (atttributeNamesEnumeration.hasMoreElements()) {
					String key = atttributeNamesEnumeration.nextElement();
					mav.getModel().put(key, request.getAttribute(key));
				}
			}
			renderView(request, response, pluginName, mav);
			return;
		}
	}

	/**
	 * 渲染视图
	 * 
	 * @return
	 */
	public void renderView(HttpServletRequest request,
			HttpServletResponse response, String pluginName, ModelAndView mav) {
		try {
			ViewRenderService viewRenderService = this.getViewRenderService();
			if (viewRenderService != null) {
				String viewName = mav.getViewName();
				if (!viewName.contains(viewRenderService
						.getPluginNameAndPathSplitString())) {
					viewName = pluginName
							+ viewRenderService
									.getPluginNameAndPathSplitString()
							+ viewName;
				}
				// 渲染视图
				viewRenderService.renderView(request, response, viewName,
						mav.getModel());
			} else {
				response.sendError(500, String.format("[%s]未找到视图渲染器服务!", this
						.getClass().getName()));
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
