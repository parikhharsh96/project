package com.bank.repository;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bank.bean.PayeeBean;
import com.bank.bean.RegisterPayeeBean;
import com.bank.bean.UpiBean;
import com.bank.entity.Accounts;
import com.bank.entity.Payee;
import com.bank.entity.Transactions;




@Repository
public class FundTransferRepoImpl implements FundTransferRepo {

	public FundTransferRepoImpl() {
	}

	@Autowired
	private SessionFactory factory;
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<Payee> getRegisteredPayee(Accounts user1) {
		Session session=factory.getCurrentSession();
		
		Accounts user=(Accounts) session.get(Accounts.class, user1.getAccountNo());
		List<Payee> payees=new ArrayList(user.getPayee());
	
		return payees;
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean transferPayee(String payer,String payee,double amount) {
		
		System.out.println("in repo");

		Session session = factory.getCurrentSession();
		Accounts payerAccount = (Accounts) session.get(Accounts.class, payer);
		
		System.out.println(payerAccount.getAccountHolderName());

		if (payerAccount.getBalance() > amount)
		{
			payerAccount.setBalance(payerAccount.getBalance() - amount);
			Accounts payeeAccount = (Accounts) session.get(Accounts.class,payee);
			payeeAccount.setBalance((payeeAccount.getBalance() + amount));

			// creating a date format and then using it to get the current date and time in
			// the
			// specified format and storing it in the timestamp

			Date date = new Date();
			System.out.println(date);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String s1 = formatter.format(date);
			Transactions tx1 = new Transactions();
			try {

				tx1.setTimestamp(formatter.parse(s1));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			tx1.setAccounts(payerAccount);
			tx1.setAmount(amount);
			tx1.setBalance(payerAccount.getBalance());
			tx1.setTransactionType("debit");
			session.save(tx1);

			Transactions tx2 = new Transactions();

			try {
				tx2.setTimestamp(formatter.parse(s1));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tx2.setAccounts(payeeAccount);
			tx2.setAmount(amount);
			tx2.setBalance(payeeAccount.getBalance());
			tx2.setTransactionType("credit");
			session.save(tx2);

			session.merge(payerAccount);
			session.merge(payeeAccount);
			
			return true;
		}
		
		else
		{
			return false;
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts getUpiHolder(UpiBean upiBean) {
		Session session = factory.getCurrentSession();
		Accounts payerAccount = (Accounts) session.bySimpleNaturalId(Accounts.class).load(upiBean.getPayerUpi());
		return payerAccount;
		
	}
	
	
	

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean transferUpi(UpiBean upiBean) {
		Session session = factory.getCurrentSession();

		
		Accounts payerAccount = (Accounts) session.bySimpleNaturalId(Accounts.class).load(upiBean.getPayerUpi());
		if (payerAccount.getBalance() > upiBean.getAmount()) {
			
			payerAccount.setBalance((payerAccount.getBalance() - upiBean.getAmount()));
			Accounts payeeAccount = (Accounts) session.bySimpleNaturalId(Accounts.class).load(upiBean.getPayeeUpi());
		payeeAccount.setBalance((payeeAccount.getBalance() + upiBean.getAmount()));

		

		session.save(payerAccount);
		session.save(payeeAccount);

		Date date = new Date();
		System.out.println(date);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String s1 = formatter.format(date);
		System.out.println("byupi");
		Transactions tx1 = new Transactions();
		try {
			tx1.setTimestamp(formatter.parse(s1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tx1.setAccounts(payerAccount);
		tx1.setAmount(upiBean.getAmount());
		tx1.setBalance(payerAccount.getBalance());
		tx1.setTransactionType("debit");
		session.save(tx1);

		Transactions tx2 = new Transactions();
		try {
			tx2.setTimestamp(formatter.parse(s1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tx2.setAccounts(payeeAccount);
		tx2.setAmount(upiBean.getAmount());
		tx2.setBalance(payeeAccount.getBalance());
		tx2.setTransactionType("credit");
		session.save(tx2);

		session.merge(payerAccount);
		session.merge(payeeAccount);
		
		return true;
		}
		
		else
		{
			return false;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean registerPayee(RegisterPayeeBean registerPayeeBean) {
		
		
		System.out.println("in repo");
		Session session = factory.getCurrentSession();
		Accounts payer= (Accounts) session.get(Accounts.class, registerPayeeBean.getPayerAccountNo());
		Accounts payee=(Accounts) session.get(Accounts.class, registerPayeeBean.getPayeeAccountNo());
		
		
		List<Payee> payees=new ArrayList(payer.getPayee());
		System.out.println("reached here");
		Payee newPayee=new Payee();
		
		newPayee.setPayee(payee);
		newPayee.setPayer(payer);
		
	
		
		
		SQLQuery sql = (SQLQuery) session.createSQLQuery("select * from registered_payee where payee_ac_no=:payee and payer_ac_no=:payer");
		sql.addEntity(Payee.class);
		sql.setParameter("payee", registerPayeeBean.getPayeeAccountNo());
		sql.setParameter("payer", registerPayeeBean.getPayerAccountNo());
		 
		if(sql.uniqueResult()!=null)
		{
			return false;
			
		}
		
		else
		{
		
		Payee payeeNew = new Payee();
		
		payeeNew.setPayer(payer);
		payeeNew.setPayee(payee);
		payeeNew.setPayeeName(registerPayeeBean.getPayeeName());
		
		session.save(payeeNew);
		
		return true;
		}

	}

}
