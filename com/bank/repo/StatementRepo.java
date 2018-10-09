package com.bank.repo;

import java.time.LocalDate;
import java.util.List;

import com.bank.entity.Transactions;

/**
 * @author Shalini Pereira
 * Version 1.0
 */
public interface StatementRepo {

	List<Transactions> loadStatement(String accountNo, LocalDate startDate, LocalDate endDate);
}
