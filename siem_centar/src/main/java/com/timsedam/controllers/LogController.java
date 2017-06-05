package com.timsedam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.timsedam.dto.LogDTO;
import com.timsedam.models.Log;
import com.timsedam.services.LogService;

@RestController
@RequestMapping(value = "/api/logs")
public class LogController {
	
	@Autowired
	private LogService service;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> saveLogs(@RequestBody LogDTO logDTO) {
		
		Log log = new Log();
		log.setAgentId(logDTO.getAgentId());
		log.setLog(logDTO.getLog());
		service.save(log);
		return new ResponseEntity<String>("Log is saved in database", HttpStatus.OK);
	}
}
