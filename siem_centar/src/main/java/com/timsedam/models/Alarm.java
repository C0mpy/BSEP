package com.timsedam.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Alarm {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable=false)
	private Date date;
	
	@Column(nullable = false)
	private String message;
	
	@Column(nullable = false)
	private String agentId;

	@Column(nullable = false)
	private String monitorId;
	
	public Alarm(){}

	public Alarm(String name, Date date, String message, String agentId, String monitorId) {
		super();
		this.name = name;
		this.date = date;
		this.message = message;
		this.agentId = agentId;
		this.monitorId = monitorId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
	
	
}
