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
		var str = document.getElementById('sql').value;
		console.log(str);
		$.ajax({
			type:"POST"
			, url:"sqlCompiler"
			, data:{
				sql:str
			}
			, success: function(e) {
				console.log(e);
			}
			, error : function(e) {
				console.log('error:'+e);	
			}
		});
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