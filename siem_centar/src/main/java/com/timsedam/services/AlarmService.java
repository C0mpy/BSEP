package com.timsedam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timsedam.models.Alarm;
import com.timsedam.repository.AlarmRepository;

@Service
public class AlarmService {

	@Autowired
	private AlarmRepository repository;
	
	public Alarm save(Alarm alarm) {
		return repository.save(alarm);
	}
}
