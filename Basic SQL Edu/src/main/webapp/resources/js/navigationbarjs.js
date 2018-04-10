/**
 * 
 */


$(function(){
	
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
		
		if(stage >3){
			alert('가장 마지막 페이지입니다.');
			return;
		}
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
				$('#LvInfo').text("Level "+obj.questext.lvstatus+" of 10"); 
				$('#currentLv').val(obj.questext.lvstatus);
				$('#qstext').text(obj.questext.qstext);
				$('#qstype').text(obj.questext.qstype);
				$('#qsdetail').text(obj.questext.qsdetail);
				$('#qsExm').text(obj.questext.qsExm);
				$('#currentLang').val(obj.questext.textLang);
				$('#progresslv').css('width', stage+"0%");
				console.log(document.cookie);
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
				$('#LvInfo').text("Level "+obj.questext.lvstatus+" of 10"); 
				$('#currentLv').val(obj.questext.lvstatus);
				$('#qstext').text(obj.questext.qstext);
				$('#qstype').text(obj.questext.qstype);
				$('#qsdetail').text(obj.questext.qsdetail);
				$('#qsExm').text(obj.questext.qsExm);
				$('#currentLang').val(obj.questext.textLang);
				$('#progresslv').css('width', stage+"0%");
				console.log(document.cookie);
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
				$('#LvInfo').text("Level "+obj.questext.lvstatus+" of 10"); 
				$('#currentLv').val(obj.questext.lvstatus);
				$('#qstext').text(obj.questext.qstext);
				$('#qstype').text(obj.questext.qstype);
				$('#qsdetail').text(obj.questext.qsdetail);
				$('#qsExm').text(obj.questext.qsExm);
				$('#currentLang').val(obj.questext.textLang);
				$('#progresslv').css('width', stage+"0%");
				
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
	







