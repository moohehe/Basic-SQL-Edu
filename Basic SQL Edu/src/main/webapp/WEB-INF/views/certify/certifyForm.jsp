<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>[ ������ �� ]</title>
</head>
<body>

	<form action="certify" method="post">
		<p>�̸� �Է��ϻ�</p>
		<input type="text" id="cert_name" name="cert_name">
		<p>email �Է��ϻ�</p>
		<input type="email" id="cert_email" name="cert_email" required="required">
		
		<input type="submit" value="������ ���!"></input>
	</form>
	

	

</body>
</html>