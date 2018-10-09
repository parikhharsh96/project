package com.bank.bean;

import org.springframework.stereotype.Component;

/**
 * @author Ratan Boddu 
 * Version 1.0
 */
@Component
public class RegisterBean {
	public RegisterBean() {
	}

	private String aadhaarNo;
	private String email;
	private String password;
	private String panNo;
	private String upiName;
	private String acHolderName;
	private String accountNo;
	private String openDate;

	public String getUpiName() {
		return upiName;
	}

	public void setUpiName(String upiName) {
		this.upiName = upiName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getAadhaarNo() {
		return aadhaarNo;
	}

	public void setAadhaarNo(String aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}

	public String getPhoneNo() {
		return upiName;
	}

	public void setPhoneNo(String phoneNo) {
		this.upiName = phoneNo;
	}

	public String getAcHolderName() {
		return acHolderName;
	}

	public void setAcHolderName(String acHolderName) {
		this.acHolderName = acHolderName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

}
