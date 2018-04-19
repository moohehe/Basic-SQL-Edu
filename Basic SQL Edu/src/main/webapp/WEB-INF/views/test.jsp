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
<script type="text/javascript" src="<c:url value="/resources/js/jquery-linenumbers.min.js"/>"></script>
<script type="text/javascript">

	function getLineNumberAndColumnIndex(textarea){
	    //var textLines = textarea.value.substr(0, textarea.value.length).split("\n");
	    var textLines = textarea.val().split("\n");
	    //console.log ('t ='+t);
	    var currentLineNumber = textLines.length;
	    var currentColumnIndex = textLines[textLines.length-1].length;
	    console.log("Current Line Number "+ currentLineNumber+" Current Column Index "+currentColumnIndex );
	    var linesView = document.getElementById('.lined');
	    var lines = "";
	    for (var k = 1; k <= currentLineNumber; k++){
	    	lines += k+"."+"\n";
	    }
	    console.log(lines);
	    $('.lined').val(lines);
	 }
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
	
	
	//화면 처리. (문제 테이블 화면)
	$(function(){
		
		getLineNumberAndColumnIndex($('#sql'));
		
		
		// 쿠키를 읽어서 들어오는 경우에 처리를 해야...
		//쿠키에 있는 내용을 읽어서 어느 stage까지 풀었는지 확인 후 해당 문제 들고 들어와야 함.
		
		
		
		
		
		
		
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
		
		// textarea skin 변경
		// Target all classed with ".lined"
		  /* $(".lined").linedtextarea(
		    {selectedLine: 1}
		  ); */


				
	});
	
	
	
</script>
<style type="text/css">
	textarea{
		width: 555px;
		height: 270px;
		font-size: 20px;
		padding: 10px;
		line-height: 30px;
		border-radius: 3px;
		border: 1px solid #aaaaaa;
	}
	
	.left_menu {
		width:100%;
		float:right;
		margin-left:-400px;
		height:100%;
	}
	.right_menu {
		float:left;
		width:400px;
		height:100%;
	}
	.view_menu {
		height:100%;
	}
	.type_menu {
		height:30%;
	}
	.sqlAnswer {
		position: absolute; top: 50%; 
		width: 100%;
		height: 40%;
		border-top: 5px;
		border-top-style: solid;
		border-top-color: gray;
	}
	.sqlAnswersheet{
		position: absolute;  
		width: 72%;
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
  transform-origin: 50% 50%;
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
  100% { 
  	transform:   skew(4deg,0deg) scaleX(1); 
  }
}

@keyframes strobe {
  0% { transform:   skew(4deg,0deg) scaleX(1);  }
  10% { transform:  skew(1deg,0deg) scaleY(.9) ;  }
  50% { transform:  skew(-4deg,0deg) scaleX(1);  }
  60% { transform:  skew(-1deg,0deg) scaleY(.9) ;  }
  100% {transform: skew(4deg,0deg) scaleX(1);  }
}
	
</style>
</head>
<body>
				<input  id="currentLv" type="hidden" value="${questext.lvstatus }"></input>
				<input id="currentLang" type="hidden" value="${questext.textLang }" ></input>

<div>	
<div class="left_menu">
	<!-- 그림이 표시되는 부분 -->
	<div class="view_menu">
		<%@ include file="view_menu.jsp" %>
	</div>
	
	<!-- 타이핑 파트 -->
	<div class="type_menu">
		<%@ include file="type_menu.jsp" %>
	</div>
</div>

<div class="right_menu">
	<!-- Navigation Bar -->
	<%@ include file="navigation.jsp" %>
</div>
</div>
</body>
</html>