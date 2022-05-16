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
import com.energent.entity.Academy;
import com.energent.service.AcademyService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping("/academy-service")
public class AcademyController {

	@Autowired
	private AcademyService academyService;
	
	@ApiOperation(value = "Return a list that contains all the academies.")
	@GetMapping("/academies")
	public List<Academy> findAllAcademies(){return academyService.findAllAcademies();}
	
	@ApiOperation(value = "Find specifical academy by given code.")
	@GetMapping("/academies/academyCode/{academyCode}")
	public Academy findAcademyByCode(@ApiParam(value = "Academy code : representative code of an academy, Primary Key of the entity.", example = "AcademyCode1") @PathVariable String academyCode) {return academyService.findAcademyById(academyCode);}

	@ApiOperation(value = "Add an academy to the DataBase, if the given id is alredy taken the academy will be updated.")
	@PostMapping("/academies/addOrUpdate")
	public Academy addOrUpdateAcademy(@ApiParam(value = "<b>An academy, representing the entity provided of:</b><ul><li>Code</li><li>Title</li><li>Location</li><li>Start Date</li><li>End Date</li></ul>"
+ "<br><p><i><small><b>Exceptions will be thrown when:</b><ul><li>The End Date field value is before the Start Date</li><li>Title or Description have special characters</li></ul></small></i></p>"
, required = true) @Valid @RequestBody Academy academy) throws Exception {return academyService.addOrUpdateAcademy(academy);}
	
	@ApiOperation(value = "Remove an academy from the DataBase, if one or more students were related with, the relation will be removed too.")
	@DeleteMapping("/academies/remove/{academyCode}")
	public Message removeAcademy(@ApiParam(value = "Univoque code representing the academy.<br><p><i><small><b>Will be returned a message containing:</b><ul><li>Operation Succeded/Failed</li></ul></small></i></p>") @PathVariable String academyCode) {return academyService.removeAcademy(academyCode);}

}