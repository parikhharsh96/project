package com.bank.ctrl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.entity.Accounts;
import com.bank.entity.Cards;
import com.bank.service.CardService;

/**
 * @author Ratan Boddu
 * Version 1.0
 */
@Controller
public class CardController {
	@Autowired
	private CardService service;
	@RequestMapping(value = "gencard.bank")
	public String activate(@RequestParam("pin") String pin, Map model,HttpSession session) {
		Accounts user = (Accounts) session.getAttribute("USER");
		try {
			Cards card=service.activateCard(user.getAccountNo(), pin);
			String card1=card.getCardNo().substring(0, 4);
			String card2=card.getCardNo().substring(4, 8);
			String card3=card.getCardNo().substring(8, 12);
			String card4=card.getCardNo().substring(12, 16);
			model.put("CardNo1", card1);
			model.put("CardNo2", card2);
			model.put("CardNo3", card3);
			model.put("CardNo4", card4);
			
			model.put("Name", user.getAccountHolderName());
			model.put("Date", card.getExpiry());
			return "cardcreated";
		} catch (NullPointerException e) {
			model.put("Info", "Card already activated !");
			return "virtualcard";
		}
		
	}
	/*@RequestMapping(value = "viewcardcreated.bank",method = RequestMethod.POST)
	public String viewCard(@RequestParam("accNo") String accNo, Map model,HttpSession session) {
		Accounts user = (Accounts) session.getAttribute("USER");
		Cards cardDetails = service.fetchDetails(accNo);
		String card1=cardDetails.getCardNo().substring(0, 4);
		String card2=cardDetails.getCardNo().substring(4, 8);
		String card3=cardDetails.getCardNo().substring(8, 12);
		String card4=cardDetails.getCardNo().substring(12, 16);
		model.put("CardNo1", card1);
		model.put("CardNo2", card2);
		model.put("CardNo3", card3);
		model.put("CardNo4", card4);
		model.put("Name", user.getAccountHolderName());
		model.put("Date", cardDetails.getExpiry());
		return "viewcardcreated";
	
		
		
	}
	*/

}
