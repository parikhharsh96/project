package com.bank.ctrl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.entity.Accounts;
import com.bank.entity.Transactions;
import com.bank.service.StatementService;

/**
 * @author Shalini Pereira 
 * Version 1.0
 */

@Controller
public class StatementController {

	@Autowired
	private StatementService service;

	@RequestMapping(value = "createstmt.bank", method = RequestMethod.GET)
	public String generateStatement(HttpServletRequest request, @RequestParam("start_date") String sDate,
			@RequestParam("end_date") String eDate, Map model, HttpSession session) {
		Accounts user = (Accounts) session.getAttribute("USER");
		String accountNo = user.getAccountNo();
		request.setAttribute("Start", sDate);
		request.setAttribute("End", eDate);

		LocalDate startDate = service.dateFormat(sDate);
		LocalDate endDate = service.dateFormat(eDate);

		List<Transactions> stmt = service.loadStatement(accountNo, startDate, endDate);
		Collections.sort(stmt);

		System.out.println(startDate);
		List bal = service.balanceFormat(stmt);
		if (stmt.isEmpty()) {
			model.put("Accno", accountNo);
			return "nostmt";
		} else {
			model.put("Accno", accountNo);
			model.put("start", startDate);
			model.put("end", endDate);
			model.put("Statement", stmt);
			model.put("Balance", bal);
			return "displaystmt";
		}
	}

	@RequestMapping(value = "stmtpdf.bank", method = RequestMethod.GET)
	public String downloadPDF(HttpServletRequest request, Map model, @RequestParam("start_date") String sDate,
			@RequestParam("end_date") String eDate, HttpSession session) {

		Accounts user = (Accounts) session.getAttribute("USER");
		String accountNo = user.getAccountNo();
		LocalDate startDate = service.dateFormat(sDate);
		LocalDate endDate = service.dateFormat(eDate);

		List<Transactions> stmt = service.loadStatement(accountNo, startDate, endDate);
		Collections.sort(stmt);
		List bal = service.balanceFormat(stmt);
		String filename = null;
		try {
			FileUploadQR file = new FileUploadQR();
			filename = file.genPdf(stmt, bal, accountNo, sDate, eDate);
			String qrFilePath = file.create(filename, accountNo);
			
			Thread.sleep(4000);
			System.out.println(qrFilePath);
			model.put("qr_path", qrFilePath);
			model.put("Filename", filename);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "stmtpdf";
	}

}
