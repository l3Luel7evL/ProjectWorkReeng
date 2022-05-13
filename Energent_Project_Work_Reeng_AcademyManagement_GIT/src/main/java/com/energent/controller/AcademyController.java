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
import com.energent.entity.Academy;
import com.energent.entity.Student;
import com.energent.service.AcademyService;

@RestController
@RequestMapping("/academy-service")
public class AcademyController {

	@Autowired
	private AcademyService academyService;
	
	@GetMapping("/academies")
	public List<Academy> findAllAcademies(){return academyService.findAllAcademies();}
	
	@GetMapping("/academies/academyCode/{academyCode}")
	public Academy findAcademyByCode(@PathVariable String academyCode) {return academyService.findAcademyById(academyCode);}

	@PostMapping("/academies/addOrUpdate")
	public Academy addOrUpdateAcademy (@Valid @RequestBody Academy academy) throws Exception {return academyService.addOrUpdateAcademy(academy);}
	
	@DeleteMapping("/academies/remove/{academyCode}")
	public Message removeAcademy(@PathVariable String academyCode) {return academyService.removeAcademy(academyCode);}
	
	/*
	 * Da vedere per ora inutile
	private Message checkAcademy(Academy academy, Exception e) {
		Message msg;
		
		return msg = academy == null? new Message(e.getMessage()) : new Message("Operation Succeded");
	}*/
}
