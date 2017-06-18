package com.timsedam.dto;

import java.util.Date;

/**
 * Created by sirko on 6/18/17.
 */
public class MetricDTO {
    private String agent;
    private String monitor;
    private Date start;
    private Date end;

    public MetricDTO() {

    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
