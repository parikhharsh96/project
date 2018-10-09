package com.bank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @author Harsh Parikh, Pranjali Shirke, Shalini Pereira, Ratan Boddu
 * Version 1.0
 */
@Entity
public class Cards {
	@Id
	@Column(name="card_no")
	private String cardNo;

	private String expiry;
	private String pin;
	
	public void setPin(String pin) {
		this.pin = pin;
	}

	@OneToOne
	@JoinColumn(name = "account_no") // One to one relationship with Accounts and the column needed i.e. account_no
	private Accounts accounts;
	
	// generating Getters and Setters

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}


	public Accounts getAccounts() {
		return accounts;
	}

	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}

}
