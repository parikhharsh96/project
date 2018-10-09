package com.bank.ctrl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.bean.LoginBean;
import com.bank.entity.Aadhaar;
import com.bank.entity.Accounts;
import com.bank.service.UserService;

/**
 * @author Ratan Boddu 
 * Version 1.0
 */
@Controller
public class UserController {
	@Autowired
	private UserService service;

	@RequestMapping(value = "commitforgotpass.bank", method = RequestMethod.GET)
	public String commitForgot(@RequestParam("phoneUpi") String phone, @RequestParam("newPass") String newPass,
			@RequestParam("confirmNewPass") String confirmNewPass, Map model) {

		service.commitForgotPass(phone, newPass);
		model.put("Info", "Password changed successfully");
		return "login";
	}

	@RequestMapping(value = "checkotpforgotpass.bank", method = RequestMethod.GET)
	public String forgotCheckOtp(@RequestParam("phoneNo") String phone, @RequestParam("genValue") String genValue,
			@RequestParam("entValue") String entValue, Map model) {
		if (genValue.equals(entValue)) {
			model.put("Phone", phone);
			return "changeforgotpass";
		} else {
			model.put("Info", "Invalid OTP");
			model.put("Phone", phone);
			model.put("GenNo", genValue);
			return "forgototpcheck";
		}

	}

	@RequestMapping(value = "forgot.bank", method = RequestMethod.GET)
	public String forgotPassword(@RequestParam("phoneNo") String phoneNo, @RequestParam("emailId") String email,
			Map model) {
		java.math.BigInteger phoneNumber = new java.math.BigInteger(phoneNo);
		try {
			if (service.checkForgotPass(phoneNo, email) != null) {
				try {
					Accounts acc = service.checkForgotPass(phoneNo, email);
					Random rnd = new Random();
					int n = 100000 + rnd.nextInt(900000);
					System.out.println(n);
					// Construct data
					String apiKey = "apikey="
							+ URLEncoder.encode(/*"guO03G1OZq0-rEfYq9ZQvFVZAyiBTRfqZjyV1PYwFn"*/"", "UTF-8");
					String message = "&message=" + URLEncoder.encode("OTP is " + n, "UTF-8");
					String sender = "&sender=" + URLEncoder.encode("TXTLCL", "UTF-8");
					String numbers = "&numbers=" + URLEncoder.encode("91" + phoneNumber, "UTF-8");

					// Send data
					String data = "https://api.textlocal.in/send/?" + apiKey + numbers + message + sender;
					URL url = new URL(data);
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);

					// Get the response
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					String sResult = "";
					while ((line = rd.readLine()) != null) {
						// Process line...
						sResult = sResult + line + " ";
					}
					rd.close();

					model.put("GenNo", n);
					model.put("Phone", acc.getUpi());
					return "forgototpcheck";
				} catch (Exception e) {
					System.out.println("Error SMS " + e);
					return "Error " + e;
				}
			} else {

				model.put("Info", "Invalid details entered");
				return "forgotpassword";
			}

		} catch (NullPointerException e) {

			model.put("Info", "Invalid details entered");
			return "forgotpassword";

		}

	}

	@RequestMapping(value = "checkOtp.bank", method = RequestMethod.GET)
	public String otpChecking(@RequestParam("aadhaarNumber") String aadhaarNo,
			@RequestParam("generatedValue") String genValue, @RequestParam("enteredValue") String entValue, Map model,
			HttpSession session) {

		if (genValue.equals(entValue)) {
			return "accountcreation";
		} else {
			return "register";
		}

	}

	@RequestMapping(value = "createaccount.bank", method = RequestMethod.POST)
	public String createAccount(@RequestParam("aadhaarNumber") String aadhaarNo, @RequestParam("panNo") String panNo,
			@RequestParam("email") String email, @RequestParam("password") String password, Map model,
			HttpSession session) {
		String accno = service.createAccount(panNo, email, password, aadhaarNo);
		if (accno == "invalid") {
			model.put("Info", "Invalid credentials");
			return "accountcreation";
		}
		model.put("Info", accno);
		session.invalidate();
		return "accountcreated";
	}

	@RequestMapping(value = "register.bank", method = RequestMethod.POST)
	public String register(@RequestParam("aadhaarNumber") String aadhaarNo, Map model, HttpSession session) {
		Aadhaar aadhaar = new Aadhaar();
		aadhaar.setAadhaarNo(aadhaarNo);
		aadhaar = service.fetchAadhaarDetails(aadhaar);
		session.setAttribute("AADHAAR", aadhaar);
		try {
			if (service.checkAccAadhaar(aadhaarNo) != null) {
				model.put("Info", "Invalid Aadhaar/Aadhaar already registered with bank");
				return "register";

			} else {
				if (service.checkAadhaar(aadhaarNo) == null) {
					model.put("Info", "Invalid Aadhaar/Aadhaar already registered with bank");
					return "register";
				} else {
					try {
						java.math.BigInteger phoneNumber = new java.math.BigInteger(aadhaar.getPhoneNo());
						Random rnd = new Random();
						int n = 100000 + rnd.nextInt(900000);
						System.out.println(n);
						// Construct data
						String apiKey = "apikey="
								+ URLEncoder.encode(/*"guO03G1OZq0-rEfYq9ZQvFVZAyiBTRfqZjyV1PYwFn"*/"", "UTF-8");
						String message = "&message=" + URLEncoder.encode("OTP is " + n, "UTF-8");
						String sender = "&sender=" + URLEncoder.encode("TXTLCL", "UTF-8");
						String numbers = "&numbers=" + URLEncoder.encode("91" + phoneNumber, "UTF-8");

						// Send data
						String data = "https://api.textlocal.in/send/?" + apiKey + numbers + message + sender;
						URL url = new URL(data);
						URLConnection conn = url.openConnection();
						conn.setDoOutput(true);

						// Get the response
						BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						String line;
						String sResult = "";
						while ((line = rd.readLine()) != null) {
							// Process line...
							sResult = sResult + line + " ";
						}
						rd.close();

						model.put("Info", n);
						return "registerotp";
					} catch (Exception e) {
						System.out.println("Error SMS " + e);
						return "Error " + e;
					}

				}

			}
		} catch (NullPointerException e) {
			model.put("Info", "Invalild Aadhaar/Aadhaar already registered with bank");
			return "index";

		}

	}

	@RequestMapping(value = "login.bank", method = RequestMethod.POST)
	public String checkLogin( LoginBean login, Map model, HttpSession session) {

		Accounts user = service.authenticate(login);
		if (user != null) {
			// Login successful
			System.out.println(user.getAccountNo());
			Accounts account = service.checkForFixedDeposit(user.getAccountNo());

			session.setAttribute("USER", account);
			return "dashboard";
		} else {
			// login failed
			model.put("Info", "Invalid Email ID/Password");
			return "login";
		}

	}

	@RequestMapping(value = "logout.bank")
	public String logout(Map model, HttpSession session) {
		model.put("Info", "Logout successful, Login again!");
		session.removeAttribute("USER");
		session.invalidate();
		return "login";
	}

	@RequestMapping(value = "change.bank", method = RequestMethod.POST)
	public String changePassword(@RequestParam("existingPassword") String existPass,
			@RequestParam("newPassword") String newPass, Map model, HttpSession session) {
		Accounts user = (Accounts) session.getAttribute("USER");
		Accounts account = new Accounts();
		account.setAccountNo(user.getAccountNo());
		account.setPassword(existPass);

		try {
			if (service.checkPassword(account) != null) {

				account.setPassword(newPass);
				service.changePassword(account);
				model.put("Info", "Your password has been successfully changed!");
				return "dashboard";

			} else {
				model.put("Info", "Invalid Data ! Please try again !");
				return "changepassword";
			}
		} catch (NullPointerException e) {
			model.put("Info", "Invalid Data ! Please try again !");
			return "changepassword";
		}

	}
}
