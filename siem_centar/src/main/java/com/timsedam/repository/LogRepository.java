package com.timsedam.repository;

import com.timsedam.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long>{

	List<Log> findAllByMonitorId(String monitor_id);

	@Query(nativeQuery = true, value = "SELECT DISTINCT agent_id FROM log")
	List<String> findAllAgents();

	// parametrizovani upiti da bi se izbegao SQLI
	@Query(nativeQuery = true, value = "SELECT DISTINCT monitor_id FROM log WHERE agent_id = :agentId")
	List<String> findMonitors(@Param("agentId") String agentId);

}
