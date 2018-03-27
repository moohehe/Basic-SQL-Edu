<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Home</title>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		setInterval(function() {
			$.ajax({
				url:'getTime'
				, dataType : 'text'
				, contentType: "application/x-www-form-urlencoded; charset=UTF-8"
				, success : function(d) {
					$('#serverTime').text(d);
					console.log(d);
				}
				, error : function(e) {
					console.log(e);
				}
			});
		},1000);
	});
	
	</script>
</head>
<body>
	<h1>
		Basic SQL Edu
	</h1>

	<P>  The time on the server is <span id="serverTime">${serverTime}.</span> </P>
	<a href="test">MOVE TO COMPILER</a>
</body>
</html>
