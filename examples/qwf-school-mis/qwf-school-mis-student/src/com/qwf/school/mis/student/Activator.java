package com.qwf.school.mis.student;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.quickwebframework.ioc.IocContext;
import com.quickwebframework.framework.WebContext;
import com.qwf.school.mis.core.util.MisMenuUtils;
import com.qwf.school.mis.student.service.StudentService;

public class Activator implements BundleActivator {

	private static Log log = LogFactory.getLog(Activator.class);
	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// 从IoC容器中得到StudentService对象
		StudentService studentService = IocContext.getBean(
				bundleContext.getBundle(), StudentService.class);
		// 如果数据表检查有问题，则修复相关数据表
		if (!studentService.checkStudentTables()) {
			studentService.repairStudentTables();
			log.info("已修复学生相关数据表！");
		}
		Map<String, String> subMenuMap = new HashMap<String, String>();
		String contextPath = WebContext.getServletContext().getContextPath();
		subMenuMap.put("查询", contextPath
				+ "/qwf-school-mis-student/spring/query");
		subMenuMap.put("新增", contextPath
				+ "/qwf-school-mis-student/spring/edit");
		MisMenuUtils.registerMenu("学生信息", subMenuMap);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
