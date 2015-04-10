package com.quickwebframework.view.struts2.servlet.support;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class PluginHttpServletResponse implements HttpServletResponse {

	private HttpServletResponse srcResponse;

	public PluginHttpServletResponse(HttpServletResponse srcResponse) {
		this.srcResponse = srcResponse;
	}

	public void flushBuffer() throws IOException {
		srcResponse.flushBuffer();
	}

	public int getBufferSize() {
		return srcResponse.getBufferSize();
	}

	public String getCharacterEncoding() {
		return srcResponse.getCharacterEncoding();
	}

	public String getContentType() {
		return srcResponse.getContentType();
	}

	public Locale getLocale() {
		return srcResponse.getLocale();
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return srcResponse.getOutputStream();
	}

	public PrintWriter getWriter() throws IOException {
		return srcResponse.getWriter();
	}

	public boolean isCommitted() {
		return srcResponse.isCommitted();
	}

	public void reset() {
		srcResponse.reset();
	}

	public void resetBuffer() {
		srcResponse.resetBuffer();
	}

	public void setBufferSize(int arg0) {
		srcResponse.setBufferSize(arg0);
	}

	public void setCharacterEncoding(String arg0) {
		srcResponse.setCharacterEncoding(arg0);
	}

	public void setContentLength(int arg0) {
		srcResponse.setContentLength(arg0);
	}

	public void setContentType(String arg0) {
		srcResponse.setContentType(arg0);
	}

	public void setLocale(Locale arg0) {
		srcResponse.setLocale(arg0);
	}

	public void addCookie(Cookie arg0) {
		srcResponse.addCookie(arg0);
	}

	public void addDateHeader(String arg0, long arg1) {
		srcResponse.addDateHeader(arg0, arg1);
	}

	public void addHeader(String arg0, String arg1) {
		srcResponse.addHeader(arg0, arg1);
	}

	public void addIntHeader(String arg0, int arg1) {
		srcResponse.addIntHeader(arg0, arg1);
	}

	public boolean containsHeader(String arg0) {
		return srcResponse.containsHeader(arg0);
	}

	public String encodeRedirectURL(String arg0) {
		return srcResponse.encodeRedirectURL(arg0);
	}

	@SuppressWarnings("deprecation")
	public String encodeRedirectUrl(String arg0) {
		return srcResponse.encodeRedirectUrl(arg0);
	}

	public String encodeURL(String arg0) {
		return srcResponse.encodeURL(arg0);
	}

	@SuppressWarnings("deprecation")
	public String encodeUrl(String arg0) {
		return srcResponse.encodeUrl(arg0);
	}

	public String getHeader(String arg0) {
		return srcResponse.getHeader(arg0);
	}

	public Collection<String> getHeaderNames() {
		return srcResponse.getHeaderNames();
	}

	public Collection<String> getHeaders(String arg0) {
		return srcResponse.getHeaders(arg0);
	}

	public int getStatus() {
		return srcResponse.getStatus();
	}

	public void sendError(int arg0) throws IOException {
		srcResponse.sendError(arg0);
	}

	public void sendError(int arg0, String arg1) throws IOException {
		srcResponse.sendError(arg0, arg1);
	}

	public void sendRedirect(String arg0) throws IOException {
		srcResponse.sendRedirect(arg0);
	}

	public void setDateHeader(String arg0, long arg1) {
		srcResponse.setDateHeader(arg0, arg1);
	}

	public void setHeader(String arg0, String arg1) {
		srcResponse.setHeader(arg0, arg1);
	}

	public void setIntHeader(String arg0, int arg1) {
		srcResponse.setIntHeader(arg0, arg1);
	}

	public void setStatus(int arg0) {
		srcResponse.setStatus(arg0);
	}

	@SuppressWarnings("deprecation")
	public void setStatus(int arg0, String arg1) {
		srcResponse.setStatus(arg0, arg1);
	}

}
