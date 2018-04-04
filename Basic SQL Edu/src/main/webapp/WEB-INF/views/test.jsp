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
		});
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
	<br>
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
			
			<div class="navicontext">
				<p class="qs" id="qstext">${questext.qstext }</p><br>
				<div id="qstype"> ${questext.qstype }</div> <br><br>
			    <div id="qsdetail"> ${questext.qsdetail }</div> <br><br>
				<p> Examples </p>
				<div id="qsExm">${questext.qsExm }</div>
			</div>
		</div>	
	
		<!-- 전체 스테이지 맵 화면 -->
		 <div class="level-menu">
		 	<div class="float-btnframe lvmemu" style="background: #1a1a1a">
	        	<div class="float-unit choosinglv" style="background: #1a1a1a">Choose a level</div> 
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
	</div>
	
</body>
</html>