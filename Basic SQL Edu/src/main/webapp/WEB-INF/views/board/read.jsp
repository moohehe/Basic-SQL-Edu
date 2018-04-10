<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../resources/css/default.css" />

<title>[ 피드백 게시판 글읽기 ]</title>
</head>
<body>
<div class="centerdiv">

	<h1>[ 글 읽기 ]</h1>
	<table>
		<tr>
			<th>등록번호</th>
			<td>${board.fb_no }</td>
		</tr>			
		<tr>
			<th>작성자</th>		
			<td>${board.fb_user }</td>
		</tr>
		<tr>
			<th>이메일</th>		
			<td>${board.email }</td>
		</tr>
		<tr>
			<th>제목</th>
			<td>${board.title }</td>		
		</tr>
		<tr>
			<th>내용</th>
			<td>${board.content }</td>		
		</tr>
		<tr>
			<th>작성일</th>
			<td>${board.inputdate }</td>		
		</tr>
	</table>
	
	<a href="board/list">목록으로</a>
	
</div>
</body>
</html>