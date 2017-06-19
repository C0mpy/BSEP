package com.timsedam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timsedam.models.Alarm;

public interface AlarmRepository  extends JpaRepository<Alarm, Long>{

	

}
