package com.donohoo.airlinesystem;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Record {

	@JsonInclude(Include.NON_NULL)
	Integer id;
	@JsonInclude(Include.NON_NULL)
	Integer faaId;
	@JsonInclude(Include.NON_NULL)
	Timestamp maintDate;
	@JsonInclude(Include.NON_NULL)
	Timestamp nextDate;
	@JsonInclude(Include.NON_NULL)
	List<Integer> jobs;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFaaId() {
		return faaId;
	}
	public void setFaaId(Integer faaId) {
		this.faaId = faaId;
	}
	public Timestamp getMaintDate() {
		return maintDate;
	}
	public void setMaintDate(Timestamp maintDate) {
		this.maintDate = maintDate;
	}
	public Timestamp getNextDate() {
		return nextDate;
	}
	public void setNextDate(Timestamp nextDate) {
		this.nextDate = nextDate;
	}
	public List<Integer> getJobs() {
		return jobs;
	}
	public void setJobs(List<Integer> jobs) {
		this.jobs = jobs;
	}
	
	
}
