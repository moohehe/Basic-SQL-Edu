<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<!DOCTYPE html>
<html>
<head>
<title>Everybody's SQL</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">


<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Amatic+SC">
<link href="resources/css/homeStyle.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript">
	$(function() {
		var value = $('#certiBtn').val();
		var url = $('#url').val();
		if (value == 1) {
			setTimeout(5000, function() {
				location.href=url;
			});
		}
	});
</script>
<style>
body, html {
	height: 100%
}

body, h1, h2, h3, h4, h5, h6 {
	font-family: "Amatic SC", sans-serif
}

.bgimg {
	background-repeat: no-repeat;
	background-size: 100% 100%;
	background-image: url("resources/image/bgi.jpg");
	min-height: 100%;
}

.borderimg {
	height: 100%;
	
	background-size: cover;
	background-repeat: no-repeat;
	background-image: url("resources/image/animal.png");
	background-position: center;
	background-size: 50%;
	opacity: 1
}
</style>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>
<script type="text/javascript">
$(function(){
	
	$('#goTest').on('click', function(){
	 var language = $('#langop').val(); //사용자가 select Box에서 선택한 언어.	
	 $(location).attr('href', "test?langop="+language);
		
	});
});
</script>
</head>
<!-- Header with image -->
<body class="bgimg w3-display-container w3-grayscale-min">
	<header>
		<div class="w3-display-top w3-center">
			<br> <span class="w3-text-purple w3-hide-small"
				style="font-size: 100px"><b>Everybody's SQL</b></span>
		</div>
	</header>

	<div class="borderimg w3-text-purple w3-center" style="height: 63%;">
		<div class="" style="padding-top: 13%;">
			<b style="font-size: 40px">♡-About-♡</b>
			<div class="w3-text-purple" style="font-weight: bold; text-align: center">
				<ul style="font-size: 17px; list-style:none;">
					<li>단순하고 직관적인 방식의</li>
					<li>미니게임을 통해서</li>
					<li>누구나 쉽~게</li> 
					<li>기초 SQL구문을 익힐 수 있습니다.</li>					
				</ul>
			</div>
			

			<div class="w3-display-middle w3-center" style="margin-top: 20%;">
				<div style="">
					<p style="font-size: 30px; font-weight: bold;">
						<button id="goTest" class="w3-btn w3-button w3-yellow" > Enter</button>						
					</p>
					<span class="w3-text-purple w3-tag" style="font-size: 20px">
					<b>Select Language</b></span> 
					<select id="langop" name="lang" style="font-size: 20px;">
						
						<option value="1" <c:if test="${selectedLang == 1}">selected</c:if>> 영어</option>
						<option value="2" <c:if test="${selectedLang == 2}">selected</c:if>> 한국어</option>
						<option value="3" <c:if test="${selectedLang == 3}">selected</c:if>> 일본어</option>
					</select>
				</div>
			</div>
		</div>
	</div>
	
		<input type="hidden" id="certiBtn" value="${certiBtn}">
		<input type="hidden" id="url" value="${url}">
</body>
</html>