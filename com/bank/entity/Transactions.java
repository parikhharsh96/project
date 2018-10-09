package com.bank.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Harsh Parikh, Pranjali Shirke, Shalini Pereira, Ratan Boddu
 * Version 1.0
 */
@Entity
@Table(name = "transactions")
@SequenceGenerator(name = "seqgn", sequenceName = "transaction_seq")
public class Transactions implements Comparable<Transactions>{
	@Id
	@GeneratedValue(generator = "seqgn")
	@Column(name = "transaction_id")
	private int transactionId;
	@Column(name = "transaction_type")
	private String transactionType;
	private double balance;
	private double amount;
	private String message;
	
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Temporal(TemporalType.DATE)
	private Date timestamp;
	@ManyToOne
	@JoinColumn(name = "account_no")
	private Accounts accounts;

	public Transactions() {
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Accounts getAccounts() {
		return accounts;
	}

	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}

	@Override
	public int compareTo(Transactions t) {
		 return getTimestamp().compareTo(t.getTimestamp());
	}
}
