package qwf.test.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quickwebframework.db.orm.spring.jdbc.JdbcTemplateContext;

@Controller
public class IndexController {

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index_get(HttpServletRequest request,
			HttpServletResponse response) {
//		JdbcTemplate template = JdbcTemplateContext.getDefaultJdbcTemplate();
//		List<?> map = template.queryForList("show databases");
//		System.out.println(map.size());
		return "index";
	}
}
