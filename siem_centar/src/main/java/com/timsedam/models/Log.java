package com.timsedam.models;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

@Entity
public class Log {

	@Id
	@GeneratedValue
	private Long id;
	
	@Lob
	@Column(nullable = false)
	private String log;
	
	@Column(nullable = false)
	private String agentId;

	@Column(nullable = false)
	private String monitorId;

	@Lob
	@Column(nullable = false)
	private String regex;

	@Column(nullable = false)
	private String structure;
	
	@Column(nullable = false)
	private String system;
	
	@Column(nullable = false)
	private String logName;

	@Column(nullable = false)
	private String type;

	@Transient
	public Map<String,String> data=new HashMap<String, String>() ;
	
	public Log(){}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
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

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void parseLogData(){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(log);

		if(m.matches()){
			String[] atr_names = structure.split(" ");
			for (int i = 0; i < atr_names.length; i++) {
				this.data.put(atr_names[i], m.group(i+1));
			}
		}
	}
	
	
}
