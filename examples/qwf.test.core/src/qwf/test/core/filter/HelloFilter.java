package qwf.test.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;

import com.quickwebframework.stereotype.FilterSetting;

@Component
public class HelloFilter implements Filter {

	public void destroy() {
		System.out.println("HelloFilter -> Hello1 -> destroy()");
		// new RuntimeException().printStackTrace();
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// System.out.println("HelloFilter -> Hello1");
		arg2.doFilter(arg0, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("HelloFilter -> Hello1 -> init():" + arg0);
	}
}
