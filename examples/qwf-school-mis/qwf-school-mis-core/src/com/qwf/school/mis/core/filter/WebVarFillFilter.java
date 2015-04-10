package com.qwf.school.mis.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;

import com.qwf.school.mis.core.util.MisMenuUtils;

@Component
public class WebVarFillFilter implements Filter {

	private ServletContext servletContext;

	public void destroy() {
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		String contextPath = servletContext.getContextPath();
		arg0.setAttribute("contextPath", contextPath);
		arg0.setAttribute("allMenuMap", MisMenuUtils.getAllMenuMap());
		arg2.doFilter(arg0, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException {
		servletContext = arg0.getServletContext();
	}
}
