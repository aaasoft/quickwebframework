package com.qwf.school.mis.student.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qwf.school.mis.student.dao.StudentDao;
import com.qwf.school.mis.student.entity.Student;
import com.qwf.school.mis.student.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentDao studentDao;

	public int getStudentCount() {
		return studentDao.getStudentCount();
	}

	public Student getStudent(String id) {
		return studentDao.getStudent(id);
	}

	public List<Student> queryStudent(String name, int pageIndex, int pageSize) {
		return studentDao.queryStudent(name, pageIndex, pageSize);
	}

	public void addStudent(Student stu) {
		studentDao.addStudent(stu);
	}

	public void updateStudent(Student stu) {
		studentDao.updateStudent(stu);
	}

	public void deleteStudent(Student stu) {
		studentDao.deleteStudent(stu);
	}

	public void deleteStudentById(String id) {
		studentDao.deleteStudentById(id);
	}

	public boolean checkStudentTables() {
		return studentDao.checkStudentTable();
	}

	public void repairStudentTables() {
		studentDao.repairStudentTable();
	}
}
