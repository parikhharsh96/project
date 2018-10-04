package com.bank.repository;

import java.util.List;

import com.bank.bean.PayeeBean;
import com.bank.bean.RegisterPayeeBean;
import com.bank.bean.UpiBean;
import com.bank.entity.Accounts;
import com.bank.entity.Payee;

public interface FundTransferRepo {
	
	
	
	boolean transferUpi(UpiBean upiBean);
	
	boolean registerPayee( RegisterPayeeBean registerPayeeBean);

	

	Accounts getUpiHolder(UpiBean upiBean);

	 List<Payee> getRegisteredPayee(Accounts user);

	boolean transferPayee(String payer, String payee, double amount);
	
	
	

}
