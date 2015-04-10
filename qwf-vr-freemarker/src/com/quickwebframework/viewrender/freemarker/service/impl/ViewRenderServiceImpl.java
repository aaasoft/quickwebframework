package com.quickwebframework.viewrender.freemarker.service.impl;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleEvent;

import com.quickwebframework.viewrender.ViewRenderService;
import com.quickwebframework.viewrender.freemarker.Activator;
import com.quickwebframework.viewrender.freemarker.util.PluginTemplateLoader;

import freemarker.template.Configuration;

public class ViewRenderServiceImpl extends ViewRenderService {
	private Configuration configuration;

	public ViewRenderServiceImpl() {
		Properties freeMarkerProp = this.getProperties();
		configuration = new Configuration();
		// 配置Freemarker
		try {
			configuration.setSettings(freeMarkerProp);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		// 配置ViewRender
		PluginTemplateLoader pluginTemplateLoader = new PluginTemplateLoader(
				this);
		configuration.setTemplateLoader(pluginTemplateLoader);
	}

	@Override
	public String getBundleName() {
		return Activator.BUNDLE_NAME;
	}

	@Override
	public void bundleChanged(BundleEvent event) {

	}

	@Override
	public void renderView(HttpServletRequest request,
			HttpServletResponse response, String viewName,
			Map<String, Object> model) {
		try {
			response.setCharacterEncoding(configuration.getDefaultEncoding());
			response.setContentType("text/html");
			configuration.getTemplate(viewName).process(model,
					response.getWriter());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
