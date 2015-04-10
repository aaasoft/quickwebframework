package qwf.test.core.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ThreadInfoController {

	@RequestMapping(value = "thread", method = RequestMethod.GET)
	public String get_thread(HttpServletRequest request,
			HttpServletResponse response) {
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		Map<String, StackTraceElement[]> allStackTraces = new HashMap<String, StackTraceElement[]>();
		for (Thread thread : map.keySet()) {
			allStackTraces.put(thread.toString(), map.get(thread));
		}

		request.setAttribute("allStackTraces", allStackTraces);
		return "thread";
	}
}
