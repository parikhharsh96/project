package com.bank.repo;

import com.bank.bean.LoginBean;
import com.bank.entity.Aadhaar;
import com.bank.entity.Accounts;

/**
 * @author Ratan Boddu
 * Version 1.0
 */
public interface UserRepo {
	void changePassword(Accounts accounts);

	Aadhaar fetchAadhaarDetails(Aadhaar aadhaar);

	Accounts authenticate(LoginBean login);

	Aadhaar checkAadhaar(String aadhaarNo);

	Aadhaar checkPan(String PanrNo);

	Accounts checkAccAadhaar(String aadhaarNo);

	String createAccount(Accounts account);

	Accounts checkPassword(Accounts accounts);

	Accounts checkForgotPass(String phoneNo, String email);

	void commitForgotPass(String phone, String newPass);

	Accounts checkAccountRecursion(String id);
	
	Accounts checkForFixedDeposit(String accountNo);
}
