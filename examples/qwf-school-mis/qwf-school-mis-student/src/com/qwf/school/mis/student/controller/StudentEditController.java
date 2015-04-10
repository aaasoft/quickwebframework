package com.qwf.school.mis.student.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qwf.school.mis.student.service.StudentService;

@Controller
public class StudentEditController {
	@Autowired
	private StudentService studentService;

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String get_edit(HttpServletRequest request,
			HttpServletResponse response, String id) {
		return "edit";
	}
}
