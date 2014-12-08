package com.donohoo.airlinesystem;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Passenger {

	@JsonInclude(Include.NON_NULL)
	Integer id;
	@JsonInclude(Include.NON_NULL)
	String name;
	@JsonInclude(Include.NON_NULL)
	List<Integer> flights;
	
	public List<Integer> getFlights() {
		return flights;
	}
	public void setFlights(List<Integer> flights) {
		this.flights = flights;
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
}
