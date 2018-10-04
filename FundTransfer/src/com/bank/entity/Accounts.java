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
import javax.transaction.Transaction;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "accounts")
public class Accounts {
	@Id
	@Column(name="account_no")
	private String accountNo;
	/* private int aadhaarNo; */
	private String email;
	private double balance;
	private String password;
	@Column(name="opening_date")
	private String openingDate;
	@NaturalId
	private String upi;
	@Column(name="acholder_name")
	private String accountHolderName;
	@OneToOne(orphanRemoval = true)
	@JoinColumn(name = "aadhaar_no" )
	private Aadhaar aadhaar;
	@OneToOne(mappedBy = "accounts", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Cards cards;
	@OneToOne(mappedBy = "accounts", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.LAZY)
	private FixedDeposit fixedDeposit;
	@OneToMany(mappedBy = "accounts", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Transactions> transactions;

	@OneToMany(mappedBy="payer", cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<Payee> payee; 

	
	
	public Set<Payee> getPayee() {
		return payee;
	}

	public void setPayee(Set<Payee> payee) {
		this.payee = payee;
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

	

	public String getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public Cards getCards() {
		return cards;
	}

	public void setCards(Cards cards) {
		this.cards = cards;
	}

	public FixedDeposit getFixedDeposit() {
		return fixedDeposit;
	}

	public void setFixedDeposit(FixedDeposit fixedDeposit) {
		this.fixedDeposit = fixedDeposit;
	}

	public Set<Transactions> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transactions> transactions) {
		this.transactions = transactions;
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
