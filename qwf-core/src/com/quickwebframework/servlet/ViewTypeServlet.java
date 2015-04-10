package com.quickwebframework.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quickwebframework.framework.WebContext;

public abstract class ViewTypeServlet extends HttpServlet {

	private static final long serialVersionUID = 1764897069126545760L;
	private String viewTypeName;

	/**
	 * 得到视图类型名称
	 * 
	 * @return
	 */
	public String getViewTypeName() {
		return viewTypeName;
	}

	/**
	 * 设置视图类型名称
	 * 
	 * @param viewTypeName
	 */
	public void setViewTypeName(String viewTypeName) {
		this.viewTypeName = viewTypeName;
	}

	public ViewTypeServlet() {
		String bundleName = getBundleName();
		viewTypeName = WebContext.getQwfConfig(bundleName + ".viewTypeName");
	}

	// 注册
	public void register() {
		WebContext.registerViewTypeServlet(this);
	}

	// 取消注册
	public void unregister() {
		WebContext.unregisterViewTypeServlet(this);
	}

	/**
	 * 得到插件名称(不能由Bundle.getSymbolicName()方法得到，
	 * 因为当有项目要把所有qwf的bundle整合到一个bundle中时会出问题)
	 * 
	 * @return
	 */
	public abstract String getBundleName();

	/**
	 * 得到此视图类型Servlet下面的所有URL
	 * 
	 * @return
	 */
	public abstract String[] getUrls();

	/**
	 * HTTP服务
	 */
	@Override
	public abstract void service(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException;
}
