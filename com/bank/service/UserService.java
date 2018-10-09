package com.bank.service;

import com.bank.bean.LoginBean;
import com.bank.entity.Aadhaar;
import com.bank.entity.Accounts;

/**
 * @author Ratan Boddu
 * Version 1.0
 */
public interface UserService {
	void changePassword(Accounts accounts);
	
	Aadhaar fetchAadhaarDetails(Aadhaar aadhaar);

	Accounts authenticate(LoginBean login);

	Aadhaar checkAadhaar(String aadhaarNo);
	
	Accounts checkAccAadhaar(String aadhaarNo);
	
	Aadhaar checkPan(String PanrNo);
	
	String createAccount(String panNo, String email, String pass, String aadhaarNo);

	Accounts checkPassword(Accounts account);

	Accounts checkForgotPass(String phoneNo, String email);

	void commitForgotPass(String phone, String newPass);
	
	String generateAccNo(String string);
	
	Accounts checkForFixedDeposit(String accountNo);

}
