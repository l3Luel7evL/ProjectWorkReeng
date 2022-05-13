package com.energent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.energent.bean.Message;
import com.energent.entity.*;
import com.energent.repository.*;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private AcademyService academyService;

	@Override
	public Student findStudentById(String id) {return studentRepository.findById(id).get();}

	@Override
	public List<Student> findAllStudents() {return studentRepository.findAll();}

	@Override
	public List<Student> findStudentsByAcademies(List<Academy> academies) {return studentRepository.findAllStudentsByAcademiesIn(academies);}

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
		Message msg;
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
		Academy academy = academyService.findAcademyById(academyCode);
		Student student = findStudentById(studentFiscalCode);
		
		if(student.getAcademies().contains(academy))
			student.getAcademies().remove(academy);

		studentRepository.insertJoin(academyCode, studentFiscalCode);
		
		
		return null;
	}

	@Override
	public Message deleteOnJoinTableAcademyStudent(String studentFiscalCode, String academyCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int ageCalculator(Student student) {
		// TODO Auto-generated method stub
		return 0;
	}

}
