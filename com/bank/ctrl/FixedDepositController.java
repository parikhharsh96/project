package com.bank.ctrl;

import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.bean.FixedDepoBean;
import com.bank.entity.Accounts;
import com.bank.entity.FixedDeposit;
import com.bank.service.FixedDepositService;

/**
 * @author Pranjali Shirke
 * Version 1.0
 */
@Controller
public class FixedDepositController {

	@Autowired
	private FixedDepositService fixedDepoService;

	// For creating fixed deposit for the current account
	@RequestMapping(value = "fixedDeposit.bank", method = RequestMethod.POST)
	public String createFixedDepo(FixedDepoBean fixedDepoBean, Map model, HttpSession session) {

		Accounts account = new Accounts();
		Accounts user = (Accounts) session.getAttribute("USER");
		String accountNo=user.getAccountNo();
		account = fixedDepoService.getAccountNo(user.getAccountNo());
		//System.out.println(fixedDepoBean.getPrincipalAmount());
		
		if(user.getBalance() > fixedDepoBean.getPrincipalAmount()) {
		FixedDeposit fixedDeposit = new FixedDeposit();
		String fixedDepositNumber = fixedDepoService.generateFixedDepositNumber("65409800");
		fixedDeposit.setFixedDepositNo(fixedDepositNumber);
		fixedDeposit.setAccounts(account);
		fixedDeposit.setPrincipalAmount(fixedDepoBean.getPrincipalAmount());
		fixedDeposit.setStartDate(new Date());

		java.sql.Date endDate = fixedDepoService.endDateValue(fixedDepoBean.getTenure());
		fixedDeposit.setEndDate(endDate);

		fixedDeposit.setMaturityValue(
				fixedDepoService.calculateMaturityValue(fixedDepoBean.getPrincipalAmount(), fixedDepoBean.getTenure()));

		fixedDepoService.createFdAccount(fixedDeposit,accountNo);

		Accounts accountCheck = fixedDepoService.checkForFixedDeposit(user.getAccountNo());
		
		model.put("principalAmount", fixedDepoBean.getPrincipalAmount());
		model.put("tenure", fixedDepoBean.getTenure());
		model.put("startDate", fixedDepoBean.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		model.put("endDate", endDate);
		model.put("maturityValue", fixedDepoBean.getMaturityValue());
		model.put("interestEarned", fixedDepoBean.getInterestEarned());
		session.setAttribute("USER", accountCheck);
		return "fixeddepositcreated";
		}
		else {
			model.put("Alert", "Fixed Deposit cannot be created due to Insufficient Balance");
			return "fixeddepositcalculator";
		}
	}

	// For listing all fixed deposits of the current account
	@RequestMapping(value = "fetchFixedDeposits.bank", method = RequestMethod.GET)
	public String getdata(Map model, HttpSession session) {
		Accounts user = (Accounts) session.getAttribute("USER");
		String accountNo = user.getAccountNo();
		List<FixedDeposit> fixedDeposit = fixedDepoService.displayFixedDeposits(accountNo);
		// For sorting
		Collections.sort(fixedDeposit);
		
		if(fixedDeposit.isEmpty())
		{
			model.put("Info", "No fixed deposits yet!");
			return "nodepositsfound";
		}
		else {
		model.put("FD", fixedDeposit);

		return "fetchfixeddeposit";
	}
	}
	
	// For listing all fixed deposits of the current account
		@RequestMapping(value = "prematurefixeddepositdelete.bank", method = RequestMethod.GET)
		public String getdatatodelete(Map model, HttpSession session) {
			Accounts user = (Accounts) session.getAttribute("USER");
			String accountNo = user.getAccountNo();
			List<FixedDeposit> fixedDeposit = fixedDepoService.displayFixedDeposits(accountNo);
			// For sorting
			Collections.sort(fixedDeposit);
			
			if(fixedDeposit.isEmpty())
			{
				model.put("Info", "No fixed deposits yet!");
				return "nodepositsfound";
			}
			else {
				System.out.print("Premature Delete");
			model.put("FD", fixedDeposit);
			return "prematuredelete";
		}
		}
	
	// For deleting fixed deposits prematurely of the current account
		@RequestMapping(value = "deletefd.bank", method = RequestMethod.GET)
		public String deleteFixedDeposits(@RequestParam("deleteMe") String deleteMe,
				Map model, HttpSession session) {
			
			Accounts user = (Accounts) session.getAttribute("USER");
			//String payerAccountNo = user.getAccountNo();
			
			double prematureInterest = fixedDepoService.preMatureInterest(deleteMe, user.getAccountNo()); 
			
			double balance = (user.getBalance()+ prematureInterest);
			user.setBalance(balance);
			Accounts accountCheck = fixedDepoService.checkForFixedDeposit(user.getAccountNo());
			session.setAttribute("USER", accountCheck);
			return "dashboard";
		}

}
