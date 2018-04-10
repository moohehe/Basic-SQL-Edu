/**
 * 
 */


//여기 문제별 innerHTML이 들어감.

$(function() {
	
	
	
	//배경 이미지 경로 함수 지정
	var imgpath = function(file){
		return "url(/www/resources/image/"+file+")";
	}
	
	//문제 테이블 안 칼럼 선택자 지정 함수
	var imgselector = function(imgcolumes){
		return 'img[columesimg = "'+imgcolumes+'"]';
	}
	
	//1번 문제 호출 innerHTML
	var ques1 = function(){
		
		//배경(테이블) 이미지 변경.
		$('.questionTable').css({"background":imgpath("table_land.jpg"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
		//테이블 안 칼럼들 이미지 변경.
		$(imgselector(1)).attr("src", "/www/resources/image/bluebird2.png");
		$(imgselector(2)).attr("src", "/www/resources/image/blackpenguin.jpg");
		

	}
	
	//2번 문제
	var ques2 = function(){
		//배경 변경.
		$('.questionTable').css({"background":imgpath("bgi.jpg"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
		
	}
	
	
	//다음 버튼을 누름에 따라서 계속 문제 변형.
	$('#nextbtn').on('click', function(){
		ques2();
		
	});
	
	//이전 버튼을 누름에 따라서 계속 문제 변형.
	$('#prevbtn').on('click', function(){
		ques1();
		
	});
	
});






