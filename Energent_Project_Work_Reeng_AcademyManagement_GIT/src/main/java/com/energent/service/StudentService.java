package com.energent.service;

import java.util.List;

import com.energent.bean.Message;
import com.energent.entity.*;

public interface StudentService {
	
	//Read
	Student findStudentById(String id);
	List<Student> findAllStudents();
	List<Student> findStudentsByAcademies(List<Academy> academies);
	
	//Other CRUDs
	Student addOrUpdateStudent(Student student) throws Exception;
	Message removeStudent(String studentFiscalCode, String academyCode);
	
	//Join table operations
	Message addOnJoinTableAcademyStudent(String academyCode, String studentFiscalCode);
	Message deleteOnJoinTableAcademyStudent(String studentFiscalCode, String academyCode);

}