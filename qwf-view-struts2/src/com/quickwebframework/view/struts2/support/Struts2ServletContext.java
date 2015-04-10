package com.quickwebframework.view.struts2.support;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;

public class Struts2ServletContext implements ServletContext {

	public static final String BUNDLE_RESOURCE_URL_PREFIX = "/bundle:";
	private ServletContext srcServletContext;
	private Map<String, Object> attributeMap = new HashMap<String, Object>();

	public Struts2ServletContext(ServletContext srcServletContext) {
		this.srcServletContext = srcServletContext;
	}

	public Dynamic addFilter(String arg0, String arg1) {
		return srcServletContext.addFilter(arg0, arg1);
	}

	public Dynamic addFilter(String arg0, Filter arg1) {
		return srcServletContext.addFilter(arg0, arg1);
	}

	public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
		return srcServletContext.addFilter(arg0, arg1);
	}

	public void addListener(String arg0) {
		srcServletContext.addListener(arg0);
	}

	public <T extends EventListener> void addListener(T arg0) {
		srcServletContext.addListener(arg0);
	}

	public void addListener(Class<? extends EventListener> arg0) {
		srcServletContext.addListener(arg0);
	}

	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			String arg1) {
		return srcServletContext.addServlet(arg0, arg1);
	}

	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			Servlet arg1) {
		return srcServletContext.addServlet(arg0, arg1);
	}

	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			Class<? extends Servlet> arg1) {
		return srcServletContext.addServlet(arg0, arg1);
	}

	public <T extends Filter> T createFilter(Class<T> arg0)
			throws ServletException {
		return srcServletContext.createFilter(arg0);
	}

	public <T extends EventListener> T createListener(Class<T> arg0)
			throws ServletException {
		return srcServletContext.createListener(arg0);
	}

	public <T extends Servlet> T createServlet(Class<T> arg0)
			throws ServletException {
		return srcServletContext.createServlet(arg0);
	}

	public void declareRoles(String... arg0) {
		srcServletContext.declareRoles(arg0);
	}

	public Object getAttribute(String arg0) {
		return attributeMap.get(arg0);
	}

	public Enumeration<String> getAttributeNames() {
		return srcServletContext.getAttributeNames();
	}

	public ClassLoader getClassLoader() {
		System.out.println("getClassLoader:");
		return srcServletContext.getClassLoader();
	}

	public ServletContext getContext(String arg0) {
		return srcServletContext.getContext(arg0);
	}

	public String getContextPath() {
		return srcServletContext.getContextPath();
	}

	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return srcServletContext.getDefaultSessionTrackingModes();
	}

	public int getEffectiveMajorVersion() {
		return srcServletContext.getEffectiveMajorVersion();
	}

	public int getEffectiveMinorVersion() {
		return srcServletContext.getEffectiveMinorVersion();
	}

	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return srcServletContext.getEffectiveSessionTrackingModes();
	}

	public FilterRegistration getFilterRegistration(String arg0) {
		return srcServletContext.getFilterRegistration(arg0);
	}

	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return srcServletContext.getFilterRegistrations();
	}

	public String getInitParameter(String arg0) {
		return srcServletContext.getInitParameter(arg0);
	}

	public Enumeration<String> getInitParameterNames() {
		return srcServletContext.getInitParameterNames();
	}

	public JspConfigDescriptor getJspConfigDescriptor() {
		return srcServletContext.getJspConfigDescriptor();
	}

	public int getMajorVersion() {
		return srcServletContext.getMajorVersion();
	}

	public String getMimeType(String arg0) {
		return srcServletContext.getMimeType(arg0);
	}

	public int getMinorVersion() {
		return srcServletContext.getMinorVersion();
	}

	public RequestDispatcher getNamedDispatcher(String arg0) {
		return srcServletContext.getNamedDispatcher(arg0);
	}

	public String getRealPath(String arg0) {
		return srcServletContext.getRealPath(arg0);
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		return srcServletContext.getRequestDispatcher(arg0);
	}

	public URL getResource(String arg0) throws MalformedURLException {
		System.out.println("getResource:" + arg0);
		// 如果是插件中的资源
		if (arg0.startsWith(BUNDLE_RESOURCE_URL_PREFIX)) {
			return new URL(arg0.replace(BUNDLE_RESOURCE_URL_PREFIX, "bundle:/"));
		}
		return srcServletContext.getResource(arg0);
	}

	public InputStream getResourceAsStream(String arg0) {
		System.out.println("getResourceAsStream:" + arg0);
		if (arg0.startsWith(BUNDLE_RESOURCE_URL_PREFIX)) {
			String tmpStr = arg0.substring(BUNDLE_RESOURCE_URL_PREFIX.length());
			while (tmpStr.startsWith("/")) {
				tmpStr = tmpStr.substring(1);
			}
			int spIndex = tmpStr.indexOf("/");
			String bundleId = tmpStr.substring(0, spIndex);
			String resourcePath = tmpStr.substring(spIndex);

			bundleId = StringUtils.split(bundleId, '.')[0];
			Bundle bundle = Activator.getContext().getBundle(
					Long.parseLong(bundleId));
			URL url = bundle.getResource(resourcePath);
			try {
				return url.openStream();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return srcServletContext.getResourceAsStream(arg0);
	}

	public Set<String> getResourcePaths(String arg0) {
		System.out.println("getResourcePaths:" + arg0);
		return srcServletContext.getResourcePaths(arg0);
	}

	public String getServerInfo() {
		return srcServletContext.getServerInfo();
	}

	@SuppressWarnings("deprecation")
	public Servlet getServlet(String arg0) throws ServletException {
		return srcServletContext.getServlet(arg0);
	}

	public String getServletContextName() {
		return srcServletContext.getServletContextName();
	}

	@SuppressWarnings("deprecation")
	public Enumeration<String> getServletNames() {
		return srcServletContext.getServletNames();
	}

	public ServletRegistration getServletRegistration(String arg0) {
		return srcServletContext.getServletRegistration(arg0);
	}

	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return srcServletContext.getServletRegistrations();
	}

	@SuppressWarnings("deprecation")
	public Enumeration<Servlet> getServlets() {
		return srcServletContext.getServlets();
	}

	public SessionCookieConfig getSessionCookieConfig() {
		return srcServletContext.getSessionCookieConfig();
	}

	public void log(String arg0) {
		srcServletContext.log(arg0);
	}

	@SuppressWarnings("deprecation")
	public void log(Exception arg0, String arg1) {
		srcServletContext.log(arg0, arg1);
	}

	public void log(String arg0, Throwable arg1) {
		srcServletContext.log(arg0, arg1);
	}

	public void removeAttribute(String arg0) {
		srcServletContext.removeAttribute(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
		attributeMap.put(arg0, arg1);
	}

	public boolean setInitParameter(String arg0, String arg1) {
		return srcServletContext.setInitParameter(arg0, arg1);
	}

	public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
		srcServletContext.setSessionTrackingModes(arg0);
	}
}
