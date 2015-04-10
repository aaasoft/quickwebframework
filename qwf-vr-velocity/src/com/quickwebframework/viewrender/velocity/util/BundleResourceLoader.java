package com.quickwebframework.viewrender.velocity.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.osgi.framework.Bundle;

import com.quickwebframework.framework.OsgiContext;

public class BundleResourceLoader extends ResourceLoader {
	// 用来缓存已经找到的视图名称与插件
	private Map<String, Bundle> templateBundles = Collections
			.synchronizedMap(new HashMap<String, Bundle>());

	private String pluginNameAndPathSplitString;
	private String viewNameSuffix;
	private String viewNamePrefix;

	@Override
	public long getLastModified(Resource resource) {
		Bundle bundle = templateBundles.get(resource.getName());
		if (bundle == null) {
			return 0;
		} else {
			return bundle.getLastModified();
		}
	}

	@Override
	public InputStream getResourceStream(String name)
			throws ResourceNotFoundException {
		if ("VM_global_library.vm".equals(name)) {
			return null;
		}

		String[] tmpArray = name.split(pluginNameAndPathSplitString);
		if (tmpArray.length < 2) {
			throw new VelocityException("视图名称[" + name + "]不符合规则：“[插件名]"
					+ pluginNameAndPathSplitString + "[路径]”");
		}
		String pluginName = tmpArray[0];
		String path = tmpArray[1];
		// 对视图名称进行处理(添加前后缀)
		path = viewNamePrefix + path + viewNameSuffix;

		Bundle bundle = OsgiContext.getBundleByName(pluginName);

		if (bundle == null) {
			throw new VelocityException(String.format(
					"Can't found plugin[%s],template [%s] load failure.",
					pluginName, name));
		}
		URL url = bundle.getResource(path);
		if (url == null) {
			throw new ResourceNotFoundException(
					"BundleResourceLoader : cannot find " + name);
		}
		InputStream ufs;
		try {
			ufs = url.openStream();
		} catch (IOException ioe) {
			String msg = "Exception while loading Template " + name;
			log.error(msg, ioe);
			throw new VelocityException(msg, ioe);
		}
		templateBundles.put(name, bundle);
		return new BufferedInputStream(ufs);
	}

	@Override
	public void init(ExtendedProperties arg0) {
		this.pluginNameAndPathSplitString = arg0
				.getString("class.pluginNameAndPathSplitString");
		this.viewNamePrefix = arg0.getString("class.viewNamePrefix");
		this.viewNameSuffix = arg0.getString("class.viewNameSuffix");
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		long lastModified = getLastModified(resource);
		return lastModified != resource.getLastModified();
	}

}
