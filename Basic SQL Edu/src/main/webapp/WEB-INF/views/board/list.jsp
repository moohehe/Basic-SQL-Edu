<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>[ Feedback List ]</title>

<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/default.css' />" />
<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js" />"></script>
<script type="text/javascript">

	function writeForm(){
		location.href = "writeForm";
	}
	
	function pagingFormSubmit(currentPage){
		var form = document.getElementById("pagingForm");
		var page = document.getElementById("page");
		
		page.value = currentPage;
		
		form.submit();
	}
	
	
</script>
</head>
<body>

<div class="centerdiv">
<h1>[ Feedback List ]</h1>

<table style="margin: auto; text-align: center;"> 
<c:forEach var="board" items="${boardlist }">
	<tr class="tr">
		<th style="width:100px; ">등록번호</th>
		<td colspan="2">${board.fb_no}</td>
	</tr>	
	<tr class="tr">		
		<th class="th">작성자</th>
		<td class="td">${board.fb_user}</td>
	</tr>
	<tr class="tr">
		<th class="th">작성일</th>
		<td class="td">${board.fb_indate }</td>
	</tr>	
	<tr class="tr">		
		<th class="th">이메일</th>
		<td class="td">${board.email}</td>
	</tr>
	<tr class="tr">
		<th class="th">제목</th>
		<td class="td left">${board.title}</td>	
	</tr>	
	<tr class="tr">						
		<th class="th">내용</th>
		<td class="td"><pre>${board.content}</pre></td>	
	</tr>	
	
	<tr class="tr">
		<th class="th">메모</th>
		<td class="td left">
			<form action="updateMemo" method="post">
					<label><input type="radio" name="status"<c:if test="${board.status==0 }">checked="checked"</c:if>value="0"><span class="up">대기</span></label>
					<label><input type="radio" name="status"<c:if test="${board.status==1 }">checked="checked"</c:if>value="1"><span class="up">완료</span></label>
					<input type="hidden" name="fb_no" value="${board.fb_no }">
					<br>
					<textarea name="memo" rows="3" cols="80">${board.memo }</textarea>
					<br>
					<input type="submit" value="저장">
			</form>
		</td>
	</tr>
	<td class="td white">
		<br/>
	</td>
	
</c:forEach>
</table>
</div>	

<br/>

<div id="navigator">
	<a href="javascript:pagingFormSubmit(${navi.currentPage - navi.pagePerGroup})">◁◁</a> &nbsp;&nbsp;
	<a href="javascript:pagingFormSubmit(${navi.currentPage - 1})">◀</a> &nbsp;&nbsp;
	<c:forEach begin="${navi.startPageGroup }" end="${navi.endPageGroup }" var="counter">
		<c:choose>
			<c:when test="${counter == navi.currentPage }">
				<a href="javascript:pagingFormSubmit(${counter })"><b>${counter }</b></a>
			</c:when>
			<c:otherwise>
				<a href="javascript:pagingFormSubmit(${counter })">${counter }</a>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<a href="javascript:pagingFormSubmit(${navi.currentPage + 1})">▶</a> &nbsp;&nbsp;
	<a href="javascript:pagingFormSubmit(${navi.currentPage + navi.pagePerGroup})">▷▷</a>

<form id="pagingForm" method="get" action="list">
	<input type="hidden" name="page" id="page" />
	제목 : <input type="text"  name="searchText" value="${searchText}" />
	<input type="button" onclick="pagingFormSubmit(1)" value="검색">
</form>

</div>

<div id="listMemo"></div>

</body>
</html>