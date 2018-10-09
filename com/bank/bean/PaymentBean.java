package com.bank.bean;

public class PaymentBean {
	String upi;
	String cardNo;
	double amount;
	String pin;

	public PaymentBean() {
	}

	public String getUpi() {
		return upi;
	}

	public void setUpi(String upi) {
		this.upi = upi;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin2) {
		this.pin = pin2;
	}

}
