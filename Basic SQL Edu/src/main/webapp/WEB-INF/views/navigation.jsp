<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<!-- 우측 네비게이션 화면 관련 DIV -->	
	<div class="sidebar-fixed position-fixed navigation">
		<!-- 기본으로 보여지는 네비게이션 바 페이지 -->
		<div class="wrap"> 
			`
			<!-- 현재 레벨과  이전, 다음버튼 그리고 전체 스테이지 맵으로 이동 버튼-->
			<div class="float-btnframe">

				<div class="float-unit lvstat" id="LvInfo"> Level ${questext.lvstatus } of 20 </div>
		
				<div class="float-unit dropbtn">Stage</div>
				<div class="float-unit btn" id="prevbtn" >◀</div>
				<div class="float-unit btn nxtbtn" id="nextbtn" >▶</div>
				<input id="currentLang" type="hidden" value="${questext.textLang }" ></input>
				</div>
				<!-- 진행 상태 표시 바 -->
				<DIV class=progress-small> 
				<DIV role=progressbar aria-valuenow=10 aria-valuemin=0 
				class="progress-bar-small progress-bar-s-info" id="progresslv" style="width: ${questext.lvstatus *5}%" aria-valuemax=100> 
				<!-- <SPAN class=sr-only>20% Complete</SPAN>  -->
				</DIV><!-- Complete --> 
			</DIV>
			
			
			
			
			
			
			
			<!-- 네이게이션 내부 text들 받아옴. -->
			<div class="navicontext">
				<p class="qs" id="qstext">${questext.qstext }</p><br>
				<div id="qstype"> ${questext.qstype }</div> <br><br>
			    <div id="qsdetail"> ${questext.qsdetail }</div> <br><br>
				<p> Examples </p>
				<div id="qsExm">${questext.qsExm }</div>
				
				<!-- 언어 선택 (영어, 한국어, 일본어) -->		
				<div class="langcheck" >
					<input  id="currentLv" type="hidden" value="${questext.lvstatus }"></input>
					<div class="langbtn btn" data-num="1" > eng </div> 
					<div class="langbtn btn" data-num="2" > kor </div> 
					<div class="langbtn btn" data-num="3" > jpn </div> 
				</div>
			</div>
		</div>	
		
		<!-- 피드백 게시판-->
<!-- 		<div class="contact">
			<ul class="a">
				<li><a href="board/writeForm">글쓰기</a></li>		
				<li><a href="board/list">피드백 게시판</a></li>
				<li><a href="board/successView">작성완료화면 테스트</a></li>
			</ul>
		</div> -->
		
		<!-- stage 버튼 누르면 나타나는 전체 스테이지 맵 화면 -->
		 <div class="level-menu">
		 	<div class="float-btnframe lvmemu">
	        	<div class="float-unit choosinglv" >Choose a level</div> 
	       		<button class="float-unit closing dropbtn"> CLOSE </button>
	        </div>
			<!-- 전체 문제(단계) 출력 -->
	        <div class="dropdown-content">
		        <c:forEach var="stages" varStatus="status" items="${stageList}">
					<c:if test="${status.count == 1 || status.count == 11 }">
		        		<ul class="vertical">
		        	</c:if>
			        	<li>
			        	<a href="" class="li1 moveStagebtn" data-num="${status.count }">
			        	 <span class="stagebtn${status.count }">√</span>
			        	   Level ${stages.lvstatus } </a>
			        	</li>
		        	<c:if test="${status.count == 10}">
				    	</ul>
				    	</div>
				        <div class="dropdown-content">
	        		</c:if>
		        	<c:if test="${status.count == 20}">
				    	</ul>
				    	</div>
	        		</c:if>
		        </c:forEach>
		    </div>
	      </div>
	

		  
	</div><!-- 우측 네비게이션 화면 관련 DIV 종료 -->	
	