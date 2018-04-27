<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>

<title>Work Scout</title>

<!-- Mobile Specific Metas
================================================== -->
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<!-- CSS
================================================== -->
<link rel="stylesheet" href="resources/css/style_board.css">
<link rel="stylesheet" href="resources/css/colors/green.css" id="colors">
<!-- Scripts
================================================== -->
<script src="<c:out value='resources/js/jquery-3.2.1.js' />"></script>
<script src="<c:out value='resources/js/board/custom.js' />"></script>
<script src="<c:out value='resources/js/board/jquery.superfish.js' />"></script>
<script src="<c:out value='resources/js/board/jquery.themepunch.tools.min.js' />"></script>
<script src="<c:out value='resources/js/board/jquery.themepunch.revolution.min.js' />"></script>
<script src="<c:out value='resources/js/board/jquery.themepunch.showbizpro.min.js' />"></script>
<script src="<c:out value='resources/js/board/jquery.flexslider-min.js' />"></script>
<script src="<c:out value='resources/js/board/chosen.jquery.min.js' />"></script>
<script src="<c:out value='resources/js/board/jquery.magnific-popup.min.js' />"></script>
<%-- <script src="<c:out value='resources/js/board/waypoints.min.js' />"></script> --%>
<%-- <script src="<c:out value='resources/js/board/jquery.counterup.min.js' />"></script> --%>
<script src="<c:out value='resources/js/board/jquery.jpanelmenu.js' />"></script>
<script src="<c:out value='resources/js/board/stacktable.js' />"></script>
<script type="text/javascript">
	$(function() {
		$('.up').on('click',function() {
			var fb_no = $(this).attr('fb_no');
			var status = $(this).attr('status');
			$.ajax({
				url : 'updateState'
				, type : 'post'
				, data : {
					fb_no : fb_no
					, status : status
				}
				, success : function(e) {
					if (e == 1) {
						selectstatus(fb_no,status);
					}
				}
				, error : function(e) {
					console.log('실패');
				}
			});
		});
		
	});
	function selectstatus(fb_no,status) {
		if (status == 0) {
			$('.up[status="0"]').css('background-color','black');
			$('.up[status="1"]').css('background-color','');
		} else {
			$('.up[status="0"]').css('background-color','');
			$('.up[status="1"]').css('background-color','black');
		}
		$('input[fb_no='+fb_no+']').attr('status',status);
		
	}
function writeForm(){
	location.href = "writeForm";
}

function pagingFormSubmit(currentPage){
	var form = document.getElementById("pagingForm");
	var page = document.getElementById("page");
	page.value = currentPage;
	var temp = $('#searchText').val();
	form.submit();
}


</script>

</head>

<body>
<div id="wrapper">


<!-- Titlebar
================================================== -->
<div id="titlebar">
	<div class="container">
		<div class="ten columns">
			<span>We've found ${total } feedback for:</span>
			<h2>Basic SQL Edu</h2>
		</div>

<!-- 		<div class="six columns">
			<a href="add-resume.html" class="button">Post a Resume, It’s Free!</a>
		</div> -->

	</div>
</div>


<!-- Content
================================================== -->
<div class="container">
	<!-- Recent Jobs -->
	<div class="eleven columns">
	<div class="padding-right">

		<ul class="resumes-list">
			<c:forEach var="board" items="${boardlist }">
				<li>
					<div class="resumes-list-content">
						<h4>${board.fb_user } <span>${board.title }</span></h4>
						<span><i class="fa fa-map-marker"></i> ${board.email }</span>
						<span><i class="fa fa-money"></i> ${board.fb_indate }</span>
						<p>${board.content }</p>
						
						<!-- <div class="skills">
							<span name="status" value="0">대기</span>
							<span name="status" value="1">완료</span>
						</div> -->
						<div class="clearfix"></div>
	
					<div class="clearfix">
						<form style="width:100%;" action="updateMemo" method="post">
							<div class="skills">
							<label>
							<span  style="width:90%; <c:if test="${board.status == 0 }">background-color:black;</c:if> " class="up" fb_no="${board.fb_no }" status="0">대기</span>
							</label>
							<label>
							<span  style="width:90%; <c:if test='${board.status == 1 }'>background-color:black;</c:if> " class="up" fb_no="${board.fb_no }" status="1">완료</span>
							</label>
							</div>
							<input style="width:100%" type="hidden" name="status" fb_no="${board.fb_no }" value="${board.status }">
							<input style="width:100%" type="hidden" name="fb_no" value="${board.fb_no }">
							<br>
							<textarea name="memo" rows="3" cols="80">${board.memo }</textarea>
							<br>
							<input type="submit" value="저장" style="width:100%;">
						</form>
					</div>
					</div>
				</li>
			</c:forEach>
		</ul>
		<div class="clearfix"></div>

		<div class="pagination-container">
			<nav class="pagination">
				<ul>
					<c:forEach begin="${navi.startPageGroup }" end="${navi.endPageGroup }" var="counter">
					<li>
					<c:choose>
						<c:when test="${counter == navi.currentPage }">
								<a href="javascript:pagingFormSubmit(${counter })"><b>${counter }</b></a>
						</c:when>
						<c:otherwise>
							<a href="javascript:pagingFormSubmit(${counter })">${counter }</a>
						</c:otherwise>
					</c:choose>
					</li>
				</c:forEach>
				</ul>
			</nav>

			<nav class="pagination-next-prev">
				<ul>
					<li><a class="prev" href="javascript:pagingFormSubmit(${navi.currentPage - 1})">PREVIOUS</a></li>
					<li><a class="Next" href="javascript:pagingFormSubmit(${navi.currentPage + 1})">NEXT</a></li>
				</ul>
			</nav>
			
		</div>

	</div>
	</div>
<div class="five columns">

		<!-- Skills -->
		<div class="widget">
			<h4>SEARCH</h4>

		</div>

		<!-- Location -->
		<div class="widget">
			<h4>SEARCH</h4>
			<form class="list-search" id="pagingForm" method="get" action="list">
				<input type="text"  name="searchText" id="searchText" value="${searchText}" placeholder="제목 + 내용" >
				<button onclick="pagingFormSubmit(1)" style="width:100%;"><i class="fa fa-search"></i></button>
				<div class="clearfix"></div>
				<input type="hidden" name="page" id="page">
			</form>
			
		</div>




	</div>



</div>



<!-- Back To Top Button -->
<div id="backtotop"><a href="#">TOP</a></div>

</div>
<!-- Wrapper / End -->



</body>
</html>