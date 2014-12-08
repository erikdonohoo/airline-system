package com.donohoo.airlinesystem;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Crew {

	@JsonInclude(Include.NON_NULL)
	Integer id;
	@JsonInclude(Include.NON_NULL)
	String name;
	@JsonInclude(Include.NON_NULL)
	String position;
	@JsonInclude(Include.NON_NULL)
	List<Integer> flights;
	@JsonInclude(Include.NON_NULL)
	Integer flyHours;
	@JsonInclude(Include.NON_NULL)
	Integer salary;
	@JsonInclude(Include.NON_NULL)
	Integer seniority;
	@JsonInclude(Include.NON_NULL)
	Integer supervisor;
	
	public Integer getFlyHours() {
		return flyHours;
	}
	public void setFlyHours(Integer flyHours) {
		this.flyHours = flyHours;
	}
	public Integer getSalary() {
		return salary;
	}
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	public Integer getSeniority() {
		return seniority;
	}
	public void setSeniority(Integer seniority) {
		this.seniority = seniority;
	}
	public Integer getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(Integer supervisor) {
		this.supervisor = supervisor;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public List<Integer> getFlights() {
		return flights;
	}
	public void setFlights(List<Integer> flights) {
		this.flights = flights;
	}
}
