package com.bank.service;

import com.bank.bean.PaymentBean;
import com.bank.entity.Cards;

/**
 * @author Ratan Boddu
 * Version 1.0
 */
public interface CardService {
	
	Cards activateCard(String accountNo, String pin);

	Cards checkCardInfo(PaymentBean bean);

	String doTransaction(Cards cards, PaymentBean bean);

	Cards fetchDetails(String accNo);
	
}
