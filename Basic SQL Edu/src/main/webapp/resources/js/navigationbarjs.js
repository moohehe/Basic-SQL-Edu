var fstage; //현재 레벨(스테이지) 가져옴.
var flang; //어떤 언어인지 선택. 
//arraylist 받을 변수 설정.
var qlist;
//배경 이미지 경로 함수 지정
var imgpath;
//문제 테이블 안 칼럼 선택자 지정 함수
var imgselector;
	

$(function(){
	fstage = $('#currentLv').val(); //현재 레벨(스테이지) 가져옴.
	flang = $('#currentLang').val(); //어떤 언어인지 선택. 
	
	//arraylist 받을 변수 설정.
	qlist = [];
	
	
	//배경 이미지 경로 함수 지정
	imgpath = function(file){
		return "url(/www/resources/image/"+file+")";
	}
	
	//문제 테이블 안 칼럼 선택자 지정 함수
	imgselector = function(imgcolumes){
		return '[columesimg = "'+imgcolumes+'"]';
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
	
	
	
<<<<<<< HEAD
	getDataByAJAX(fstage, flang);
	//createQuiz(qlist, fstage);
=======
	//(얘가 진짜) 문제 별 그림 뿌려주는 함수.
	function createQuiz(qlist, stage){
		console.log("cq의 stage"+stage);
		
		//stage별 분기 처리 필요.
		switch(stage){ //지금 현재 없는 문제 뷰. (1번, 11번, 15, 16, 19, 20)
		
			case 1: //select 문제가 아니어서 지정된 화면을 보여줘야 하는 레벨1.
				ques1();
				
				break;
			//동물 select
			case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 10:
				
				$.each(qlist, function(index, value){
					
					var species = value.animal_species;
					var color = value.animal_color;
					
					//테이블 안 칼럼들 이미지 변경.
					$(imgselector((index)+1)).attr("src", "/www/resources/image/"+species+color+".JPG");
					
				});
				
				//배경 변경.
				$('.questionTable').css({"background":imgpath("table_land.jpg"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
				break;
				
			case 9: //div가 3개만 나오는 문제라서 일단 따로 분류.
				$.each(qlist, function(index, value){
					var species = value.animal_species;
					var color = value.animal_color;
					
					//테이블 안 칼럼들 이미지 변경.
					$(imgselector((index)+1)).attr("src", "/www/resources/image/"+species+color+".JPG");
					$(imgselector(4)).attr("src", "");
					$(imgselector(5)).attr("src", "");
				});
				
				//배경 변경.
				$('.questionTable').css({"background":imgpath("table_land.jpg"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
				break;
				
			case 11: //alter table 문제
				break;
			case 12: case 13: case 14: // 모두 person 문제테이블 활용.
				break;
			case 17: case 18: // robot 문제테이블 활용.
				break;
				
		}
	}//end of create quiz
	
	
	//처음 그려질 경우 DB를 갔다오는 Ajax. (나중에 Ajax는 함수화 가능하면 함수화 한다.)
	$.ajax({
		url : "langcheck",
		type : "post",
		data : {
			stage : fstage,
			lang : flang,
			compl : "pass"
		},
		dataType : "json",
		success : function(obj){
			
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
			$('#progresslv').css('width', (obj.questext.lvstatus)*5+"%");
			console.log(document.cookie);
			
			//처음 화면 문제테이블 갱신
			qlist = obj.qlist;
			createQuiz(qlist, obj.questext.lvstatus);
			
		},
		error : function(err){
			alert('가장 마지막 페이지입니다.');
		}
	});
>>>>>>> branch 'master' of https://github.com/moohehe/Basic-SQL-Edu.git
	
	
	
	
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
		console.log('누름');
		var stage = $('#currentLv').val(); //현재 레벨(스테이지) 가져옴.
		var lang = $(this).attr('data-num'); //어떤 언어인지 선택.
		
		getDataByAJAX(stage, lang);
		return false;
	});
	
	//다음 버튼 동작
	$('#nextbtn').on('click', function(){
		var stage = Number($('#currentLv').val())+1;
		var lang = $('#currentLang').val();
		
		getDataByAJAX(stage, lang);
		return false;
		
	});
	
	//이전 버튼 동작
	$('#prevbtn').on('click', function(){
		var stage = Number($('#currentLv').val())-1;
		var lang = $('#currentLang').val();
		
		if(stage<1){
			alert('첫 페이지입니다.');
			return;
		}
		getDataByAJAX(stage, lang);
		return false;
	});
	
	//전체 스테이지 맵에서 원하는 스테이지 버튼 클릭시 해당 스테이지로 이동.
	$('.moveStagebtn').on('click', function(){
		var stage = $(this).attr('data-num');
		var lang = $('#currentLang').val();
		
		getDataByAJAX(stage, lang);
		return false;
	});
			

});

//(얘가 진짜) 문제 별 그림 뿌려주는 함수.
function createQuiz(qlist, stage){
	console.log("cq의 stage"+stage);
	
	//stage별 분기 처리 필요.
	switch(stage){ //지금 현재 없는 문제 뷰. (1번, 11번, 15, 16, 19, 20)
	
		case 1: //select 문제가 아니어서 지정된 화면을 보여줘야 하는 레벨1.
			
			$('.questionTable').css({"background":imgpath("bg"+stage+".png"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
			
			break;
		//동물 select
		case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 10:
			
			$.each(qlist, function(index, value){
				
				var species = value.animal_species;
				var color = value.animal_color;
				
				//테이블 안 칼럼들 이미지 변경.
				$(imgselector((index)+1)).attr("src", "/www/resources/image/"+species+color+".JPG");
				$(imgselector((index)+1)).attr("th_code", value.th_code);
			});
			
			//배경 변경.
			$('.questionTable').css({"background":imgpath("bg"+stage+".png"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
			break;
			
		case 9: //div가 3개만 나오는 문제라서 일단 따로 분류.
			$.each(qlist, function(index, value){
				var species = value.animal_species;
				var color = value.animal_color;
				
				//테이블 안 칼럼들 이미지 변경.
				$(imgselector((index)+1)).attr("src", "/www/resources/image/"+species+color+".JPG");
				$(imgselector(4)).attr("src", "");
				$(imgselector(5)).attr("src", "");
			});
			
			//배경 변경.
			$('.questionTable').css({"background":imgpath("bg"+stage+".png"), 'background-repeat' : 'no-repeat', 'background-position':'center center'});
			break;
			
		case 11: //alter table 문제
			break;
		case 12: case 13: case 14: // 모두 person 문제테이블 활용.
			break;
		case 17: case 18: // robot 문제테이블 활용.
			break;
			
	}
}

function getDataByAJAX(stage, lang) {
	console.log('stage='+stage+' lang='+lang);
	//처음 그려질 경우 DB를 갔다오는 Ajax. (나중에 Ajax는 함수화 가능하면 함수화 한다.)
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
			console.log('ajax success');
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
			$('#progresslv').css('width', (obj.questext.lvstatus)*5+"%");
			console.log('cookie= '+document.cookie);
			
			//처음 화면 문제테이블 갱신
			qlist = obj.qlist;
			console.log('뾰롱');
			console.log(qlist);
			createQuiz(qlist, obj.questext.lvstatus);
			setTableView(qlist); // navi 이동후에 table_data에 값 입력하기
			setTd(); // mouserover event set
			setView();
		},
		error : function(err){
			alert('가장 마지막 페이지입니다.');
		}
	});
}