package com.bank.bean;

/**
 * @author Harsh Parikh 
 * Version 1.0
 */

public class UpiBean {
	private String payerUpi;
	private String payeeUpi;
	private double amount;

	public String getPayerUpi() {
		return payerUpi;
	}

	public void setPayerUpi(String payerUpi) {
		this.payerUpi = payerUpi;
	}

	public String getPayeeUpi() {
		return payeeUpi;
	}

	public void setPayeeUpi(String payeeUpi) {
		this.payeeUpi = payeeUpi;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
