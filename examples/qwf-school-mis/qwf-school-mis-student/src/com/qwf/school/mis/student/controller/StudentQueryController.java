package com.qwf.school.mis.student.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qwf.school.mis.student.entity.Student;
import com.qwf.school.mis.student.service.StudentService;

@Controller
public class StudentQueryController {
	@Autowired
	private StudentService studentService;

	@RequestMapping(value = "query", method = RequestMethod.GET)
	public String get_query(HttpServletRequest request,
			HttpServletResponse response, String name, Integer pageIndex,
			Integer pageSize) {
		if (pageIndex == null) {
			pageIndex = 1;
		}
		if (pageSize == null) {
			pageSize = 20;
		}
		List<Student> studentList = studentService.queryStudent(name,
				pageIndex, pageSize);
		request.setAttribute("studentList", studentList);
		return "query";
	}
}
