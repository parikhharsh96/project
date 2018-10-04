package com.bank.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.bean.PayeeBean;
import com.bank.entity.Accounts;
import com.bank.entity.Payee;
import com.bank.service.FundTransferService;



@Controller
public class FundTransferController {

	@Autowired
	private FundTransferService service;
	
	@RequestMapping(value = "showRegisteredPayee.bank", method = RequestMethod.GET)
	public String showRegisteredPayee( Map model, HttpSession session) {
		Accounts user=new Accounts();
		user.setAccountNo("123456789013");
		session.setAttribute("USER", user);
		
		List<Payee> payees=service.getRegisteredPayee(user);
		
		model.put("Payees", payees);		
		return "registeredpayee";
		

	}
	
	@RequestMapping(value="fundTransfer.bank",method=RequestMethod.GET)
	public String transferPayee(@RequestParam("payeeAccountNo")String payee, @RequestParam("amount") double amount,Map model, HttpSession session)
	{
		
		System.out.println("in controller");
		String payerAccountNumber="123456789013";
		
		System.out.println(payee);
		System.out.println(amount);
		service.transferPayee(payerAccountNumber,payee,amount);
		return "showtransferresults";
		
		
	}
}
