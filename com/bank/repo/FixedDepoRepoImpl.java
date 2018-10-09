package com.bank.repo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.time.temporal.ChronoUnit;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bank.entity.Accounts;
import com.bank.entity.FixedDeposit;
import com.bank.entity.Transactions;

/**
 * @author Pranjali Shirke
 * Version 1.0
 */
@Repository
public class FixedDepoRepoImpl implements FixedDepoRepo {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void createFdAccount(FixedDeposit depositAccount,String accountNo) {
		Session session = sessionFactory.getCurrentSession();
		Accounts accounts = new Accounts();
		//if(accounts.getBalance() > )
		long millis=System.currentTimeMillis();
		Date endDate=new java.sql.Date(millis);
		session.save(depositAccount);
		Accounts account=(Accounts) session.get(Accounts.class, accountNo);
		System.out.println(account.getAccountHolderName());
		
		Transactions tx1=new Transactions();
		tx1.setAccounts(account);
		tx1.setAmount(depositAccount.getPrincipalAmount());
		System.out.println(depositAccount.getPrincipalAmount());
		account.setBalance(account.getBalance()- depositAccount.getPrincipalAmount());
		System.out.println(account.getBalance()- depositAccount.getPrincipalAmount());
		tx1.setBalance(account.getBalance());
		//System.out.println(account.getBalance()- depositAccount.getPrincipalAmount());
		tx1.setTimestamp(endDate);
		
		tx1.setTransactionType("DB");
		tx1.setMessage("Fixed Deposit");
		session.save(tx1);
		session.save(account);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts getAccountNo(String accountNo) {
		Session session = sessionFactory.getCurrentSession();
		// To fetch account no from Accounts Entity
		Accounts account = (Accounts) session.load(Accounts.class, accountNo);
		return account;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<FixedDeposit> displayFixedDeposits(String accountNo) {
		Session session = sessionFactory.getCurrentSession();
		Accounts account = (Accounts) session.get(Accounts.class, accountNo);

		List<FixedDeposit> fixedDeposit = new ArrayList<FixedDeposit>(account.getFixedDeposit());

		return fixedDeposit;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts checkFdAccountRecursion(String id) {
		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Accounts where account_no=:accno");
		hql.setParameter("accno", id);
		return (Accounts) hql.uniqueResult();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public double preMatureInterest(String deleteMe, String accountNo) {
		Session session = sessionFactory.getCurrentSession();
		double prematureInterest = 0;
		
		FixedDeposit fixedDeposit = (FixedDeposit) session.get(FixedDeposit.class, deleteMe);	
		
		java.sql.Date abc=new java.sql.Date(fixedDeposit.getStartDate().getTime());
	
		LocalDate sdate= abc.toLocalDate();
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		java.util.Date uDate = date.getTime();
		java.sql.Date abc1=new java.sql.Date(uDate.getTime());;
		LocalDate edate= abc1.toLocalDate();

		//long monthsBetween =  ChronoUnit.MONTHS.between(sdate, edate);
		long yearsBetween = ChronoUnit.YEARS.between(sdate, edate);
		
		if(yearsBetween < 1) {
			
			double balance=  fixedDeposit.getPrincipalAmount();
			System.out.println(balance);
			System.out.println(yearsBetween);
			//Accounts user= new Accounts();
			Accounts accountNumber = (Accounts) session.get(Accounts.class, accountNo);
			
			accountNumber.setBalance(accountNumber.getBalance()+balance);
			SQLQuery sqlQuery = session.createSQLQuery("UPDATE accounts SET balance = '"+ accountNumber.getBalance() +"' WHERE account_no = '"+ accountNo + "'");
			sqlQuery.executeUpdate();
			
			Transactions tx1=new Transactions();
			tx1.setAccounts(accountNumber);
			tx1.setAmount(fixedDeposit.getPrincipalAmount());
			tx1.setBalance(accountNumber.getBalance());
			
			tx1.setTimestamp(abc1);
			
			tx1.setTransactionType("CR");
			tx1.setMessage("Fixed Deposit");
			session.save(tx1);
			session.save(accountNumber);	
			
			session.delete(fixedDeposit);
			return balance;
		}else {
			
		prematureInterest = (fixedDeposit.getPrincipalAmount() * Math.pow((1 + (0.03 / 1)), 1 * yearsBetween));
		System.out.println(prematureInterest);
		System.out.println(yearsBetween);
		Accounts accountNumber = (Accounts) session.get(Accounts.class, accountNo);
		
		accountNumber.setBalance(accountNumber.getBalance()+prematureInterest);
		SQLQuery sqlQuery = session.createSQLQuery("UPDATE accounts SET balance = '"+ accountNumber.getBalance() +"' WHERE account_no = '"+ accountNo + "'");
		sqlQuery.executeUpdate();
		
		
		Transactions tx1=new Transactions();
		tx1.setAccounts(accountNumber);
		tx1.setAmount(prematureInterest);
		tx1.setBalance(accountNumber.getBalance());
		tx1.setTimestamp(abc1);
		
		tx1.setTransactionType("CR");
		tx1.setMessage("Fixed Deposit");
		session.save(tx1);
		session.save(accountNumber);	
		
		session.delete(fixedDeposit);
		
		return prematureInterest;
		}
		
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts checkForFixedDeposit(String accountNo) {
		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Accounts where account_no=:accno");
		hql.setParameter("accno", accountNo);
		return (Accounts) hql.uniqueResult();
		
	}
}
