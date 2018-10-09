package com.bank.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

/**
 * @author Harsh Parikh, Pranjali Shirke, Shalini Pereira, Ratan Boddu
 * Version 1.0
 */
@Entity
@Table(name = "accounts")
public class Accounts {
	public Accounts() {
	}

	@Id
	@Column(name = "account_no")
	private String accountNo;

	private String email;

	private double balance;

	private String password;

	@Column(name = "opening_date")
	private Date openingDate;

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public Cards getCards() {
		return cards;
	}

	public void setCards(Cards cards) {
		this.cards = cards;
	}

	@NaturalId
	private String upi;

	@Column(name = "acholder_name")
	private String accountHolderName;

	@OneToOne(orphanRemoval = true)
	@JoinColumn(name = "aadhaar_no") // Accounts, here has a one to one relationship with Aadhaar and the column
										// needed is aadhaar_no
	// This relationship here is unidirectional and hence we won't be writing
	// @OneToOne in Aadhaar
	// because Aadhaar does not need to remember Accounts
	private Aadhaar aadhaar;

	@OneToOne(mappedBy = "accounts", cascade = { CascadeType.ALL }) // Accounts is the owner of this one to one
																	// relationship with Cards since Cards is dependent
																	// on Accounts
	private Cards cards;

	@OneToMany(mappedBy = "accounts", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Transactions> transactions;

	@OneToMany(mappedBy = "accounts", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<FixedDeposit> fixedDeposit;
	
	@OneToMany(mappedBy="payer", cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<Payee> payee; 
	// Generating Getters and Setters

	public Set<Payee> getPayee() {
		return payee;
	}

	public void setPayee(Set<Payee> payee) {
		this.payee = payee;
	}

	public Set<FixedDeposit> getFixedDeposit() {
		return fixedDeposit;
	}

	public void setFixedDeposit(Set<FixedDeposit> fixedDeposit) {
		this.fixedDeposit = fixedDeposit;
	}

	public Set<Transactions> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transactions> transactions) {
		this.transactions = transactions;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUpi() {
		return upi;
	}

	public void setUpi(String upi) {
		this.upi = upi;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public Aadhaar getAadhaar() {
		return aadhaar;
	}

	public void setAadhaar(Aadhaar aadhaar) {
		this.aadhaar = aadhaar;
	}

}
