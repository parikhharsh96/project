package com.bank.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.bean.PaymentBean;
import com.bank.entity.Accounts;
import com.bank.entity.Cards;
import com.bank.repo.CardRepo;
/**
 * @author Ratan Boddu
 * Version 1.0
 */
@Service
public class CardServiceImpl implements CardService {
	@Autowired
	CardRepo repo;


	
	@Override
	public Cards activateCard(String accountNo, String pin) {
		Cards cardCheck=repo.checkCard(accountNo);
		if(cardCheck!=null) {
			return null;
		}
		Accounts acc = new Accounts();
		acc = repo.account(accountNo);
		Cards card=new Cards();
		Encoder encoder = Base64.getEncoder();
		String encodedPass = encoder.encodeToString(pin.getBytes());
		card.setPin(encodedPass);
		card.setAccounts(acc);
		char[] strAccNo = accountNo.toCharArray();
		char[] clone = Arrays.copyOfRange(strAccNo, 8, 12);
		String cardClone = String.copyValueOf(clone);
		String cardNo="324357652412";
		cardNo = cardNo + cardClone;
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		Format f = new SimpleDateFormat("MM/yy");
		date.add(Calendar.YEAR, 2);
		card.setCardNo(cardNo);
		String expiryDate = f.format(date.getTime());
		card.setExpiry(expiryDate);
		repo.add(card);
		return card;
		
	}



	@Override
	public Cards checkCardInfo(PaymentBean bean) {
		Encoder encoder = Base64.getEncoder();
		String encodedPass = encoder.encodeToString(bean.getPin().getBytes());
		bean.setPin(encodedPass);
		return repo.checkCardsInfo(bean);
	}



	@Override
	public String doTransaction(Cards cards, PaymentBean bean) {
		
		return repo.doTransaction(cards,bean);
		
	}



	@Override
	public Cards fetchDetails(String accNo) {
		
		return repo.fetchDetails(accNo);
	}

}
