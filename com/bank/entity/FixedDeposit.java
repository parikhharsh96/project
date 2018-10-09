package com.bank.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Harsh Parikh, Pranjali Shirke, Shalini Pereira, Ratan Boddu
 * Version 1.0
 */
@Entity
@Table(name = "fixed_deposit")
public class FixedDeposit implements Comparable<FixedDeposit>{
	
	@Id
	@Column(name = "fdeposit_no")
	private String fixedDepositNo;

	@OneToOne
	@JoinColumn(name = "account_no")
	private Accounts accounts;

	@Column(name = "principal_amount")
	private double principalAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "maturity_amount")
	private double maturityValue;

	public String getFixedDepositNo() {
		return fixedDepositNo;
	}

	public void setFixedDepositNo(String fixedDepositNo) {
		this.fixedDepositNo = fixedDepositNo;
	}

	public Accounts getAccounts() {
		return accounts;
	}

	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}

	public double getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(double principalAmount) {
		this.principalAmount = principalAmount;
	}

	public Date getStartDate() {
		return startDate;
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

	public double getMaturityValue() {
		return maturityValue;
	}

	public void setMaturityValue(double maturityValue) {
		this.maturityValue = maturityValue;
	}

	@Override
	public int compareTo(FixedDeposit fixedDeposits) {
	
		return getStartDate().compareTo(fixedDeposits.startDate);
	}

}
