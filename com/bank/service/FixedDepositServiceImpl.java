package com.bank.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.entity.Accounts;
import com.bank.entity.FixedDeposit;
import com.bank.repo.FixedDepoRepo;

/**
 * @author Pranjali Shirke
 * Version 1.0
 */
@Service
public class FixedDepositServiceImpl implements FixedDepositService {

	@Autowired
	private FixedDepoRepo fixedDepoRepo;

	@Override
	public double calculateMaturityValue(double principalAmount, double numberOfPeriod) {

		// 0.06 denotes 6 % rate of interest and 1 denotes Annually Interest
		return principalAmount * Math.pow((1 + (0.06 / 1)), 1 * numberOfPeriod);
	}

	@Override
	public java.sql.Date endDateValue(int year) {

		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		date.add(Calendar.YEAR, year);
		date.add(Calendar.DATE, -1);

		java.util.Date uDate = date.getTime();
		java.sql.Date endDate = new java.sql.Date(uDate.getTime());

		return endDate;

	}
	
	@Override
	public String generateFixedDepositNumber(String initialNumber) {
		
		// Need recursion to check if already exists
		FixedDeposit fixedDeposit = new FixedDeposit();
		Random random = new Random();
		String fixedDepositid = "65409800";
		/*fixedDepositid = fixedDepositid + String.format("%04d", random.nextInt(10000));*/
		//return fixedDepositid + String.format("%04d", random.nextInt(10000));
		
		fixedDepositid = fixedDepositid + String.format("%04d", random.nextInt(10000));
		try {
			if (fixedDepoRepo.checkFdAccountRecursion(fixedDepositid) == null) {
				return fixedDepositid;
			
			} else {
				generateFixedDepositNumber("65409800");
			}
				
		} catch (NullPointerException e) {
			generateFixedDepositNumber("65409800");
		}
		return fixedDepositid;
		
		
	}
	@Override
	public Accounts checkForFixedDeposit(String accountNo) {
		// TODO Auto-generated method stub
		return fixedDepoRepo.checkForFixedDeposit(accountNo);
	
	}

	@Override
	public void createFdAccount(FixedDeposit depositAccount,String accountNo) {
		fixedDepoRepo.createFdAccount(depositAccount,accountNo);
	}

	@Override
	public Accounts getAccountNo(String accountNo) {
		return fixedDepoRepo.getAccountNo(accountNo);
	}

	@Override
	public List<FixedDeposit> displayFixedDeposits(String accountNo) {
		return fixedDepoRepo.displayFixedDeposits(accountNo);
	}
	
	@Override
	public Accounts checkFdAccountRecursion(String id) {
		return fixedDepoRepo.checkFdAccountRecursion(id);
	}

	@Override
	public double preMatureInterest(String deleteMe, String accountNo){
		
		return fixedDepoRepo.preMatureInterest(deleteMe ,accountNo);
	}
	
	

}
