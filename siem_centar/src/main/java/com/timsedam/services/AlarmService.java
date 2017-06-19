package com.timsedam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timsedam.models.Alarm;
import com.timsedam.repository.AlarmRepository;

import java.util.Date;

@Service
public class AlarmService {

	@Autowired
	private AlarmRepository repository;
	
	public Alarm save(Alarm alarm) {
		return repository.save(alarm);
	}
	public String getAlarmNum(String agentId, String monitorId , Date start, Date end){return repository.getAlarmNum(agentId,monitorId,start,end);}

}
