package com.bank.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.entity.Transactions;
import com.bank.repo.StatementRepo;
/**
 * @author Shalini Pereira
 * Version 1.0
 */
@Service
public class StatementServiceImpl implements StatementService {

	@Autowired
	private StatementRepo stmtRepo;

	@Override
	public List<Transactions> loadStatement(String accountNo, LocalDate startDate, LocalDate endDate) {
		List<Transactions> stmt = stmtRepo.loadStatement(accountNo, startDate, endDate);

		List<Transactions> stmt1 = new ArrayList<Transactions>();
		for (Transactions t : stmt) {
			LocalDate localDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(t.getTimestamp()));
			if (localDate.isAfter(startDate) && localDate.isBefore(endDate)) {
				stmt1.add(t);
			}
			if (localDate.isEqual(endDate)) {
				stmt1.add(t);
			}
		}
		return stmt1;
	}

	@Override
	public List balanceFormat(List<Transactions> stmt) {

		List list = new ArrayList();
		for (Transactions t : stmt) {
			double balance = t.getBalance();
			long bal = Double.valueOf(balance).longValue();
			list.add(bal);

		}
		return list;
	}

	@Override
	public LocalDate dateFormat(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return localDate;
	}

}
