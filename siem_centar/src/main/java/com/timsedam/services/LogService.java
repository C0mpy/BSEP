package com.timsedam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timsedam.models.Log;
import com.timsedam.repository.LogRepository;


@Service
public class LogService {

	@Autowired
	private LogRepository repository;
	
	public String save(Log log) {
		System.out.println("dasa");
		return "upisan";
	}
}
