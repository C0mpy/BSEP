package com.timsedam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timsedam.models.Alarm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface AlarmRepository  extends JpaRepository<Alarm, Long>{

    @Query(nativeQuery=true, value="SELECT count(*) FROM alarm where" +
            " (:agentId='all' or agent_id=:agentId) AND" +
            " (:monitorId='all' or monitor_id=:monitorId) AND" +
            "  date >= :start AND "+
            " date <= :end")
    String getAlarmNum(@Param("agentId") String agentId,
                     @Param("monitorId") String monitorId,
                     @Param("start") Date start,
                     @Param("end") Date end);

}
