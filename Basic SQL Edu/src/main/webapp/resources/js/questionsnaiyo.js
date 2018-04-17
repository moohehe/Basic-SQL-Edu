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
		$(imgselector(3)).attr("src", "/www/resources/image/girrafe.jpg");
		$(imgselector(4)).attr("src", "/www/resources/image/girrafe.jpg");
		$(imgselector(5)).attr("src", "/www/resources/image/girrafe.jpg");

	}
	
	//2번 문제
	var ques2 = function(){
		//배경 변경.
		$('.questionTable').css({"background":imgpath("bgi.jpg"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
		//테이블 안 칼럼들 이미지 변경.
		$(imgselector(1)).attr("src", "/www/resources/image/bluebird2.png");
		$(imgselector(2)).attr("src", "/www/resources/image/bluebird2.png");
		$(imgselector(3)).attr("src", "/www/resources/image/bluebird2.png");
		$(imgselector(4)).attr("src", "");
		$(imgselector(5)).attr("src", "");
	}
	
	//3번 문제
	var ques3 = function(){
		//배경 변경.
		$('.questionTable').css({"background":imgpath("animal.png"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
		//테이블 안 칼럼들 이미지 변경.
		$(imgselector(1)).attr("src", "/www/resources/image/blackpenguin.jpg");
		$(imgselector(2)).attr("src", "/www/resources/image/blackpenguin.jpg");
		$(imgselector(3)).attr("src", "");
		$(imgselector(4)).attr("src", "");
		$(imgselector(5)).attr("src", "");
	}
	
	//4번 문제
	var ques4 = function(){
		//배경 변경.
		$('.questionTable').css({"background":imgpath("animal.png"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
		//테이블 안 칼럼들 이미지 변경.
		$(imgselector(1)).attr("src", "/www/resources/image/blackpenguin.jpg");
		$(imgselector(2)).attr("src", "/www/resources/image/blackpenguin.jpg");
		$(imgselector(3)).attr("src", "");
		$(imgselector(4)).attr("src", "");
		$(imgselector(5)).attr("src", "");
	}
	
	//문제 번호(스테이지)가 들어오면 해당 문제(스테이지)에 해당하는  화면을 보여주는 함수.
	function stageDisplay(stage){
		
		switch(stage){
			
			case 1:
				ques1();
				break;
			
			case 2:
				ques2();
				break;
				
			case 3:
				ques3();
				break;
			case 4:
				ques4();
				break;
		
		}
		
	}
	
	//다음 버튼을 누름에 따라서 계속 문제 변형.
	$('#nextbtn').on('click', function(){
		var stage = Number($('#currentLv').val())+1;
		
		stageDisplay(stage);		
	});
	
	//이전 버튼을 누름에 따라서 계속 문제 변형.
	$('#prevbtn').on('click', function(){
		var stage = Number($('#currentLv').val())-1;
		
		stageDisplay(stage);		
	});
	
	//스테이지 이동화면에서 이동 시 계속 문제 변형.
	$('.moveStagebtn').on('click', function(){
		var stage = Number($(this).attr('data-num'));	
		stageDisplay(stage);		
	});
	
	
});