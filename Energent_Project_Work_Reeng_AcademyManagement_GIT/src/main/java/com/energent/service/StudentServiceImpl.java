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
	public Message removeStudent(String studentFiscalCode /*, String academyCode*/) {
		
		Student student = findStudentById(studentFiscalCode);
		
		studentRepository.deleteById(studentFiscalCode);
		
		return msg = studentRepository.existsById(studentFiscalCode)? new Message("Operation Failed") : new Message("Operation Succeded");
	}

	@Override
	public Message addOnJoinTableAcademyStudent(String academyCode, String studentFiscalCode) {

		Student student = findStudentById(studentFiscalCode);
		Academy academy = academyService.findAcademyById(academyCode);
		
		List<Student> students = academy.getStudents();
		List<Academy> academies = student.getAcademies();
		
		academy.getStudents().add(student);
		student.getAcademies().add(academy);
		
		for(Academy acd : academies)
			for(Student stud : students)
				if(!findStudentsByAcademy(acd.getCode()).contains(stud))
					studentRepository.insertJoin(acd.getCode(), stud.getFiscalCode());
		
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
		
		int countStudentsOnJoinAfter = findStudentsByAcademy(academyCode).size();
		
		return msg = countStudentsOnJoinBefore > countStudentsOnJoinAfter? new Message("Operation Succeded") : new Message("Operation Failed");
	}

	private int ageCalculator(Student student) {return Period.between(student.getBirthDate().toLocalDate(), LocalDate.now()).getYears();}
}