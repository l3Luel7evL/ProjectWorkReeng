package com.energent.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Student {
	
	@Id
	@Size(min = 16, max = 16)
	@Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")
	private String fiscalCode;
	
	@Column(length = 75, nullable = false)
	@Pattern(regexp = "[a-zA-Z]+")
	private String firstName, lastName;
	
	@Column(nullable = false)
	//@Pattern(regexp = "/([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))/")
	@Pattern(regexp = "([1900-2099]-[1-12]-[1-31])")
	private Date birthDate;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "academy_student", 
			   joinColumns = @JoinColumn(name="student_fiscal_code"),
			   inverseJoinColumns = @JoinColumn(name="academy_code"),
			       uniqueConstraints = @UniqueConstraint(columnNames = {
			       "student_fiscal_code", "academy_code"}))
	private List<Academy> academies = new ArrayList<>();
	
	public Student() {}
	public Student(String fiscalCode, String firstName, String lastName, Date birthDate) {
		this.fiscalCode = fiscalCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		
	}
	
	public String getFiscalCode() {return fiscalCode;}
	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
		
	}
	public String getFirstName() {return firstName;}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		
	}
	public String getLastName() {return lastName;}
	public void setLastName(String lastName) {
		this.lastName = lastName;
		
	}
	public Date getBirthDate() {return birthDate;}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		
	}
	public List<Academy> getAcademies() {return academies;}
	public void setAcademies(List<Academy> academies) {
		this.academies = academies;
		
	}	
}