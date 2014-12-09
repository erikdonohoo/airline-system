package com.donohoo.airlinesystem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {

	@JsonInclude(Include.NON_NULL)
	Integer id;
	@JsonInclude(Include.NON_NULL)
	Integer faaId;
	@JsonInclude(Include.NON_NULL)
	String depCity;
	@JsonInclude(Include.NON_NULL)
	String destCity;
	@JsonInclude(Include.NON_NULL)
	Integer depTime;
	@JsonInclude(Include.NON_NULL)
	Integer arrTime;
	@JsonInclude(Include.NON_NULL)
	Integer airfare;
	@JsonInclude(Include.NON_NULL)
	Integer mileage;
	@JsonInclude(Include.NON_NULL)
	Integer totalPass;
	@JsonInclude(Include.NON_NULL)
	Integer pilotId;
	@JsonInclude(Include.NON_NULL)
	Integer copilotId;
	@JsonInclude(Include.NON_NULL)
	Boolean newRoute;
	@JsonInclude(Include.NON_NULL)
	Boolean newCost;
	
	public Boolean getNewRoute() {
		return newRoute;
	}
	public void setNewRoute(Boolean newRoute) {
		this.newRoute = newRoute;
	}
	public Boolean getNewCost() {
		return newCost;
	}
	public void setNewCost(Boolean newCost) {
		this.newCost = newCost;
	}
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
	public String getDepCity() {
		return depCity;
	}
	public void setDepCity(String depCity) {
		this.depCity = depCity;
	}
	public String getDestCity() {
		return destCity;
	}
	public void setDestCity(String destCity) {
		this.destCity = destCity;
	}
	public Integer getDepTime() {
		return depTime;
	}
	public void setDepTime(Integer depTime) {
		this.depTime = depTime;
	}
	public Integer getArrTime() {
		return arrTime;
	}
	public void setArrTime(Integer arrTime) {
		this.arrTime = arrTime;
	}
	public Integer getAirfare() {
		return airfare;
	}
	public void setAirfare(Integer airfare) {
		this.airfare = airfare;
	}
	public Integer getMileage() {
		return mileage;
	}
	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}
	public Integer getTotalPass() {
		return totalPass;
	}
	public void setTotalPass(Integer totalPass) {
		this.totalPass = totalPass;
	}
	public Integer getPilotId() {
		return pilotId;
	}
	public void setPilotId(Integer pilotId) {
		this.pilotId = pilotId;
	}
	public Integer getCopilotId() {
		return copilotId;
	}
	public void setCopilotId(Integer copilotId) {
		this.copilotId = copilotId;
	}
}
