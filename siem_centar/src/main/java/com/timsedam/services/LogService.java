package com.timsedam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timsedam.models.Log;
import com.timsedam.repository.LogRepository;

import java.util.List;


@Service
public class LogService {

	@Autowired
	private LogRepository repository;
	
	public Log save(Log log) {
		return repository.save(log);
	}

	//posle nece trebati valjda
	public Log get(long id){
		Log l=repository.findOne(id);
		l.parseLogData();
		return l;
	}

	public List<Log> findAllByMonitorId(String monitorId){
		List<Log> logs = repository.findAllByMonitorId(monitorId);
		for(Log l : logs){
			l.parseLogData();
		}
		return logs;
	}

}
