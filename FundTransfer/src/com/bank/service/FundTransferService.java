package com.bank.service;

import java.util.List;

import com.bank.bean.PayeeBean;
import com.bank.bean.RegisterPayeeBean;
import com.bank.bean.UpiBean;
import com.bank.entity.Accounts;
import com.bank.entity.Payee;

public interface FundTransferService {
	
	
	
	void transferUpi(UpiBean upiBean);
	
	void registerPayee( RegisterPayeeBean registerPayeeBean);
	
	Accounts getUpiHolder(UpiBean upiBean);
	
	 List<Payee> getRegisteredPayee(Accounts user);

	void transferPayee(String payer, String payee, double amount);

}
