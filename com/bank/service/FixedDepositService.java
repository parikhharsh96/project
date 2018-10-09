package com.bank.service;

import java.util.List;

import com.bank.entity.Accounts;
import com.bank.entity.FixedDeposit;

/**
 * @author Pranjali Shirke Version 1.0
 */
public interface FixedDepositService {

	public double calculateMaturityValue(double principalAmount, double numberOfPeriod);

	public String generateFixedDepositNumber(String initialNumber);

	public void createFdAccount(FixedDeposit depositAccount, String accountNo);

	public Accounts getAccountNo(String accountNo);

	public java.sql.Date endDateValue(int year);

	public List<FixedDeposit> displayFixedDeposits(String accountNo);

	public Accounts checkFdAccountRecursion(String id);

	public double preMatureInterest(String deleteMe, String accountNo);

	Accounts checkForFixedDeposit(String accountNo);

}
