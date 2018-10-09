package com.bank.service;

import java.time.LocalDate;
import java.util.List;

import com.bank.entity.Transactions;

/**
 * @author Shalini Pereira
 * Version 1.0
 */
public interface StatementService {

	public List<Transactions> loadStatement(String accountNo, LocalDate startDate, LocalDate endDate);
	
	public LocalDate dateFormat(String date);
	
	public List balanceFormat(List<Transactions> stmt);
	
	
		
}
