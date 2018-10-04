package com.bank.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.bean.PayeeBean;
import com.bank.bean.RegisterPayeeBean;
import com.bank.bean.UpiBean;
import com.bank.entity.Accounts;
import com.bank.entity.Payee;
import com.bank.repository.FundTransferRepo;
@Service
public class FundTransferServiceImpl implements FundTransferService {
	
	

	public FundTransferServiceImpl() {
	}
	
	@Autowired
	private FundTransferRepo repo;
	

	@Override
	public void transferPayee(String payer,String payee,double amount) {
			
			System.out.println("in service");
		
			if(repo.transferPayee( payer,payee, amount))
			{
				System.out.println("Funds transferred successfully");
			}
			else
			{
				System.out.println("Funds transfer unsuccessful");
			}
		
	
	}

	@Override
	public void transferUpi(UpiBean upiBean) {
		
		
		
		if(repo.transferUpi(upiBean))
		{
			System.out.println("Funds transferred successfully");
		}
		else
		{
			System.out.println("Funds transfer unsuccessful");
		}
		

	}

	@Override
	public void registerPayee(RegisterPayeeBean registerPayeeBean) {
		
		System.out.println("in service");
		
		if(repo.registerPayee(registerPayeeBean))
		{
			System.out.println("Payee registered successfully");
		}
		else
		{
			System.out.println("Payee registration unsuccessful");
		}

	}

	@Override
	public Accounts getUpiHolder(UpiBean upiBean) {
		
		if(repo.getUpiHolder(upiBean)!=null)
		{
			return repo.getUpiHolder(upiBean);
		}
		else
		{
			System.out.println("there is no user with such upi");
			return null;
		}
		
		
	}

	@Override
	public List<Payee> getRegisteredPayee(Accounts user) {
		
		
		
		List<Payee> payee=repo.getRegisteredPayee(user);
		for (Payee payee2 : payee) {
			System.out.println(payee2.getPayeeName());
		}
		
		
		return payee;
		
		
	}

}
