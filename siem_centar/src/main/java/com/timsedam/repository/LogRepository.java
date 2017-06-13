package com.timsedam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timsedam.models.Log;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long>{

	List<Log> findAllByMonitorId(String monitor_id);

}
