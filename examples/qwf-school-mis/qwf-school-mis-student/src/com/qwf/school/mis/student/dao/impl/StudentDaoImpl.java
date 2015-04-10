package com.qwf.school.mis.student.dao.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.osgi.framework.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.qwf.school.mis.student.Activator;
import com.qwf.school.mis.student.dao.StudentDao;
import com.qwf.school.mis.student.entity.Student;

@Component
public class StudentDaoImpl implements StudentDao {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int getStudentCount() {
		return jdbcTemplate.queryForInt("select count(*) from student");
	}

	public Student getStudent(String id) {
		return jdbcTemplate.queryForObject(
				"select * from student where id = ?", Student.class, id);
	}

	public List<Student> queryStudent(String name, int pageIndex, int pageSize) {
		return jdbcTemplate
				.queryForList(
						"select * from ("
								+ "select ROW_NUMBER() OVER() AS rownum,student.* from student where name like ?"
								+ ") as tmp where rownum >=? and rownum<?",
						Student.class, "%" + name + "%", (pageIndex - 1)
								* pageSize, pageIndex * pageSize);
	}

	public void addStudent(Student stu) {
		jdbcTemplate.update("insert into student(id,name) values(?,?)",
				stu.getId(), stu.getName());
	}

	public void updateStudent(Student stu) {
		jdbcTemplate.update("update student set name = ? where id=?",
				stu.getName(), stu.getId());
	}

	public void deleteStudent(Student stu) {
		deleteStudentById(stu.getId());
	}

	public void deleteStudentById(String id) {
		jdbcTemplate.update("delete from student where id=?", id);
	}

	public boolean checkStudentTable() {
		try {
			this.getStudentCount();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public void repairStudentTable() {
		Bundle currentBundle = Activator.getContext().getBundle();
		try {
			URL sqlUrl = currentBundle.getResource("repair_student_tables.sql");
			InputStream input = sqlUrl.openStream();
			String sql = IOUtils.toString(input, "utf-8");
			jdbcTemplate.update(sql);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
