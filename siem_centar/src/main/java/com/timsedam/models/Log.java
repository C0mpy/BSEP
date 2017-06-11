package com.timsedam.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Log {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String log;
	
	@Column(nullable = false)
	private String agentId;

	@Column(nullable = false)
	private String monitorId;

	@Column(nullable = false)
	private String regex;

	@Column(nullable = false)
	private String structure;

	public Map<String,String> data;
	
	public Log(){}

	public Long getId() {
		return id;
	}

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

	public void parseLogData(){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(log);

		if(m.matches()){
			String[] atr_names = structure.split(" ");
			for (int i = 0; i < atr_names.length; i++) {
				this.data.put(atr_names[i], m.group(i));
			}
		}
	}
	
	
}
