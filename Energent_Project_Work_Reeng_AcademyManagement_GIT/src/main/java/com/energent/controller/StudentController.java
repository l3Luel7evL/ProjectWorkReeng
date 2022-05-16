package com.energent.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.energent.bean.Message;
import com.energent.entity.Student;
import com.energent.service.StudentService;

@RestController
@RequestMapping("/student-service")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@GetMapping("/students/studentFiscalCode/{studentFiscalCode}")
	public Student findStudentByFiscalCode(@PathVariable String studentFiscalCode) {return studentService.findStudentById(studentFiscalCode);}
	
	@GetMapping("/students")
	public List<Student> findAllStudents() {return studentService.findAllStudents();}
		
	//1---IN AcademyStudentController? nota n°1.
	@GetMapping("/students/academies/{academyCode}")
	public List<Student> findStudentsByAcademy(@PathVariable String academyCode){return studentService.findStudentsByAcademy(academyCode);}
	
	@PostMapping("/students/addOrUpdate")
	public Student addOrUpdateStudent(@Valid @RequestBody Student student) throws Exception {return studentService.addOrUpdateStudent(student);}

	@DeleteMapping("/students/academies/academyCode/{academyCode}/remove/{studentFiscalCode}")
	public Message removeStudent(@PathVariable String academyCode, @PathVariable String studentFiscalCode) {return studentService.removeStudent(studentFiscalCode, academyCode);}
	
	//VD. 1
	@PostMapping("/students/academies/addOnJoin/academyCode/{academyCode}/studentFiscalCode/{studentFiscalCode}")
	public Message addOnJoinTable(@PathVariable String academyCode, @PathVariable String studentFiscalCode) {return studentService.addOnJoinTableAcademyStudent(academyCode, studentFiscalCode);}
	
	//VD. 1
	@DeleteMapping("/students/academies/removeFromJoin/academyCode/{academyCode}/studentFiscalCode/{studentFiscalCode}")
	public Message removeFromJoinTable(@PathVariable String academyCode, @PathVariable String studentFiscalCode) throws Exception {return studentService.deleteOnJoinTableAcademyStudent(studentFiscalCode, academyCode);}
}