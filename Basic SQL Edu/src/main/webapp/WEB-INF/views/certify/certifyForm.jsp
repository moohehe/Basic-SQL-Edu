<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>[ CERTIFICATION ]</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/Navibar.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/mainPage.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/view_menu.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/type_menu.css"/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/certification.css' />" rel="stylesheet">

	<!-- BootStrap -->
	<link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet">
	<link href="<c:url value='/resources/css/mdb.css' />" rel="stylesheet">
	<link href="<c:url value='/resources/css/style.css' />" rel="stylesheet">
	<link href="<c:url value='/resources/css/animate.css' />" rel="stylesheet">

	<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>
</head>
<body>
<<<<<<< HEAD
<div class="bodyy">
	<div class="certi-form">
		<div class="topp" style="height:25px;"></div>
		<div class="topp">
		<%-- <div class="back_img"><img src="<c:out value='resourec/image/backbtn.png' /> ">BACK</div> --%>
			 CERTIFICATION
		</div>
		<div class="title">
			<span>
			<c:choose>
				<c:when test="${lang == 1 }">
					Congratulation!<br> Enter your information for Certification.
				</c:when>
				<c:when test="${lang == 2 }">
					축하합니다.!<br> 인증서 발급을 위해 정보를 입력해주세요 
				</c:when>
				<c:when test="${lang == 3 }">
					おめでとうございます。<br> 証明書の発給のためにあなたの情報を入力してください。
				</c:when>
			</c:choose>
			</span>
		</div>
		<br><br><br>
		<form action="certify" method="post">
		<c:choose>
		<c:when test="${lang == 1 }">
			<p class="content_name">name</p>
		</c:when>
		<c:when test="${lang == 2 }">
			<p class="content_name">이름</p>
		</c:when>
		<c:when test="${lang == 3 }">
			<p class="content_name">名前</p>
		</c:when>
	</c:choose>
			<input class="content_input_form" type="text" id="cert_name" name="cert_name" placeholder="Your name">
			<p class="content_name">E-MAIL</p>
			<input class="content_input_form" type="email" id="cert_email" name="cert_email" placeholder="ilovesql@basicsqledu.com">
			<br><br><br>
			<input class="btn certi-submit" type="submit" value="SUBMIT"></input>
		</form>
	</div>
	<div class="xbtn"></div>
</div>
=======

	<form action="certify" method="post">
		<p>�̸� �Է��ϻ�</p>
		<input type="text" id="cert_name" name="cert_name">
		<p>email �Է��ϻ�</p>
		<input type="email" id="cert_email" name="cert_email" required="required">
>>>>>>> branch 'master' of https://github.com/moohehe/Basic-SQL-Edu.git
		

</body>
</html>
