package com.quickwebframework.view.struts2.servlet.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.quickwebframework.viewrender.ViewRenderService;

public class PluginHttpServletRequest implements HttpServletRequest {
	private static Log log = LogFactory.getLog(PluginHttpServletRequest.class);
	private HttpServletRequest srcRequest;
	private String pluginName;
	private String urlPrefix;
	private ViewRenderService viewRenderService;

	public PluginHttpServletRequest(HttpServletRequest srcRequest,
			String viewTypeName, String pluginName,
			ViewRenderService viewRenderService) {

		this.srcRequest = srcRequest;
		this.pluginName = pluginName;
		urlPrefix = "/" + pluginName + "/" + viewTypeName;
		this.viewRenderService = viewRenderService;
	}

	public AsyncContext getAsyncContext() {
		return srcRequest.getAsyncContext();
	}

	public Object getAttribute(String arg0) {
		return srcRequest.getAttribute(arg0);
	}

	public Enumeration<String> getAttributeNames() {
		return srcRequest.getAttributeNames();
	}

	public String getCharacterEncoding() {
		return srcRequest.getCharacterEncoding();
	}

	public int getContentLength() {
		return srcRequest.getContentLength();
	}

	public String getContentType() {
		return srcRequest.getContentType();
	}

	public DispatcherType getDispatcherType() {
		return srcRequest.getDispatcherType();
	}

	public ServletInputStream getInputStream() throws IOException {
		return srcRequest.getInputStream();
	}

	public String getLocalAddr() {
		return srcRequest.getLocalAddr();
	}

	public String getLocalName() {
		return srcRequest.getLocalName();
	}

	public int getLocalPort() {
		return srcRequest.getLocalPort();
	}

	public Locale getLocale() {
		return srcRequest.getLocale();
	}

	public Enumeration<Locale> getLocales() {
		return srcRequest.getLocales();
	}

	public String getParameter(String arg0) {
		return srcRequest.getParameter(arg0);
	}

	public Map<String, String[]> getParameterMap() {
		return srcRequest.getParameterMap();
	}

	public Enumeration<String> getParameterNames() {
		return srcRequest.getParameterNames();
	}

	public String[] getParameterValues(String arg0) {
		return srcRequest.getParameterValues(arg0);
	}

	public String getProtocol() {
		return srcRequest.getProtocol();
	}

	public BufferedReader getReader() throws IOException {
		return srcRequest.getReader();
	}

	@SuppressWarnings("deprecation")
	public String getRealPath(String arg0) {
		return srcRequest.getRealPath(arg0);
	}

	public String getRemoteAddr() {
		return srcRequest.getRemoteAddr();
	}

	public String getRemoteHost() {
		return srcRequest.getRemoteAddr();
	}

	public int getRemotePort() {
		return srcRequest.getRemotePort();
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		log.debug("getRequestDispatcher->" + arg0);
		if (arg0.startsWith(urlPrefix)) {
			arg0 = arg0.substring(urlPrefix.length());
		}
		String viewName = pluginName
				+ viewRenderService.getPluginNameAndPathSplitString() + arg0;
		log.debug("getRequestDispatcher->viewName->" + viewName);
		return new PluginRequestDispatcher(viewName, viewRenderService);
	}

	public String getScheme() {
		return srcRequest.getScheme();
	}

	public String getServerName() {
		return srcRequest.getServerName();
	}

	public int getServerPort() {
		return srcRequest.getServerPort();
	}

	public ServletContext getServletContext() {
		return srcRequest.getServletContext();
	}

	public boolean isAsyncStarted() {
		return srcRequest.isAsyncStarted();
	}

	public boolean isAsyncSupported() {
		return srcRequest.isAsyncSupported();
	}

	public boolean isSecure() {
		return srcRequest.isSecure();
	}

	public void removeAttribute(String arg0) {
		srcRequest.removeAttribute(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
		srcRequest.setAttribute(arg0, arg1);
	}

	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		srcRequest.setCharacterEncoding(arg0);
	}

	public AsyncContext startAsync() throws IllegalStateException {
		return srcRequest.startAsync();
	}

	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1)
			throws IllegalStateException {
		return srcRequest.startAsync(arg0, arg1);
	}

	public boolean authenticate(HttpServletResponse arg0) throws IOException,
			ServletException {
		return srcRequest.authenticate(arg0);
	}

	public String getAuthType() {
		return srcRequest.getAuthType();
	}

	public String getContextPath() {
		return srcRequest.getContextPath();
	}

	public Cookie[] getCookies() {
		return srcRequest.getCookies();
	}

	public long getDateHeader(String arg0) {
		return srcRequest.getDateHeader(arg0);
	}

	public String getHeader(String arg0) {
		return srcRequest.getHeader(arg0);
	}

	public Enumeration<String> getHeaderNames() {
		return srcRequest.getHeaderNames();
	}

	public Enumeration<String> getHeaders(String arg0) {
		return srcRequest.getHeaders(arg0);
	}

	public int getIntHeader(String arg0) {
		return srcRequest.getIntHeader(arg0);
	}

	public String getMethod() {
		return srcRequest.getMethod();
	}

	public Part getPart(String arg0) throws IOException, ServletException {
		return srcRequest.getPart(arg0);
	}

	public Collection<Part> getParts() throws IOException, ServletException {
		return srcRequest.getParts();
	}

	public String getPathInfo() {
		return srcRequest.getPathInfo();
	}

	public String getPathTranslated() {
		return srcRequest.getPathTranslated();
	}

	public String getQueryString() {
		return srcRequest.getQueryString();
	}

	public String getRemoteUser() {
		return srcRequest.getRemoteUser();
	}

	public String getRequestURI() {
		return srcRequest.getRequestURI();
	}

	public StringBuffer getRequestURL() {
		return srcRequest.getRequestURL();
	}

	public String getRequestedSessionId() {
		return srcRequest.getRequestedSessionId();
	}

	public String getServletPath() {
		return srcRequest.getServletPath();
	}

	public HttpSession getSession() {
		return srcRequest.getSession();
	}

	public HttpSession getSession(boolean arg0) {
		return srcRequest.getSession(arg0);
	}

	public Principal getUserPrincipal() {
		return srcRequest.getUserPrincipal();
	}

	public boolean isRequestedSessionIdFromCookie() {
		return srcRequest.isRequestedSessionIdFromCookie();
	}

	public boolean isRequestedSessionIdFromURL() {
		return srcRequest.isRequestedSessionIdFromURL();
	}

	@SuppressWarnings("deprecation")
	public boolean isRequestedSessionIdFromUrl() {
		return srcRequest.isRequestedSessionIdFromUrl();
	}

	public boolean isRequestedSessionIdValid() {
		return srcRequest.isRequestedSessionIdValid();
	}

	public boolean isUserInRole(String arg0) {
		return srcRequest.isUserInRole(arg0);
	}

	public void login(String arg0, String arg1) throws ServletException {
		srcRequest.login(arg0, arg1);
	}

	public void logout() throws ServletException {
		srcRequest.logout();
	}

}
