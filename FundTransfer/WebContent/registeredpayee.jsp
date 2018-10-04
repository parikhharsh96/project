<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.bank.entity.Payee"%>
<%@page import="com.bank.entity.Aadhaar"%>
<%@page import="com.bank.entity.Accounts"%>
<%@page import="com.bank.entity.Cards"%>
<%@page import="com.bank.entity.FixedDeposit"%>
<%@page import="com.bank.entity.Transactions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="bootstrap.min.css">
<script type="text/javascript" src="bootstrap.min.js"></script>
<script type="text/javascript" src="jquery-3.3.1.min.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Insert title here</title>
</head>
<body>
<form action="fundTransfer.bank" method="get">
			
			<c:forEach items="${Payees}" var="payee">
				<%-- <jsp:useBean id="payee" class="com.bank.entity.Payee" />
				<jsp:getProperty name="payee" property="payeeName"/>
				<jsp:getProperty name="payee" property="payee"/>
				<jsp:getProperty name="payee" property="payer"/> --%>
				<input type="radio" name="payeeAccountNo" value="${payee.payee}"> ${payee.payeeName }<br>
				
			</c:forEach>
		<label> Enter the amount</label><input type="text" name="amount">
		
		<input type="submit" value="submit">

</form>

<a href="#">Register a Payee?</a>

</body>
</html>