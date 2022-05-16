package com.energent.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping("/student-service")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@ApiOperation(value = "Returns a student by given fiscal code.")
	@GetMapping("/students/studentFiscalCode/{studentFiscalCode}")
	public Student findStudentByFiscalCode(@ApiParam(value = "Univoque code representing entity's identificative string.", example = "ABCDFG01H23I456L") @PathVariable String studentFiscalCode) {return studentService.findStudentById(studentFiscalCode);}
	
	@ApiOperation(value = "Return a list with all the students registered.")
	@GetMapping("/students")
	public List<Student> findAllStudents() {return studentService.findAllStudents();}
		
	@ApiOperation(value = "Return a list with all the students related to a specified academy.")
	@GetMapping("/students/academies/{academyCode}")
	public List<Student> findStudentsByAcademy(@ApiParam(value = "Academy Code : representative of academy's identificative to search the relation with.") @PathVariable String academyCode){return studentService.findStudentsByAcademy(academyCode);}
	
	@ApiOperation("Add a student to the DataBase, if student's fiscal code is alredy taken the student will be updated.")
	@PostMapping("/students/addOrUpdate")
	public Student addOrUpdateStudent(@ApiParam(value="A student, representing the entity provided of: <ul><li>Fiscal Code</li><li>First Name</li><li>Last Name</li><li>Birth Date</li></ul>") @Valid @RequestBody Student student) throws Exception {return studentService.addOrUpdateStudent(student);}

	@ApiOperation("Remove a student from the DataBase.")
//	@DeleteMapping("/students/academies/academyCode/{academyCode}/remove/{studentFiscalCode}")
	@DeleteMapping("/students/remove/{studentFiscalCode}")
	public Message removeStudent(@PathVariable String academyCode, @PathVariable String studentFiscalCode) {return studentService.removeStudent(studentFiscalCode/*, academyCode*/);}
	
	@ApiOperation(value = "Adds a relation between a student and an academy.")
	@PostMapping("/students/academies/addOnJoin/academyCode/{academyCode}/studentFiscalCode/{studentFiscalCode}")
	public Message addOnJoinTable(@PathVariable String academyCode, @PathVariable String studentFiscalCode) {return studentService.addOnJoinTableAcademyStudent(academyCode, studentFiscalCode);}
	
	@ApiOperation(value = "Remove the relation between a student and an academy")
	@DeleteMapping("/students/academies/removeFromJoin/academyCode/{academyCode}/studentFiscalCode/{studentFiscalCode}")
	public Message removeFromJoinTable(@PathVariable String academyCode, @PathVariable String studentFiscalCode) throws Exception {return studentService.deleteOnJoinTableAcademyStudent(studentFiscalCode, academyCode);}

}