<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="https://code.jquery.com/jquery.min.js"></script>
<script type="text/javascript">
	function sqlrun() {
		var text = document.getElementById('sql').value;
		console.log(text);
	}
</script>
</head>
<body>
<div>
<textarea cols="20" rows="20" id="sql">
</textarea>
</div>
<button id="sqltest" onclick="javascript:sqlrun();">SQL 입력</button>
</body>
</html>