<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>
<script type="text/javascript">
	function sqlrun() {
		var str = document.getElementById('sql').value;
		console.log(str);
		if (str == '') {
			return;
		}
		$.ajax({
			type:"POST"
			, url:"sqlCompiler"
			, data:{
				sql:str
			}
			, success: function(e) {
				console.log(e);
				$('#resultView').val(e);
			}
			, error : function(e) {
				console.log('error:'+e);	
			}
		});
	}
	function dbtest() {
		var str = document.getElementById('sql').value;
		console.log(str);
		if (str == '') {
			return;
		}
		$.ajax({
			type:"POST"
			, url:"sqlCompiler2"
			, data:{
				sql:str
			}
			, success: function(e) {
				console.log(e);
				$('#resultView').val(e);
			}
			, error : function(e) {
				console.log('error:'+e);	
			}
	}
</script>
</head>
<body>

	<div>
		<textarea cols="20" rows="20" id="sql"></textarea>
		<textarea cols="20" rows="20" id="resultView"></textarea>
	</div>
	<button id="sqltest" onclick="javascript:sqlrun();">SQL 확인</button>
	<button id="db" onclick="javascript:dbtest()">dbTest</button>
	<a href="getTable">뷰 테스트1</a> <br>
	<button class="btn" id="btn1">1</button><br>
	<button class="btn" id="btn2">2</button><br>
	<button class="btn" id="btn3">3</button><br>
	<button class="btn" id="btn4">4</button><br>
	<button class="btn" id="btn5">5</button><br>
	<button class="btn" id="btn6">6</button><br>
	<button class="btn" id="btn7">7</button><br>
	<button class="btn" id="btn8">8</button><br>
	<button class="btn" id="btn9">9</button><br>
	
	
</body>
</html>