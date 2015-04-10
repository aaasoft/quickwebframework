package com.quickwebframework.viewrender.velocity.service.impl;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.osgi.framework.BundleEvent;

import com.quickwebframework.viewrender.ViewRenderService;
import com.quickwebframework.viewrender.velocity.Activator;
import com.quickwebframework.viewrender.velocity.util.BundleResourceLoader;

public class ViewRenderServiceImpl extends ViewRenderService {

	private VelocityEngine engine;

	public ViewRenderServiceImpl() {
		Properties velocityProp = this.getProperties();
		engine = new VelocityEngine(velocityProp);
		engine.setProperty(Velocity.RESOURCE_LOADER, "class");
		engine.setProperty("class.resource.loader.class",
				BundleResourceLoader.class.getName());
		engine.setProperty(
				"class.resource.loader.class.pluginNameAndPathSplitString",
				this.getPluginNameAndPathSplitString());
		engine.setProperty("class.resource.loader.class.viewNamePrefix",
				this.getViewNamePrefix());
		engine.setProperty("class.resource.loader.class.viewNameSuffix",
				this.getViewNameSuffix());
		engine.init();
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
			final Map<String, Object> model) {
		// 得到模板
		Template template = engine.getTemplate(viewName);
		// 准备数据
		Context context = new Context() {
			public boolean containsKey(Object key) {
				return model.containsKey(key);
			}

			public Object get(String key) {
				return model.get(key);
			}

			public Object[] getKeys() {
				return model.keySet().toArray();
			}

			public Object put(String key, Object value) {
				return model.put(key, value);
			}

			public Object remove(Object key) {
				return model.remove(key);
			}
		};

		// 输出
		try {
			// 设置编码
			response.setCharacterEncoding(template.getEncoding());
			response.setContentType("text/html");
			template.merge(context, response.getWriter());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
