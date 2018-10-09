package com.bank.ctrl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.bank.bean.PaymentBean;
import com.bank.entity.Cards;
import com.bank.service.CardService;

@Controller
public class PaymentController {
	@Autowired
	CardService service;

	@RequestMapping("process.bank")
	public String processPayment(@RequestParam("amount") Double amount,@RequestParam("card") String card,
			@RequestParam("pin") String pin,@RequestParam("upi") String accno,PaymentBean bean, Map model, HttpServletRequest request) {
	bean.setAmount(amount);
	bean.setCardNo(card);
	bean.setPin(pin);
	bean.setUpi(accno);
	
		model.put("Pay", bean);
		String referer = request.getHeader("referer");
		String addr = request.getRemoteAddr();
		
		int lisl = referer.lastIndexOf("/");
		String trimmed = referer.substring(0, lisl + 1);
		String target = trimmed.replace("localhost", addr);
		
		
		
		
		Cards cards=new Cards();
		cards=service.checkCardInfo(bean);
		if(cards!=null) {
			String kyahua=service.doTransaction(cards,bean);
			String status=kyahua;
			model.put("Info", status);
			model.put("Value", status);		
		}
		else {
			//return "confirm"; // uske saath "status" bhej
			model.put("Info", "invalid");
			model.put("Value", "invalid");	
		}
		model.put("Target", target);
		
		// System.out.println(Url);
		return "confirmtest";
	}
}
