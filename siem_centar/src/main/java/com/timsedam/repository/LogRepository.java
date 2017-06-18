package com.timsedam.repository;

import com.timsedam.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long>{

	List<Log> findAllByMonitorId(String monitor_id);

	@Query(nativeQuery = true, value = "SELECT DISTINCT agent_id FROM log")
	List<String> findAllAgents();

	// parametrizovani upiti da bi se izbegao SQLI
	@Query(nativeQuery = true, value = "SELECT DISTINCT monitor_id FROM log WHERE agent_id = :agentId")
	List<String> findMonitors(@Param("agentId") String agentId);

	@Query(nativeQuery=true, value="SELECT count(*) FROM log where" +
			" (:agentId='all' or agent_id=:agentId) AND" +
			" (:monitorId='all' or monitor_id=:monitorId) AND" +
			"  date >= :start AND "+
			" date <= :end")
		String getLogNum(@Param("agentId") String agentId,
					  @Param("monitorId") String monitorId,
			          @Param("start")  Date start,
					  @Param("end") Date end);
}
