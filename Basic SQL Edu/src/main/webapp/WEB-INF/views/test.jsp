<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	function sqlrun() {
		var text = document.getElementById('sql').value;
		console.log(text);
	}
</script>
</head>
<body>
<div>
<textarea cols="4" rows="4" id="sql">
</textarea>
</div>
<button value="SQL 확인" id="sqltest" onclick="javascript:sqlrun();" />
</body>
</html>