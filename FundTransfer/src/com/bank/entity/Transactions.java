package com.bank.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="transactions")
@SequenceGenerator(name="seqgn", sequenceName="transaction_seq")
public class Transactions {
	@Id
	@GeneratedValue(generator="seqgn")
	@Column(name="transaction_id")
	private int transactionId;
	
	@Column(name="transaction_type")
	private String transactionType;
	private double balance;
	private double amount;
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

	/*public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}*/

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

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public Accounts getAccounts() {
		return accounts;
	}

	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}

}
