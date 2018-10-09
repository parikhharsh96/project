package com.bank.service;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.bean.LoginBean;
import com.bank.entity.Aadhaar;
import com.bank.entity.Accounts;
import com.bank.repo.UserRepo;

/**
 * @author Ratan Boddu 
 * Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo repo;

	@Override
	public Accounts authenticate(LoginBean login) {

		Encoder encoder = Base64.getEncoder();
		String encodedPass = encoder.encodeToString(login.getPassword().getBytes());
		login.setPassword(encodedPass);

		return repo.authenticate(login);
	}

	@Override
	public Aadhaar checkAadhaar(String aadhaarNo) {
		return repo.checkAadhaar(aadhaarNo);
	}

	@Override
	public Aadhaar checkPan(String PanrNo) {
		return repo.checkPan(PanrNo);
	}

	@Override
	public Accounts checkAccAadhaar(String aadhaarNo) {
		return repo.checkAccAadhaar(aadhaarNo);
	}

	@Override
	public String createAccount(String panNo, String email, String pass, String aadhaarNo) {

		try {
			Accounts acc = new Accounts();
			Aadhaar aadhaar = repo.checkAadhaar(aadhaarNo);
			String check = aadhaar.getAadhaarNo();
			Aadhaar aadhaarPan = repo.checkPan(panNo);
			String checkTwo = aadhaarPan.getAadhaarNo();
			if (check.equals(checkTwo)) {

				acc.setAadhaar(aadhaar);
				acc.setAccountHolderName(aadhaar.getName());
				acc.setBalance(0);
				acc.setEmail(email);
				Encoder encoder = Base64.getEncoder();
				String encodedPass = encoder.encodeToString(pass.getBytes());
				acc.setPassword(encodedPass);

				String endUpi = aadhaar.getPhoneNo() + "@confiance";
				acc.setUpi(endUpi);

				// Need recursion to check if already exists

				/*
				 * Random random = new Random(); String id = "12345678"; id = id +
				 * String.format("%04d", random.nextInt(10000));
				 */
				String id = generateAccNo("54123687");
				acc.setAccountNo(id);

				// acc.setAccountNo(id);
				/*
				 * String id = generateAccNo("12345678"); acc.setAccountNo(id);
				 */
				// String idCheck=generateAccountNumber();
				/*
				 * if(repo.checkAccountRecursion(id)==null) { acc.setAccountNo(id); }else {
				 * 
				 * }
				 */

				Calendar date = Calendar.getInstance();
				date.setTime(new Date());
				// Format f = new SimpleDateFormat("dd/MM/yy");
				// String open = f.format(date.getTime());
				java.util.Date dateUtil = date.getTime();
				java.sql.Date open = new java.sql.Date(dateUtil.getTime());
				acc.setOpeningDate(open);
				return repo.createAccount(acc);
				// return "User exists";

			} else {

				return "User does not exist";
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			return "invalid";
		}

	}

	@Override
	public Aadhaar fetchAadhaarDetails(Aadhaar aadhaar) {
		return repo.fetchAadhaarDetails(aadhaar);
	}

	@Override
	public void changePassword(Accounts accounts) {
		Encoder encoder = Base64.getEncoder();
		String encodedPass = encoder.encodeToString(accounts.getPassword().getBytes());
		accounts.setPassword(encodedPass);
		repo.changePassword(accounts);

	}

	@Override
	public Accounts checkPassword(Accounts account) {
		Encoder encoder = Base64.getEncoder();
		String encodedPass = encoder.encodeToString(account.getPassword().getBytes());
		account.setPassword(encodedPass);
		return repo.checkPassword(account);
		// return null;
	}

	@Override
	public Accounts checkForgotPass(String phoneNo, String email) {

		return repo.checkForgotPass(phoneNo, email);
	}

	@Override
	public void commitForgotPass(String phone, String newPass) {
		repo.commitForgotPass(phone, newPass);
	}

	@Override
	public String generateAccNo(String string) {
		Random random = new Random();
		String id = "12345678";
		id = id + String.format("%04d", random.nextInt(10000));
		try {
			if (repo.checkAccountRecursion(id) == null) {
				return id;
			
			} else {
				generateAccNo("12345678");
			}
				
		} catch (NullPointerException e) {
			generateAccNo("12345678");
		}
		return id;
		
	}
	@Override
	public Accounts checkForFixedDeposit(String accountNo) {
		// TODO Auto-generated method stub
		return repo.checkForFixedDeposit(accountNo);
	
	}
}
