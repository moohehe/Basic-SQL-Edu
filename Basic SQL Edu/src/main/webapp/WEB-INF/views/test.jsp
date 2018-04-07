<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/Navibar.css"/>"/>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/navigationbarjs.js"/>"></script>
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
		});
	}
	
	$(function(){
		
		//테이블 이름 표시
		$('.questionTable').hover(function(){
			
				$('.helptext').text('table_land');
				
				
		}, function(){
			$('.helptext').text('');
			$('.helptext2').text('');
			$('.helptext3').text('');

		});
		
		//칼럼 안내문구
		$('.tcolumes').hover(function(){
			var colume =$(this).attr('tcolumes');
			
			if(colume == '1'){
				$('.helptext').text('bluebird');
			}else if(colume == '2'){
				$('.helptext').text('blackpenguin');
			}else if(colume == '3'){
				$('.helptext').text('giraffe');
			}
		}
		, function(){
			var colume =$(this).attr('tcolumes');
				$('.helptext').text('');
				$('.helptext2').text('');
				$('.helptext3').text('');
		});
		
		
		
	});
	
	
	
</script>
<style type="text/css">
	.sqlAnswer {
		position: absolute; top: 50%; 
		width: 100%;
		height: 80%;
		border-top: 5px;
		border-top-style: solid;
		border-top-color: gray;
	}
	.sqlAnswersheet{
		position: absolute; left: 20%; 
		width: 35%;
		border: 2px;
		border-style: solid;
		border-color: red;
	}
	.questionTable {
		background-image: url("<c:url value="/resources/image/table_land.jpg"/>");
		background-repeat: no-repeat;
		background-position: center center;
		background-size: full;
		position: absolute; top: 10%; left: 10%;
		width: 50%;
		height: 30%;
		border: 1px;
		border-color: blue;
		border-style: solid;
		padding-left: 10px;
		color: black;
		
		
	}
	.tableColumes {
		float: left;
		width: auto; height: auto;
    	max-width: 130px;
    	max-height: 130px;
	}
	.helpdiv{
		top: 80%;
		position: absolute;
		color: white;
	}
	.strobe {
  transform-origin: bottom;
  animation: strobeStart .5s ease-out, strobe 1s infinite;
  animation-delay: 0s, .5s;
}

@keyframes strobeStart {
  0% {
    transform:  skew(0deg,0deg) scaleY(1) ;
    animation-timing-function: ease-in;
   }
  40% {
    transform:  skew(0deg,0deg) scaleY(.9);
    animation-timing-function: ease-out;
  }
  100% { transform:   skew(4deg,0deg) scaleX(1); }
}

@keyframes strobe {
  0% { transform:   skew(4deg,0deg) scaleX(1); }
  10% { transform:  skew(1deg,0deg) scaleY(.9) ; }
  50% { transform:  skew(-4deg,0deg) scaleX(1); }
  60% { transform:  skew(-1deg,0deg) scaleY(.9) ; }
  100% {transform: skew(4deg,0deg) scaleX(1); }
}
	
</style>
</head>
<body>
	
	
	<!-- 문제 출제 화면 DIV (테이블이 그림으로 보여지는 곳.) -->
	<div class="questionTable"> 
		<div class="tcolumes strobe" tcolumes="1"> 
			<img class="tableColumes" src="<c:url value="/resources/image/bluebird2.png"/>">
		</div>	
		
		<div class="tcolumes" tcolumes="2"> 
			<img class="tableColumes" src="<c:url value="/resources/image/blackpenguin.jpg"/>">
		</div>	
		
		<div class="tcolumes" tcolumes="3"> 
			<img class="tableColumes" src="<c:url value="/resources/image/girrafe.jpg"/>">
		</div> 
		<br>
			<div class="helpdiv helptext"> </div>
			<div class="helpdiv helptext2"> </div>
			<div class="helpdiv helptext3"> </div>
	</div>
	
	
	<!-- SQL 정답 입력 화면 DIV -->
	<div class="sqlAnswer">
	<div class="sqlAnswersheet">
		<textarea cols="20" rows="20" id="sql"></textarea>
		<textarea cols="20" rows="20" id="resultView"></textarea> <br>
	<button id="sqltest" onclick="javascript:sqlrun();">SQL 확인</button>
	<button id="db" onclick="javascript:dbtest()">dbTest</button>
	</div>
	</div><!-- SQL 정답 입력 화면 DIV 종료 -->

	<!-- 우측 네비게이션 화면 관련 DIV -->	
	<div class="navigation">
	
		<!-- 기본으로 보여지는 네비게이션 바 페이지 -->
		<div class="wrap"> 
			
			<!-- 언어 선택 (영어, 한국어, 일본어) -->		
			<div class="langcheck" >
				<input  id="currentLv" type="hidden" value="${questext.lvstatus }"></input>
				<button class="langbtn" data-num="1" > eng </button> 
				<button class="langbtn" data-num="2" > kor </button> 
				<button class="langbtn" data-num="3" > jpn </button> 
			</div>
			
			<!-- 현재 레벨과  이전, 다음버튼 그리고 전체 스테이지 맵으로 이동 버튼-->
			<div class="float-btnframe">
			<div class="float-unit lvstat" id="LvInfo"> Level ${questext.lvstatus } of 10 </div>
	
			<button class="float-unit dropbtn">Stage</button>
			<button class="float-unit btn nxtbtn" id="nextbtn" > ▶ </button>
			<button class="float-unit btn" id="prevbtn" > ◀ </button>
			<input id="currentLang" type="hidden" value="${questext.textLang }" >
			</div>
			<br>
			
			<!-- 진행 상태 표시 바 -->
			<DIV class=progress-small> 
			<DIV role=progressbar aria-valuenow=10 aria-valuemin=0 
			class="progress-bar-small progress-bar-s-info" id="progresslv" style="width: ${questext.lvstatus }0%" aria-valuemax=100> 
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
			</div>
		</div>	
		
		<!-- stage 버튼 누르면 나타나는 전체 스테이지 맵 화면 -->
		 <div class="level-menu">
		 	<div class="float-btnframe lvmemu" style="background: #6e6e6e">
	        	<div class="float-unit choosinglv" style="background: #6e6e6e">Choose a level</div> 
	       		<button class="float-unit closing dropbtn"> CLOSE </button>
	        </div>
				<br>
			
			<!-- 전체 문제(단계) 출력 -->
	        <div class="dropdown-content">
	        <c:forEach var="stages" varStatus="status" items="${stageList}">
	        	<button class="stagebtn${status.count }"> ${status.count } </button> 
	        	<button class="moveStagebtn" data-num="${status.count }"> Level ${stages.lvstatus } </button> <br>
	        </c:forEach>
			   
		    </div>
	      	<br>
	      	
	      </div>
	</div><!-- 우측 네비게이션 화면 관련 DIV 종료 -->	
	
</body>
</html>