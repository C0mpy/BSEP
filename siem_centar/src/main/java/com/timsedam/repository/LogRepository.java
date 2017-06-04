package com.timsedam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timsedam.models.Log;

public interface LogRepository extends JpaRepository<Log, Long>{

	

}
