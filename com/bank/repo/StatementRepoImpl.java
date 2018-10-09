package com.bank.repo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bank.entity.Accounts;
import com.bank.entity.Transactions;

/**
 * @author Shalini Pereira
 * Version 1.0
 */
@Repository
public class StatementRepoImpl implements StatementRepo {
	@Autowired
	private SessionFactory factory;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<Transactions> loadStatement(String accountNo, LocalDate startDate, LocalDate endDate) {
		Session session = factory.getCurrentSession();
		
		Accounts acnts = (Accounts) session.get(Accounts.class, accountNo);
		List<Transactions> stmt = new ArrayList<Transactions>(acnts.getTransactions());
		
		return stmt;
	}

}
