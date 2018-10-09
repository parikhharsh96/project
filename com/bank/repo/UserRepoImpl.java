package com.bank.repo;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bank.bean.LoginBean;
import com.bank.entity.Aadhaar;
import com.bank.entity.Accounts;
import com.bank.entity.FixedDeposit;

/**
 * @author Ratan Boddu
 * Version 1.0
 */
@Repository
public class UserRepoImpl implements UserRepo {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Accounts authenticate(LoginBean login) {

		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Accounts where email=:eml and password=:pwd");
		hql.setParameter("eml", login.getEmail());
		hql.setParameter("pwd", login.getPassword());
		// hql.setParameter("accno", login.getAccountNumber());
		return (Accounts) hql.uniqueResult();

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Aadhaar checkAadhaar(String aadhaarNo) {

		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Aadhaar where aadhaar_no=:aadhr");
		hql.setParameter("aadhr", aadhaarNo);
		return (Aadhaar) hql.uniqueResult();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Aadhaar checkPan(String PanrNo) {
		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Aadhaar where pan_no=:panno");
		hql.setParameter("panno", PanrNo);
		return (Aadhaar) hql.uniqueResult();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts checkAccAadhaar(String aadhaarNo) {

		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Accounts where aadhaar_no=:aadhr");
		hql.setParameter("aadhr", aadhaarNo);
		return (Accounts) hql.uniqueResult();
	}


	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String createAccount(Accounts account) {

		Session session = sessionFactory.getCurrentSession();
		session.save(account);
		return account.getAccountNo();

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Aadhaar fetchAadhaarDetails(Aadhaar aadhaar) {
		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Aadhaar where aadhaar_no=:aadhr");
		hql.setParameter("aadhr", aadhaar.getAadhaarNo());
		return (Aadhaar) hql.uniqueResult();
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void changePassword(Accounts accounts) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery("UPDATE accounts SET Password = '"+ accounts.getPassword() +"' WHERE account_no = '"+ accounts.getAccountNo() + "'");
		sqlQuery.executeUpdate();
		//session.save(accounts);
		//return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts checkPassword(Accounts accounts) {
		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Accounts where account_no=:accno and password=:pass");
		hql.setParameter("accno", accounts.getAccountNo());
		hql.setParameter("pass", accounts.getPassword());
		return (Accounts) hql.uniqueResult();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts checkForgotPass(String phoneNo, String email) {
		Session session = sessionFactory.getCurrentSession();
		phoneNo=phoneNo+"@confiance";
		Query hql = session.createQuery("from Accounts where upi=:upino and email=:eml");
		hql.setParameter("upino", phoneNo);
		hql.setParameter("eml", email);
		return (Accounts) hql.uniqueResult();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void commitForgotPass(String phone, String newPass) {
		Session session = sessionFactory.getCurrentSession();
		Accounts accounts=new Accounts();
		Encoder encoder = Base64.getEncoder();
		String encodedPass = encoder.encodeToString(newPass.getBytes());
		accounts.setPassword(encodedPass);
		accounts.setUpi(phone);
		SQLQuery sqlQuery = session.createSQLQuery("UPDATE accounts SET Password = '"+ accounts.getPassword() +"' WHERE upi = '"+ accounts.getUpi() + "'");
		sqlQuery.executeUpdate();
		
	}
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts checkAccountRecursion(String id) {
		Session session = sessionFactory.getCurrentSession();
		Query hql = session.createQuery("from Accounts where account_no=:accno");
		hql.setParameter("accno", id);
		return (Accounts) hql.uniqueResult();
	}
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Accounts checkForFixedDeposit(String accountNo) {
		Session session = sessionFactory.getCurrentSession();
		Accounts account = new Accounts();
		account = (Accounts) session.get(Accounts.class, accountNo);
		
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		java.util.Date uDate = date.getTime();
		java.sql.Date endDate = new java.sql.Date(uDate.getTime());
		System.out.println(endDate);
		
		FixedDeposit fixedDeposit = new FixedDeposit();
		SQLQuery sql1 = session.createSQLQuery("select FDEPOSIT_NO from FIXED_DEPOSIT where ACCOUNT_NO =:currentAccountNo and END_DATE=:currentDate");
		sql1.setParameter("currentAccountNo", account.getAccountNo());
		sql1.setParameter("currentDate", endDate);
		
		String fdNo = (String) sql1.uniqueResult();
		if(fdNo != null) {
			fixedDeposit = (FixedDeposit) session.get(FixedDeposit.class, fdNo);
			System.out.println(fdNo);
			System.out.println("SAME");
			double maturityValue = fixedDeposit.getMaturityValue();
			double balance = account.getBalance();
			double newBalance = balance + maturityValue;
			account.setBalance(newBalance);
			
			System.out.println("Deleted");
			session.delete(fixedDeposit);
			
			session.save(account);
			return account;
			
			
		}else {
		System.out.println("Alert");
		return account;
		}
	}

}
