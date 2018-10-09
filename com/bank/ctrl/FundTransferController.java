package com.bank.ctrl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.bean.RegisterPayeeBean;
import com.bank.bean.UpiBean;
import com.bank.entity.Accounts;
import com.bank.entity.Payee;
import com.bank.service.FundTransferService;

/**
 * @author Harsh Parikh 
 * Version 1.0
 */
@Controller
public class FundTransferController {

	@Autowired
	private FundTransferService service;

	@RequestMapping(value = "showRegisteredPayee.bank", method = RequestMethod.GET)
	public String showRegisteredPayee(Map model, HttpSession session) {
		Accounts user = (Accounts) session.getAttribute("USER");

		if (service.getRegisteredPayee(user) != null) {

			List<Payee> payees = service.getRegisteredPayee(user);

			model.put("Payees", payees);
			/* model.put("StringPayees", stringPayees); */
			System.out.println("leaving controller");
			return "registeredpayee";
		} else {
			model.put("message", "You don't have any registered payees, kindly add some!!");
			return "NewFile";
		}

	}

	@RequestMapping(value = "fundTransfer.bank", method = RequestMethod.GET)
	public String transferPayee(@RequestParam("payeeSrNo") int payeeSrNo, @RequestParam("amount") double amount,
			@RequestParam("message") String message, Map model, HttpSession session) {
		Accounts user = (Accounts) session.getAttribute("USER");
		System.out.println("in controller");
		String payerAccountNumber = user.getAccountNo();

		System.out.println(payeeSrNo);
		System.out.println(amount);
		if (service.transferPayee(payerAccountNumber, payeeSrNo, amount, message)) {
			Accounts accountCheck = service.getNewAccountInfo(payerAccountNumber);
			//set session
			session.setAttribute("USER", accountCheck);
			return "displayfundtransferresults";
		} else {
			model.put("message", "Fund transfer failed due to insufficient funds!!");

			return "registeredpayee";
		}

	}

	@RequestMapping(value = "getUpi.bank", method = RequestMethod.GET)
	public String getUpi(@RequestParam("payeeUpi") String payeeUpi, Map model, HttpSession session) {

		Accounts user = (Accounts) session.getAttribute("USER");
		String payerUpi = user.getUpi();

		if (payerUpi.equals(payeeUpi)) {
			model.put("message", "You have entered your own upi!!");
			return "enterupi";
		}

		else {
			UpiBean upiBean = new UpiBean();
			upiBean.setPayeeUpi(payeeUpi);
			upiBean.setPayerUpi(payerUpi);
			Accounts payeeUpiHolder = service.getUpiHolder(upiBean);
			if (payeeUpiHolder != null) {
				System.out.println(payeeUpiHolder.getAccountHolderName());
				model.put("Payee", payeeUpiHolder);
				return "displayupi";
			} else {
				model.put("message", "No such user exists with the entered upi!!");
				return "enterupi";
			}

		}
	}

	@RequestMapping(value = "transferUpi.bank", method = RequestMethod.GET)
	public String transferUpi(@RequestParam("payeeUpi") String payeeUpi, @RequestParam("amount") double amount,
			Map model, HttpSession session) {
		Accounts user = (Accounts) session.getAttribute("USER");
		String payerUpi = user.getUpi();

		UpiBean upiBean = new UpiBean();
		upiBean.setAmount(amount);
		upiBean.setPayeeUpi(payeeUpi);
		upiBean.setPayerUpi(payerUpi);

		System.out.println(upiBean.getPayeeUpi());
		if (service.transferUpi(upiBean)) {
			System.out.println("success");
			Accounts accountCheck = service.getNewAccountInfoUpi(payerUpi);
			//session set
			session.setAttribute("USER", accountCheck);
			return "displayfundtransferresults";
		} else {
			model.put("message", "Not successful due to insufficient funds!!");
			System.out.println("not successful");
			return "enterupi";
		}

	}

	@RequestMapping(value = "checkIfPayeeExists.bank", method = RequestMethod.GET)
	public String checkIfPayeeExists(@RequestParam("payeeAccountNo") String payeeAccountNo, Map model,
			HttpSession session) {
		RegisterPayeeBean registerPayeeBean = new RegisterPayeeBean();
		registerPayeeBean.setPayeeAccountNo(payeeAccountNo);
		if (service.checkIfPayeeExists(registerPayeeBean) != null) {
			model.put("Payee", service.checkIfPayeeExists(registerPayeeBean));
			return "registerpayee";

		} else {
			model.put("message", "Payee does not exist!!");

			return "addPayee";
		}

	}

	@RequestMapping(value = "registerPayee.bank", method = RequestMethod.GET)
	public String registerPayee(@RequestParam("payeeAccountNo") String payeeAccountNo, Map model, HttpSession session) {

		Accounts user = (Accounts) session.getAttribute("USER");
		String payerAccountNo = user.getAccountNo();
		RegisterPayeeBean registerPayeeBean = new RegisterPayeeBean();
		registerPayeeBean.setPayeeAccountNo(payeeAccountNo);
		registerPayeeBean.setPayerAccountNo(payerAccountNo);
		if (service.registerPayee(registerPayeeBean)) {
			return "displayfundtransferresults";
		} else {
			model.put("message", "Payee already registered.!");
			return "addPayee";
		}

	}

	@RequestMapping(value = "viewCurrencyConversionRates.bank", method = RequestMethod.GET)
	public String viewCurrencyConversionRates(Map model, HttpSession session) {
		String inline = "";

		try {
			URL url = new URL("http://free.currencyconverterapi.com/api/v5/convert?q=USD_INR&compact=y");
			// Parse URL into HttpURLConnection in order to open the connection in order to
			// get the JSON data
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// Set the request to GET or POST as per the requirements
			conn.setRequestMethod("GET");
			// Use the connect method to create the connection bridge
			conn.connect();
			// Get the response status of the Rest API
			int responsecode = conn.getResponseCode();
			System.out.println("Response code is: " + responsecode);

			if (responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " + responsecode);
			else {
				// Scanner functionality will read the JSON data from the stream
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					inline += sc.nextLine();
				}
				System.out.println("\nJSON Response in String format");
				System.out.println(inline);
				// Close the stream when reading the data has been finished
				sc.close();

			}

			String string = inline;

			String[] splitted = string.split(":");
			String splitted3 = splitted[2];
			System.out.println(splitted3);

			String currency = splitted3.replaceAll("}", "");

			System.out.println("this" + currency);

			double d = Double.parseDouble(currency);

			System.out.println(d);

			model.put("Currency", currency);

			// Disconnect the HttpURLConnection stream
			conn.disconnect();

			return "viewcurrencyconverion";

		}

		catch (Exception e) {
			e.printStackTrace();

			return "viewcurrencyconverion";
		}

	}

}
