package com.bank.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bank.bean.RegisterPayeeBean;
import com.bank.bean.UpiBean;
import com.bank.entity.Accounts;
import com.bank.entity.Payee;
import com.bank.entity.Transactions;

/**
 * @author Harsh Parikh Version 1.0
 */
@Repository
public class FundTransferRepoImpl implements FundTransferRepo {

	public FundTransferRepoImpl() {
	}

	@Autowired
	private SessionFactory factory;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<Payee> getRegisteredPayee(Accounts user1) {
		try {
			Session session = factory.getCurrentSession();

			Accounts user = (Accounts) session.get(Accounts.class, user1.getAccountNo());
			System.out.println(user.getAccountHolderName());
			Set set = user.getPayee();
			System.out.println(set.size());

			if (set.size() != 0) {
				List<Payee> payees = new ArrayList(user.getPayee());
				System.out.println("leaving repo");
				for (Payee payee : payees) {
					System.out.println(payee);
				}
				return payees;
			} else {
				return null;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;

		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean transferPayee(String payer, int payeeSrNo, double amount, String message) {

		Session session = factory.getCurrentSession();
		Accounts payerAccount = (Accounts) session.get(Accounts.class, payer);

		System.out.println(payerAccount.getAccountHolderName());

		if (payerAccount.getBalance() > amount) {

			System.out.println(payeeSrNo);

			SQLQuery sql = (SQLQuery) session.createSQLQuery("select payee_ac_no from registered_payee where srno=:payee");
			sql.setParameter("payee", payeeSrNo);

			String payee1 = (String) sql.uniqueResult();

			System.out.println(payee1);

			Accounts payeeAccount = (Accounts) session.get(Accounts.class, payee1);

			payerAccount.setBalance(payerAccount.getBalance() - amount);

			payeeAccount.setBalance((payeeAccount.getBalance() + amount));

			long millis = System.currentTimeMillis();
			Date endDate = new java.sql.Date(millis);
			Transactions tx1 = new Transactions();
			tx1.setTimestamp(endDate);
			tx1.setAccounts(payerAccount);
			tx1.setAmount(amount);
			tx1.setBalance(payerAccount.getBalance());
			tx1.setTransactionType("DB");
			tx1.setMessage(message);
			session.save(tx1);

			Transactions tx2 = new Transactions();

			tx2.setTimestamp(endDate);
			tx2.setAccounts(payeeAccount);
			tx2.setAmount(amount);
			tx2.setBalance(payeeAccount.getBalance());
			tx2.setTransactionType("CR");
			session.save(tx2);

			return true;

		}

		else {
			return false;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts getUpiHolder(UpiBean upiBean) {
		Session session = factory.getCurrentSession();
		Accounts payeeAccount = (Accounts) session.bySimpleNaturalId(Accounts.class).load(upiBean.getPayeeUpi());
		if (payeeAccount != null) {
			System.out.println(upiBean.getPayeeUpi());
			System.out.println(payeeAccount.getAccountHolderName());
			return payeeAccount;
		} else {
			return null;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean transferUpi(UpiBean upiBean) {
		Session session = factory.getCurrentSession();

		Accounts payerAccount = (Accounts) session.bySimpleNaturalId(Accounts.class).load(upiBean.getPayerUpi());
		System.out.println(payerAccount.getAccountHolderName());
		if (payerAccount.getBalance() > upiBean.getAmount()) {

			payerAccount.setBalance((payerAccount.getBalance() - upiBean.getAmount()));
			Accounts payeeAccount = (Accounts) session.bySimpleNaturalId(Accounts.class).load(upiBean.getPayeeUpi());
			System.out.println(payeeAccount.getAccountHolderName());
			payeeAccount.setBalance((payeeAccount.getBalance() + upiBean.getAmount()));

			session.save(payerAccount);
			session.save(payeeAccount);

			long millis = System.currentTimeMillis();
			Date endDate = new java.sql.Date(millis);
			Transactions tx1 = new Transactions();
			tx1.setTimestamp(endDate);
			tx1.setAccounts(payerAccount);
			tx1.setAmount(upiBean.getAmount());
			tx1.setBalance(payerAccount.getBalance());
			tx1.setTransactionType("debit");
			session.save(tx1);
			Transactions tx2 = new Transactions();

			tx2.setTimestamp(endDate);
			tx2.setAccounts(payeeAccount);
			tx2.setAmount(upiBean.getAmount());
			tx2.setBalance(payeeAccount.getBalance());
			tx2.setTransactionType("credit");
			session.save(tx2);
		
			return true;
		}

		else {
			return false;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean registerPayee(RegisterPayeeBean registerPayeeBean) {

		Session session = factory.getCurrentSession();
		Accounts payer = (Accounts) session.get(Accounts.class, registerPayeeBean.getPayerAccountNo());
		Accounts payee = (Accounts) session.get(Accounts.class, registerPayeeBean.getPayeeAccountNo());

		List<Payee> payees = new ArrayList(payer.getPayee());
		Payee newPayee = new Payee();

		newPayee.setPayee(payee);
		newPayee.setPayer(payer);

		SQLQuery sql = (SQLQuery) session
				.createSQLQuery("select * from registered_payee where payee_ac_no=:payee and payer_ac_no=:payer");
		sql.addEntity(Payee.class);
		sql.setParameter("payee", registerPayeeBean.getPayeeAccountNo());
		sql.setParameter("payer", registerPayeeBean.getPayerAccountNo());

		if (sql.uniqueResult() != null) {
			return false;

		}

		else {

			Payee payeeNew = new Payee();

			payeeNew.setPayer(payer);
			payeeNew.setPayee(payee);
			payeeNew.setPayeeName(payee.getAccountHolderName());

			session.save(payeeNew);

			return true;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts checkIfPayeeExists(RegisterPayeeBean registerPayeeBean) {
		Session session = factory.getCurrentSession();
		Accounts payee = (Accounts) session.get(Accounts.class, registerPayeeBean.getPayeeAccountNo());

		return payee;

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts getNewAccountInfo(String payerAccountNumber) {
		Session session = factory.getCurrentSession();
		Query hql = session.createQuery("from Accounts where account_no=:accno");
		hql.setParameter("accno", payerAccountNumber);
		return (Accounts) hql.uniqueResult();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts getNewAccountInfoUpi(String payerUpi) {
		Session session = factory.getCurrentSession();
		Query hql = session.createQuery("from Accounts where upi=:upino");
		hql.setParameter("upino", payerUpi);
		return (Accounts) hql.uniqueResult();
	}

}
