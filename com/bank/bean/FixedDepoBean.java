package com.bank.bean;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Pranjali Shirke 
 * Version 1.0
 */
public class FixedDepoBean {

	private double principalAmount, maturityValue, interestEarned;
	private int tenure;
	private Date startDate;
	private Date endDate;

	public double getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(double principalAmount) {
		this.principalAmount = principalAmount;
	}

	public double getMaturityValue() {
		return maturityValue;
	}

	public void setMaturityValue(double maturityValue) {
		this.maturityValue = maturityValue;
	}

	public double getInterestEarned() {
		return interestEarned;
	}

	public void setInterestEarned(double interestEarned) {
		this.interestEarned = interestEarned;
	}

	public int getTenure() {
		return tenure;
	}

	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	public Date getStartDate() {
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		java.util.Date uDate = date.getTime();
		return uDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
