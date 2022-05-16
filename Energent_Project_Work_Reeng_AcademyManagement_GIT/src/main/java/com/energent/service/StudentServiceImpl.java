package com.energent.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.energent.bean.Message;
import com.energent.entity.*;
import com.energent.repository.*;


@Service
@Transactional
public class StudentServiceImpl implements StudentService{
	
	//private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Message msg;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private AcademyService academyService;

	@Override
	public Student findStudentById(String id) {return studentRepository.findById(id).get();}

	@Override
	public List<Student> findAllStudents() {return studentRepository.findAll();}

	@Override
	public List<Student> findStudentsByAcademy(String academyCode) {
		List<Academy> academies = new ArrayList<>();
		
		if(academyService.findAcademyById(academyCode) != null)
			academies.add(academyService.findAcademyById(academyCode));
		
		return studentRepository.findAllStudentsByAcademiesIn(academies);
	}

	@Override
	public Student addOrUpdateStudent(Student student) throws Exception {
		
		String id = student.getFiscalCode();
		Student studentToSave = null, studentToUpdate = null;
		
		if (ageCalculator(student) < 18) 
			throw new Exception("Età inferiore ai 18 anni, impossibile completare la registrazione!");
		
		if(!studentRepository.existsById(id))
			studentToSave = studentRepository.save(student);
		
		else {
			studentToUpdate = findStudentById(id);
			
			studentToUpdate.setFirstName(student.getFirstName());
			studentToUpdate.setLastName(student.getLastName());
			studentToUpdate.setBirthDate(student.getBirthDate());
			studentToUpdate.getAcademies().addAll(student.getAcademies());
			
			studentToSave = studentRepository.save(studentToUpdate);
		}
		
		return studentToSave;
	}

	@Override
	public Message removeStudent(String studentFiscalCode, String academyCode) {
		
		Academy academy = academyService.findAcademyById(academyCode);
		Student student = findStudentById(studentFiscalCode);
		
		List<Student> students = academy.getStudents();
		List<Academy> academies = student.getAcademies();
		
		if(student.getAcademies()!= null && academy.getStudents()!= null) {
			academies.remove(academy);
			students.remove(student);
			
			academy.getStudents().clear();
			student.getAcademies().clear();
		}
		
		studentRepository.deleteById(studentFiscalCode);
		
		academy.getStudents().addAll(students);
		student.getAcademies().addAll(academies);	
		
		return msg = studentRepository.existsById(studentFiscalCode)? new Message("Operation Failed") : new Message("Operation Succeded");
	}

	@Override
	public Message addOnJoinTableAcademyStudent(String academyCode, String studentFiscalCode) {

		/*Academy academy = academyService.findAcademyById(academyCode);
		Student student = findStudentById(studentFiscalCode);
		
		if(student.getAcademies().contains(academy))
			student.getAcademies().remove(academy);*/

		studentRepository.insertJoin(academyCode, studentFiscalCode);
		
		return msg = findStudentsByAcademy(academyCode).get(0) != null? new Message("Operation Succeded") : new Message("Operation Failed");
	}

	@Override
	public Message deleteOnJoinTableAcademyStudent(String studentFiscalCode, String academyCode) throws Exception {
		
		Student student = findStudentById(studentFiscalCode);
		Academy academy = academyService.findAcademyById(academyCode);
		
		int countStudentsOnJoinBefore = findStudentsByAcademy(academyCode).size();
		
		academy.getStudents().remove(student);
		student.getAcademies().remove(academy);
		
		List<Student> students = academy.getStudents();
		List<Academy> academies = student.getAcademies();
		
		studentRepository.deleteJoin(studentFiscalCode, academyCode);
		
		for(Academy acd : academies)
			for(Student stud : students)
				if(!findStudentsByAcademy(acd.getCode()).contains(stud))
					addOnJoinTableAcademyStudent(acd.getCode(), stud.getFiscalCode());
		/*
		for(Academy acd : academies)
			addOnJoinTableAcademyStudent(acd.getCode(), student.getFiscalCode());

		for(Student stud : students)
			if(!findStudentsByAcademy(academyCode).contains(stud))
				addOnJoinTableAcademyStudent(academy.getCode(), stud.getFiscalCode());
		*/
		
		/*
		List<Student> students = academy.getStudents();
		List<Academy> academies = student.getAcademies();
		
		int countStudentsOnJoinBefore = findStudentsByAcademy(academyCode).size();
		
		if(students.size()> 0)
			students.remove(academy);
		
		if(academies.size()> 0)
			academies.remove(student);
		
		academy.getStudents().clear();
		student.getAcademies().clear();
		
		studentRepository.deleteJoin(studentFiscalCode, academyCode);
		
		student.setAcademies(academies);
		academy.setStudents(students);
		
		addOrUpdateStudent(student);
		academyService.addOrUpdateAcademy(academy);
		
		for(Student stud : academy.getStudents())
			addOnJoinTableAcademyStudent(academy.getCode(), stud.getFiscalCode());
		
		for(Academy acd : student.getAcademies())
			addOnJoinTableAcademyStudent(acd.getCode(), student.getFiscalCode());
		
		int countStudentsOnJoinAfter = findStudentsByAcademy(academyCode).size();
		
//		addOrUpdateStudent(student);
//		academyService.addOrUpdateAcademy(academy);
		*/
		int countStudentsOnJoinAfter = findStudentsByAcademy(academyCode).size();
		
		return msg = countStudentsOnJoinBefore > countStudentsOnJoinAfter? new Message("Operation Succeded") : new Message("Operation Failed");
	}

	private int ageCalculator(Student student) {return Period.between(student.getBirthDate().toLocalDate(), LocalDate.now()).getYears();}
}