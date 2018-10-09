package com.bank.repo;

import com.bank.bean.PaymentBean;
import com.bank.entity.Accounts;
import com.bank.entity.Cards;

/**
 * @author Ratan Boddu
 * Version 1.0
 */
public interface CardRepo {
	public void add(Cards card);

	public Accounts account(String accountNo);

	public Cards checkCardsInfo(PaymentBean bean);

	public String doTransaction(Cards cards, PaymentBean bean);

	public Cards checkCard(String accountNo);

	public Cards fetchDetails(String accNo);
}
