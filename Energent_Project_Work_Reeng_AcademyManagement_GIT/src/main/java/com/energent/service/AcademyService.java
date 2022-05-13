package com.energent.service;

import java.util.List;

import com.energent.bean.Message;
import com.energent.entity.Academy;

public interface AcademyService {
	
	Academy findAcademyById(String id);
	Academy addOrUpdateAcademy(Academy academy) throws Exception;
	Message removeAcademy(String id);
	List<Academy> findAllAcademies();
	
}