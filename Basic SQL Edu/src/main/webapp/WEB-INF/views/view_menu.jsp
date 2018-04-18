<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 문제 출제 화면 DIV (테이블이 그림으로 보여지는 곳.) -->
	<div class="questionTable"> 
		
		<c:choose>
			<c:when test="${qlist != null }">
				<c:forEach var="animal" varStatus="status" items="${qlist}">
					<div class="tcolumes strobe" tcolumes="${status.count}"> 
						<img class="tableColumes" columesimg="${status.count}" src="<c:url value="/resources/image/bluebird2.png"/>">
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div class="tcolumes strobe" tcolumes="1"> 
					<img class="tableColumes" columesimg="1" src="<c:url value="/resources/image/bluebird2.png"/>">
				</div>	
				
				<div class="tcolumes" tcolumes="2"> 
					<img class="tableColumes" columesimg="2" src="<c:url value="/resources/image/blackpenguin.jpg"/>">
				</div>	
				
				<div class="tcolumes" tcolumes="3"> 
					<img class="tableColumes" columesimg="3" src="<c:url value="/resources/image/girrafe.jpg"/>">
				</div> 
				<div class="tcolumes" tcolumes="4"> 
					<img class="tableColumes" columesimg="4" src="<c:url value="/resources/image/girrafe.jpg"/>">
				</div> 
				<div class="tcolumes" tcolumes="5"> 
					<img class="tableColumes"  columesimg="5" src="<c:url value="/resources/image/girrafe.jpg"/>">
				</div> 
			</c:otherwise>
		</c:choose>
		<br>
			<div class="helpdiv helptext"> </div>
			<div class="helpdiv helptext2"> </div>
			<div class="helpdiv helptext3"> </div>
	</div>