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
	
	@ApiOperation(value = "Find all students.")
	@GetMapping("/students")
	public List<Student> findAllStudents() {return studentService.findAllStudents();}
		
	@ApiOperation(value = "Find specifical student by given fiscal code.")
	@GetMapping("/students/studentFiscalCode/{studentFiscalCode}")
	public Student findStudentByFiscalCode(@ApiParam(value = "Student Fiscal Code : representative and univoque code of a student, Primary Key of the entity.<br><p><i><small><b>Will be returned :</b><ul><li>The selected entity.</li></ul></small></i></p>", example = "ABCDFG01H23I456L") @PathVariable String studentFiscalCode) {return studentService.findStudentById(studentFiscalCode);}
	
	@ApiOperation(value = "Finds all the students related to a specified academy.")
	@GetMapping("/students/academies/{academyCode}")
	public List<Student> findStudentsByAcademy(@ApiParam(value = "Academy Code : representative of academy's identificative to search the relation with.<br><p><i><small><b>Will be returned :</b><ul><li>A list filled with all the students related with the selected academy.</li></ul></small></i></p>", example = "AcademyCode1") @PathVariable String academyCode){return studentService.findStudentsByAcademy(academyCode);}
	
	@ApiOperation("Save a student on the DB, if student's fiscal code is alredy taken, will be updated.")
	@PostMapping("/students/addOrUpdate")
	public Student addOrUpdateStudent(@ApiParam(value="A student, representing the entity provided of: <ul><li>Fiscal Code.</li><li>First Name.</li><li>Last Name.</li><li>Birth Date.</li></ul><br><p><i><small><b>Will be returned :</b><ul><li>The saved or updated entity.</li></ul></small></i></p><br><p><i><small><b>Exceptions will be thrown when :</b><ul><li>Fiscal code won't match an authentical fiscal code pattern.</li><li>Birth Date gives back an age lesser than 18.</li></ul></small></i></p>") @Valid @RequestBody Student student) throws Exception {return studentService.addOrUpdateStudent(student);}

	@ApiOperation("Remove a student from the DB.")
	@DeleteMapping("/students/remove/{studentFiscalCode}")
	public Message removeStudent(@ApiParam(value = "Academy code : representative and univoque code of an academy, Primary Key of the entity.<br><p><i><small><b>Will be returned a message containing:</b><ul><li>Operation Succeded/Failed.</li></ul></small></i></p>") @PathVariable String studentFiscalCode) {return studentService.removeStudent(studentFiscalCode);}
	
	@ApiOperation(value = "Add a relation between a student and an academy.")
	@PostMapping("/students/addOnJoin/{academyCode}/studentFiscalCode/{studentFiscalCode}")
	public Message addOnJoinTable(@ApiParam(value = "Academy code : representative and univoque code of an academy, Primary Key of the entity.", example = "AcademyCode1")@PathVariable String academyCode, @ApiParam(value = "Student Fiscal Code : representative and univoque code of a student, Primary Key of the entity.<br><p><i><small><b>Will be returned a message containing:</b><ul><li>Operation Succeded/Failed.</li></ul></small></i></p>", example = "ABCDFG01H23I456L")@PathVariable String studentFiscalCode) {return studentService.addOnJoinTableAcademyStudent(academyCode, studentFiscalCode);}
	
	@ApiOperation(value = "Remove a relation between a student and an academy")
	@DeleteMapping("/students/removeFromJoin/{academyCode}/studentFiscalCode/{studentFiscalCode}")
	public Message removeFromJoinTable(@ApiParam(value = "Academy code : representative and univoque code of an academy, Primary Key of the entity.", example = "AcademyCode1")@PathVariable String academyCode, @ApiParam(value = "Student Fiscal Code : representative and univoque code of a student, Primary Key of the entity.<br><p><i><small><b>Will be returned a message containing:</b><ul><li>Operation Succeded/Failed.</li></ul></small></i></p>", example = "ABCDFG01H23I456L")@PathVariable String studentFiscalCode) throws Exception {return studentService.deleteOnJoinTableAcademyStudent(studentFiscalCode, academyCode);}

}