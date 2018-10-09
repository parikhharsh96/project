package com.bank.repo;

import java.util.List;

import com.bank.entity.Accounts;
import com.bank.entity.FixedDeposit;

/**
 * @author Pranjali Shirke
 * Version 1.0
 */
public interface FixedDepoRepo {

	public void createFdAccount(FixedDeposit depositAccount,String accountNo);

	public Accounts getAccountNo(String accountNo);

	public List<FixedDeposit> displayFixedDeposits(String accountNo);
	
	public Accounts checkFdAccountRecursion(String id);
	
	public double preMatureInterest(String deleteMe, String accountNo);

	public Accounts checkForFixedDeposit(String accountNo);

}
