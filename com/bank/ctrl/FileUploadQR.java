package com.bank.ctrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bank.entity.Transactions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class FileUploadQR {

	// referenced libraries zxing-core-2.0.jar
	// zxing-1.7-javase.jar
	// references MultiPartUtility.java
	
	
	public void formatter(PdfPCell type) {
		type.setPaddingLeft(15);
		type.setPaddingRight(15);
		type.setPaddingTop(0);
		type.setPaddingBottom(5);
	}

	
	public void alignmentFormatter(PdfPCell type) {
		type.setColspan(5);
		type.setHorizontalAlignment(Element.ALIGN_CENTER);
		type.setPadding(10.0f);
	}
	
	public String genPdf(List<Transactions> stmt, List bal, String accountNo, String sDate, String eDate) {

		try {
			String filename = "D:\\BankFInal\\Bank\\WebContent\\" + accountNo + ".pdf";
			OutputStream file = new FileOutputStream(new File(filename));

			Document document = new Document();
			PdfWriter.getInstance(document, file);
			document.open();

			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);

			PdfPCell cell = new PdfPCell(new Paragraph("Confiance Bank!!"));
			alignmentFormatter(cell);
			table.addCell(cell);

			PdfPCell cell0 = new PdfPCell(new Paragraph("Account No.: " + accountNo));
			alignmentFormatter(cell0);
			table.addCell(cell0);

			PdfPCell cell1 = new PdfPCell(new Paragraph("Statement from " + sDate + " to " + eDate));
			alignmentFormatter(cell1);
			table.addCell(cell1);

			PdfPCell date = new PdfPCell(new Paragraph("Date"));
			formatter(date);
			table.addCell(date);

			PdfPCell particulars = new PdfPCell(new Paragraph("Transcation Type"));
			formatter(particulars);
			table.addCell(particulars);

			PdfPCell amount = new PdfPCell(new Paragraph("Amount"));
			formatter(amount);
			table.addCell(amount);

			PdfPCell balance = new PdfPCell(new Paragraph("Balance"));
			formatter(balance);
			table.addCell(balance);

			PdfPCell message = new PdfPCell(new Paragraph("Remarks"));
			formatter(message);
			table.addCell(message);
			int i = 0;
			for (Transactions t : stmt) {

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String transactionDate = simpleDateFormat.format(t.getTimestamp());
				PdfPCell timeStamp = new PdfPCell(new Paragraph(transactionDate));
				formatter(timeStamp);
				table.addCell(timeStamp);

				PdfPCell transactionType = new PdfPCell(new Paragraph(t.getTransactionType()));
				formatter(transactionType);
				table.addCell(transactionType);

				PdfPCell getAmount = new PdfPCell(new Paragraph(String.valueOf(t.getAmount())));
				formatter(getAmount);
				table.addCell(getAmount);

				PdfPCell getBalance = new PdfPCell(new Paragraph(String.valueOf(bal.get(i))));
				formatter(getBalance);
				table.addCell(getBalance);

				String msg = t.getMessage();
				System.out.println(msg);
				PdfPCell getMessage;
				if (msg != null && !msg.isEmpty())
					getMessage = new PdfPCell(new Paragraph(msg));
				else
					getMessage = new PdfPCell(new Paragraph("   -  "));
				formatter(getMessage);
				table.addCell(getMessage);
				i++;
			}

			table.setSpacingBefore(30.0f);
			table.setSpacingAfter(30.0f);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);

			document.open();
			document.add(table);

			document.close();

			file.close();

			return accountNo + ".pdf";
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}

	}
	
	public static String create(String inputFilePath, String accountNo) {
		String charset = "UTF-8";

		String qrFilePath = "D:\\BankFInal\\Bank\\WebContent\\" + accountNo + ".png";


		File fileToUpload = new File(inputFilePath);

		String requestURL = "https://uguu.se/api.php?d=upload-tool";

		try {
			MultipartUtility multipart = new MultipartUtility(requestURL, charset);

			multipart.addHeaderField("User-Agent", "CodeJava");
			multipart.addHeaderField("Test-Header", "Header-Value");
			multipart.addFilePart("file", fileToUpload);
			List<String> response = multipart.finish();
			System.out.println("SERVER REPLIED :");

			String uploadedUrl = response.get(0);

			System.out.println(uploadedUrl);

			Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			BitMatrix matrix = new MultiFormatWriter().encode(new String(uploadedUrl.getBytes(charset), charset),
					BarcodeFormat.QR_CODE, 200, 200, hintMap);
			MatrixToImageWriter.writeToFile(matrix, qrFilePath.substring(qrFilePath.lastIndexOf('.') + 1),
					new File(qrFilePath));

		} catch (IOException ex) {
			System.err.println(ex);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return accountNo + ".png";
		//return qrFilePath;

	}

}
