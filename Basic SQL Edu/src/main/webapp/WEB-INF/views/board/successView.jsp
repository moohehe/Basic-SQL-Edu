<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../resources/css/default.css" />
<title>[ 감사합니다 ]</title>
</head>
<body>
<div class="centerdiv" style="text-align: center;">
<br><br><br><br><br><br><br>
<c:if test="${selectedLang == '1'}">
	<h1>Thank you.</h1>
	<h2>Feedback Has Been Completed</h2>
	<h3>We Will Do Our Best To Reflect Your Opinion.</h3>
</c:if>
<c:if test="${selectedLang == '2'}">
	<h2>피드백 접수가 완료 되었습니다.</h2>
	<h3>보내주신 의견을 참고하여 사이트에 적극반영 하도록 노력하겠습니다.</h3>
	<h1>감사합니다.</h1>
</c:if>
<c:if test="${selectedLang == '3'}">
	<h1>おめでとうございます。</h1>
	<h2>ファドバークを完了しました。</h2>
	<h3>送っていただいたご意見を参考していいサイトに反映しておきます。</h3>
</c:if>

<a href="test?langop=${selectedLang}">돌아가기</a>
</div>
</body>
</html>