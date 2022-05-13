package com.energent.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.energent.entity.*;

public interface StudentRepository extends JpaRepository<Student, String>{
	
	List<Student> findAllStudentsByAcademiesIn(List<Academy> academies);
	
	@Modifying
	@Query(value = "INSERT INTO academy_student (academy_code, student_fiscal_code) VALUES (?1, ?2)", nativeQuery = true)
	void insertJoin(String academyCode, String studentFiscalCode);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM academy_student where academy_code = ?1 and student_fiscal_code = ?2", nativeQuery = true)
	void deleteJoin(String academyCode, String studentFiscalCode);

}