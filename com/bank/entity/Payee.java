package com.bank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Harsh Parikh, Pranjali Shirke, Shalini Pereira, Ratan Boddu
 * Version 1.0
 */

@Entity
@Table(name = "registered_payee",uniqueConstraints= @UniqueConstraint(columnNames={"payer_ac_no", "payee_ac_no"}))
@SequenceGenerator(name="seqgn", sequenceName="Payee_seq")
public class Payee {
	@Id
	@GeneratedValue(generator="seqgn")
	private int srNo;
	
	@ManyToOne
	@JoinColumn(name="payee_ac_no",nullable=false)
	private Accounts payee;
	
	@Column(name = "payee_name")
	private String payeeName;

	@ManyToOne
	@JoinColumn(name="payer_ac_no",nullable=false)
	private Accounts payer; //self association

	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public Accounts getPayee() {
		return payee;
	}
	public void setPayee(Accounts payee) {
		this.payee = payee;
	}

	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public Accounts getPayer() {
		return payer;
	}
	public void setPayer(Accounts payer) {
		this.payer = payer;
	}
	
}
