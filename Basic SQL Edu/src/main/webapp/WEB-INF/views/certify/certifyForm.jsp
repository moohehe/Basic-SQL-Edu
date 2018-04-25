<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>[ 수료증 폼 ]</title>
</head>
<body>

	<form action="certify" method="post">
		<p>이름 입력하삼</p>
		<input type="text" id="cert_name" name="cert_name">
		<p>email 입력하삼</p>
		<input type="email" id="cert_email" name="cert_email" required="required">
		
		<input type="submit" value="수료증 출력!"></input>
	</form>
	

	

</body>
</html>