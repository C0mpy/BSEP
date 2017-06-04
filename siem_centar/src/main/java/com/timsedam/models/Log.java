package com.timsedam.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Log {

	@Id
	@GeneratedValue
	private Long id;
	
	public Log(){}
}
