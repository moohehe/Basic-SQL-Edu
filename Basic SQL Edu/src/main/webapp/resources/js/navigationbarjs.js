/**
 * 
 */


$(function(){
	
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
	
	
	//stage(navigation-bar)버튼 눌렀을 때 숨겨놓은 div (전체 스테이지 맵) 보여주기/닫기
	$('.dropbtn').click(function(){
		$('.level-menu').slideDown(450);
		$('.wrap').hide();
	});
	
	$('.closing').click(function(){
		$('.level-menu').slideUp(450);
		$('.wrap').show();
	});
	
	//언어선택 버튼 눌렀을 경우의 처리.
	$('.langbtn').on('click', function(){
		var stage = $('#currentLv').val(); //현재 레벨(스테이지) 가져옴.
		var lang = $(this).attr('data-num'); //어떤 언어인지 선택.
		
		$.ajax({
			url : "langcheck",
			type : "post",
			data : {
				stage : stage,
				lang : lang
			},
			dataType : "json",
			success : function(obj){
				$('#qstext').text(obj.questext.qstext);
				$('#qstype').text(obj.questext.qstype);
				$('#qsdetail').text(obj.questext.qsdetail);
				$('#qsExm').text(obj.questext.qsExm);
				$('#currentLang').val(obj.questext.textLang);
				console.log(document.cookie);
			},
			error : function(err){
				alert('실패'+JSON.stringify(err));
			}
		});
	});
	
	//다음 버튼 동작
	$('#nextbtn').on('click', function(){
		var stage = Number($('#currentLv').val())+1;
		var lang = $('#currentLang').val();
		
		/*if(stage >20){
			alert('가장 마지막 페이지입니다.');
			return;
		}*/
		$.ajax({
			url : "langcheck",
			type : "post",
			data : {
				stage : stage,
				lang : lang,
				compl : "pass"
			},
			dataType : "json",
			success : function(obj){
				
				console.log(obj);
				
				//쿠키값에 따른 화면 갱신(완료표시를 위함)
				$('.stagebtn'+$('#currentLv').val()).css('color', 'red');
				
				//화면 값 갱신
				$('#LvInfo').text("Level "+obj.questext.lvstatus+" of 20"); 
				$('#currentLv').val(obj.questext.lvstatus);
				$('#qstext').text(obj.questext.qstext);
				$('#qstype').text(obj.questext.qstype);
				$('#qsdetail').text(obj.questext.qsdetail);
				$('#qsExm').text(obj.questext.qsExm);
				$('#currentLang').val(obj.questext.textLang);
				$('#progresslv').css('width', stage+"0%");
				console.log(document.cookie);
				
				//화면 문제테이블 갱신
				stageDisplay(stage);
			},
			error : function(err){
				alert('가장 마지막 페이지입니다.');
			}
		});
		
	});
	
	//이전 버튼 동작
	$('#prevbtn').on('click', function(){
		var stage = Number($('#currentLv').val())-1;
		var lang = $('#currentLang').val();
		
		if(stage<1){
			alert('첫 페이지입니다.');
			return;
		}
		$.ajax({
			url : "langcheck",
			type : "post",
			data : {
				stage : stage,
				lang : lang
			},
			dataType : "json",
			success : function(obj){
				$('#LvInfo').text("Level "+obj.questext.lvstatus+" of 20"); 
				$('#currentLv').val(obj.questext.lvstatus);
				$('#qstext').text(obj.questext.qstext);
				$('#qstype').text(obj.questext.qstype);
				$('#qsdetail').text(obj.questext.qsdetail);
				$('#qsExm').text(obj.questext.qsExm);
				$('#currentLang').val(obj.questext.textLang);
				$('#progresslv').css('width', stage+"0%");
				console.log(document.cookie);
				
				//화면 문제테이블 갱신
				stageDisplay(stage);
			},
			error : function(err){
				alert('첫 페이지입니다.');
			}
		});
	});
	
	//전체 스테이지 맵에서 원하는 스테이지 버튼 클릭시 해당 스테이지로 이동.
	$('.moveStagebtn').on('click', function(){
		var stage = $(this).attr('data-num');
		var lang = $('#currentLang').val();
		
		$.ajax({
			url : "langcheck",
			type : "post",
			data : {
				stage : stage,
				lang : lang
			},
			dataType : "json",
			success : function(obj){
				//화면 갱신
				$('#LvInfo').text("Level "+obj.questext.lvstatus+" of 20"); 
				$('#currentLv').val(obj.questext.lvstatus);
				$('#qstext').text(obj.questext.qstext);
				$('#qstype').text(obj.questext.qstype);
				$('#qsdetail').text(obj.questext.qsdetail);
				$('#qsExm').text(obj.questext.qsExm);
				$('#currentLang').val(obj.questext.textLang);
				$('#progresslv').css('width', stage+"0%");
				
				//화면 문제테이블 갱신
				stageDisplay(stage);
				
				
				//화면전환 (다시 문제가 보이도록)
				$('.level-menu').slideUp(250);
				$('.wrap').show();
				
				console.log(document.cookie);
			},
			error : function(err){
				alert('스테이지 이동 실패!');
			}
		});
	});
			

});
	







