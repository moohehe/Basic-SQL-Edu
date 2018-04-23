<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="resources/css/default.css" />
<style type="text/css">
	.centerdiv {
	background-color:white;
	opacity: 1;
	font-family: Verdana,sans-serif;
	}
</style>
</head>
<body>

<div class="centerdiv">
<h1>[ Contact US ]</h1>


<form action="write" method="post" id="faq">
<table class="table">

	<tr class="tr">
		<th class="th" style="border-bottom-color: ">작성자</th>
		<td class="td"><input type="text" name="fb_user"></td>
	</tr>
	
	<tr class="tr">
		<th class="th">이메일</th>
		<td class="td"><input type="text" name="email" style="width:300px;">
		<br>
		※ 이메일을 정확히 입력해주세요.
		</td>
	</tr>
	<tr class="tr">
		<th class="th">제목</th>
		<td class="td"><input type="text" name="title" style="width:300px;"></td>
	</tr>
	<tr class="tr">
		<th class="th">내용</th>
		<td class="td"><textarea rows="30" cols="100" name="content"></textarea></td>
	</tr>
	<tr class="tr">
		<td colspan="2" class="white center td">
		<div class="btn menubtn submitbtn">SUBMIT</div>
		<div class="btn menubtn closebtn">CLOSE</div>
		</td>
	</tr>
</table>
</form>

</div>				
</body>
</html>