package com.bank.repo;

import java.util.List;

import com.bank.bean.RegisterPayeeBean;
import com.bank.bean.UpiBean;
import com.bank.entity.Accounts;
import com.bank.entity.Payee;

/**
 * @author Harsh Parikh 
 * Version 1.0
 */
public interface FundTransferRepo {
	
	
	
	boolean transferUpi(UpiBean upiBean);
	
	boolean registerPayee( RegisterPayeeBean registerPayeeBean);

	

	Accounts getUpiHolder(UpiBean upiBean);

	 List<Payee> getRegisteredPayee(Accounts user);

	boolean transferPayee(String payer, int payeeSrNo, double amount,String message);
	public Accounts checkIfPayeeExists(RegisterPayeeBean registerPayeeBean);

	Accounts getNewAccountInfo(String payerAccountNumber);

	Accounts getNewAccountInfoUpi(String payerUpi);
	
	
	

}
