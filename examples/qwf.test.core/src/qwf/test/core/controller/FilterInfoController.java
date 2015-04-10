package qwf.test.core.controller;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quickwebframework.framework.WebContext;

@Controller
public class FilterInfoController {
	@RequestMapping(value = "filter", method = RequestMethod.GET)
	public String get_filter(HttpServletRequest request,
			HttpServletResponse response) {
		Filter[] filters = WebContext.getFilters();
		request.setAttribute("filters", filters);
		return "filter";
	}
}
