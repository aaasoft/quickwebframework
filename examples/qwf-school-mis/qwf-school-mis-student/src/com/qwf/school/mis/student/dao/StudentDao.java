package com.qwf.school.mis.student.dao;

import java.util.List;

import com.qwf.school.mis.student.entity.Student;

public interface StudentDao {

	/**
	 * 得到所有学生数量
	 */
	public int getStudentCount();

	/**
	 * 根据学号得到学生
	 * 
	 * @param id
	 * @return
	 */
	public Student getStudent(String id);

	/**
	 * 查询学生
	 * 
	 * @param name
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<Student> queryStudent(String name, int pageIndex, int pageSize);

	/**
	 * 增加一个学生
	 * 
	 * @param stu
	 */
	public void addStudent(Student stu);

	/**
	 * 更新一个学生的信息
	 * 
	 * @param stu
	 */
	public void updateStudent(Student stu);

	/**
	 * 删除一个学生
	 * 
	 * @param stu
	 */
	public void deleteStudent(Student stu);

	/**
	 * 根据学号删除学生
	 * 
	 * @param id
	 */
	public void deleteStudentById(String id);

	/**
	 * 检查学生表是否正常
	 * 
	 * @return
	 */
	public boolean checkStudentTable();

	/**
	 * 修复学生相关表
	 */
	public void repairStudentTable();
}
