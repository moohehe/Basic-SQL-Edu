var fstage; //현재 레벨(스테이지) 가져옴.
var flang; //어떤 언어인지 선택. 
//arraylist 받을 변수 설정.
var qlist;
var anslist;
//배경 이미지 경로 함수 지정
var imgpath;
//문제 테이블 안 칼럼 선택자 지정 함수
var imgselector;
   

$(function(){
	$('.success').hide();
	$('.fail').hide();
	$('.contactus').hide();
	$('.contactus-bg').hide();
	
	$('.close-cert-search').on('click', function() {
		$('.certi-search').hide();
	});
	
	$('.certification-search-btn').on('click', function() {
		var menu = $(this).text();
		var text = $('#search-data').val();
		$.ajax({
			url : 'certi-search'
			, type : 'post'
			, data : {
				text:text
			}
			, dataType : 'json'
			, success : function(d) {
				if (d.is == true) {
					location.href=d.url;
				} 
				return;
			}
			, error : function(e) {
				console.log(e);
			}
		});
	});
	$('.menubtn').on('click',function() {
		var menu = $(this).text();
		if (menu == 'CONTACT US') {
			$('.contactus').removeClass('hide-on-bush');
			$('.contactus-bg').removeClass('hide-on-bush');
			$('.contactus-bg').show();
			$('.contactus').show();
			$('.contactus-bg').on('click',function() {
				$('.contactus').hide();
				$('.contactus-bg').hide();
			});
		} else if (menu == 'CERTIFICATION') {
			$('.certi-search').show();
			$('.certi-search').addClass('animated bounceInUp');
		} else if (menu == 'CLOSE') {
			$('.contactus').hide();
			$('.contactus-bg').hide();
		}
	});
	
	$('.btn').on('mouseover',function() {
		var btn_name = $(this).text();
		console.log('btn_name'+btn_name);
		if (btn_name == 'SUBMIT') {
			
		} else {
			
		}
	});

	$('.btn').on('mouseout',function() {
		var btn_name = $(this).text();
		console.log('mouserout='+btn_name);
	});
	fstage = $('#currentLv').val(); //현재 레벨(스테이지) 가져옴.
	flang = $('#currentLang').val(); //어떤 언어인지 선택. 
	
	//arraylist 받을 변수 설정.
	qlist = []; //문제 뷰
	anslist=[];//정답 뷰
	
	
	//배경 이미지 경로 함수 지정
	imgpath = function(file){
		return "url(/www/resources/image/"+file+")";
	}
	
	//문제 테이블 안 칼럼 선택자 지정 함수
	imgselector = function(imgcolumes){
		return '[columesimg = "'+imgcolumes+'"]';
	}

   
   //처음 그려지는 경우의 ajax다녀오기.
   getDataByAJAX(fstage, flang);
   
   
   
   //stage(navigation-bar)버튼 눌렀을 때 숨겨놓은 div (전체 스테이지 맵) 보여주기/닫기
   $('.dropbtn').click(function(){
      //$('.level-menu').slideDown(450);
	  $('.level-menu').show();
      $('.wrap').hide();
   });
   
   $('.closing').click(function(){
      /*$('.level-menu').slideUp(450);*/
	   $('.level-menu').hide();
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
      nextStage();
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
      //$('.level-menu').slideUp(4500);
      $('.wrap').show();
      return false;
   });
         

});



// move to Next Stage
function nextStage() {
   var stage = Number($('#currentLv').val())+1;
   var lang = $('#currentLang').val();
   
   getDataByAJAX(stage, lang);
}
//(얘가 진짜) 문제 별 그림 뿌려주는 함수.
function createQuiz(qlist, anslist, stage){
	// img 전부 복구
	$('img').show();
	$('img').fadeIn();
	
   console.log("cq의 stage"+stage);
   
   //칼럼 지우기.
	  for(var i=1; i<6; i++){
		  $(imgselector(i)).attr("src", "");
	  }
   
   // stage 11에 들어갈때만 height 속성을 고정값으로 지정하기 위해서 매번 초기화한다.
   $('div.css-view').css('height','');
   //stage별 분기 처리 필요.
   switch(stage){ //지금 현재 없는 문제 뷰. (1번, 11번, 15, 16, 19, 20)
   
      case 1: //select 문제가 아니어서 지정된 화면을 보여줘야 하는 레벨1.
         
         //칼럼들 지워놓음.
   	  	for(var i=1; i<6; i++){
       	 $(imgselector(i)).attr("src", "");
        }
   	//배경변경
  	  $('.questionTable').css({"background":imgpath("bg1.png"), 'background-size':'100%', 'background-repeat' : 'no-repeat', 'background-position':'center'});
   	//테이블 변경.
  		$('#table_data').html("");

    	  	var tags = "<table class='table table-hover'><!-- Table head --><thead class='blue lighten-4'>";
    	  
    		tags += "<tr>";
			tags += "<th scope='row' class='t_head'>Column</th>";
			tags += "<th class='t_head'>DataType</th>";
			tags += "<th class='t_head'>Constraint</th>";
			tags += "</tr></thead>";
    		
			tags += "<tbody><tr>";
			tags += "<td class='t_body'>Animal_num</td>";
			tags += "<td class='t_body'>Number</td>";
			tags += "<td class='t_body'>Primary key</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>Name</td>";
			tags += "<td class='t_body'>varchar(40)</td>";
			tags += "<td class='t_body'>Unique</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>Color</td>";
			tags += "<td class='t_body'>varchar(40)</td>";
			tags += "<td class='t_body'>Not null</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>Habitat</td>";
			tags += "<td class='t_body'>varchar(40)</td>";
			tags += "<td class='t_body'>Foreign Key</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>Legs</td>";
			tags += "<td class='t_body'>Number</td>";
			tags += "<td class='t_body'>Not null</td>";
			tags += "</tr>";

			tags += "</tbody></table>";
    		
			$('#table_data').html(tags);
			//$('.table-window').html(tags);
         break;
      //동물 select
      case 2: case 3: case 5: case 6: case 7: case 8: case 10:
         
         $.each(qlist, function(index, value){
            
            var species = value.animal_species;
            var color = value.animal_color;
            
            //테이블 안 칼럼들 이미지 변경.
            $(imgselector((index)+1)).attr("src", "/www/resources/image/"+species+color+".png");
            $(imgselector((index)+1)).attr("th_code", value.th_code);
            addAnimation('rubberBand', value.th_code);
         });
         
         $.each(anslist, function(index, value){
            
            var th_code = value;
            
            //테이블 안 칼럼들 중 정답 이미지에만 애니메이션 동작시키기.
            $('img[th_code="'+th_code+'"]').addClass('animated infinite flash');
         });
         //테이블 이름 변경
         $('#table_name').text("[table_name : ANIMAL]");
         //배경변경
         changeBackimg(stage);
         
         break;
      case 4: 
         $.each(qlist, function(index, value){
             
            var species = value.animal_species;
            var color = value.animal_color;
            
            //테이블 안 칼럼들 이미지 변경.
            $(imgselector((index)+1)).attr("src", "/www/resources/image/"+species+color+".png");
            $(imgselector(5)).attr("src", "");
            $(imgselector((index)+1)).attr("th_code", value.th_code);
            addAnimation('rubberBand', value.th_code);
         });
         
         $.each(anslist, function(index, value){
            
            var th_code = value;
            
            //테이블 안 칼럼들 중 정답 이미지에만 애니메이션 동작시키기.
            $('img[th_code="'+th_code+'"]').addClass('animated infinite flash');
         });
         //테이블 이름 변경
         $('#table_name').text("[table_name : ANIMAL]");
         //배경변경
         changeBackimg(stage);
         break;
         
      case 9: //div가 3개만 나오는 문제라서 일단 따로 분류.
         $.each(qlist, function(index, value){
            var species = value.animal_species;
            var color = value.animal_color;
            
            //테이블 안 칼럼들 이미지 변경.
            $(imgselector((index)+1)).attr("src", "/www/resources/image/"+species+color+".png");
            $(imgselector(4)).attr("src", "");
            $(imgselector(5)).attr("src", "");
            $(imgselector((index)+1)).attr("th_code", value.th_code);
            addAnimation('rubberBand', value.th_code);
         });
         
         $.each(anslist, function(index, value){
            var th_code = value;
            //테이블 안 칼럼들 중 정답 이미지에만 애니메이션 동작시키기.
            $('img[th_code="'+th_code+'"]').addClass('animated infinite flash');
         });
         //테이블 이름 변경
         $('#table_name').text("[table_name : ANIMAL]");
         //배경변경
         changeBackimg(stage);
         break;
          
      case 11: //alter table 문제
    	  console.log('11번 실행');
    	  //배경 변경.
    	  $('.questionTable').css({"background":imgpath("alter"+1+".png"), 'background-size':'contain', 'background-repeat' : 'no-repeat', 'background-position':'bottom'});
          
    	  //칼럼들 지워놓음.
    	  for(var i=1; i<6; i++){
        	 $(imgselector(i)).attr("src", "");
         }
    	//테이블 이름 변경
          $('#table_name').text("[table_name : table_name]");
          
    	  // 테이블 부분에 문제 출제가 필요하다.
    	//테이블 변경.
  		var tags = "<table class='table table-hover'><!-- Table head --><thead class='blue lighten-4'>";
  		
    		tags += "<tr>";
			tags += "<th scope='row' class='t_head'>QuizNo.</th>";
			tags += "<th class='t_head'>KeyWord</th>";
			tags += "<th class='t_head'>Description</th>";
			tags += "</tr></thead>";
    		
			tags += "<tbody><tr>";
			tags += "<td class='t_body'>1</td>";
			tags += "<td class='t_body'>Drop</td>";
			tags += "<td class='t_body'>Delete ‘legs’ column</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>2</td>";
			tags += "<td class='t_body'>Change</td>";
			tags += "<td class='t_body'>Change ‘color’ column to ‘hair_color’ and add datatype ‘varchar(20)’</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>3</td>";
			tags += "<td class='t_body'>Add</td>";
			tags += "<td class='t_body'>Add column ‘gender’ And add datatype ‘varchar(20)’</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>4</td>";
			tags += "<td class='t_body'>Modify</td>";
			tags += "<td class='t_body'>Change ‘gender’ column’s datatype to ‘varchar(10)’ and add constraint ’not null’</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>5</td>";
			tags += "<td class='t_body'>Rename</td>";
			tags += "<td class='t_body'>Change table name to ‘person’</td>";
			tags += "</tr>";

			tags += "</tbody></table>";
			$('#table_data').html(tags);

         break;
         
      case 12: case 13: case 14: // 모두 person 문제테이블 활용.
    	  if(stage == 12){
        	 $(imgselector(5)).attr("src", "");
        	 
         }
         $.each(qlist, function(index, value){
            
            var job = value.job;
            var color = value.hair_color;
            
            console.log("사람 직업:"+ job + "사람 머리색:" + color);
            //테이블 안 칼럼들 이미지 변경.
            $(imgselector((index)+1)).attr("src", "/www/resources/image/"+job+color+".png");
            $(imgselector((index)+1)).attr("th_code", value.th_code);
            //애니메이션 동작.
            addAnimation('rubberBand', value.th_code);
         });
         //정답 리스트 돌기.
         $.each(anslist, function(index, value){
             var th_code = value;
             console.log("정답코드"+th_code);
             //테이블 안 칼럼들 중 정답 이미지에만 애니메이션 동작시키기.
             $('img[th_code="'+th_code+'"]').addClass('animated 2s flash');
          });
         //테이블 이름 변경
         $('#table_name').text("[table_name : PERSON]");
         //배경변경
         changeBackimg(stage);
         break;
         
      case 15:
    	//칼럼들 지워놓음.
   	  	for(var i=1; i<6; i++){
       	 $(imgselector(i)).attr("src", "");
        }
   	  	//배경변경
          changeBackimg(stage);
        //테이블 이름 변경
          $('#table_name').text("[table_name : PERSON]");
    	  break;
      case 16:
    	//배경 변경.
          $('.questionTable').css({"background":imgpath("bg"+stage+".png"), 'background-size':'contain', 'background-repeat' : 'no-repeat', 'background-position':'bottom'});
          break;
    	  
      case 17: case 18: // robot 문제테이블 활용.
         $.each(qlist, function(index, value){
            
            var r_type = value.r_type;
            var color = value.r_color;
            console.log("로봇:"+ r_type + "로봇색:" + color);
            
            
            //테이블 안 칼럼들 이미지 변경.
            $(imgselector((index)+1)).attr("src", "/www/resources/image/"+r_type+color+".png");
            $(imgselector((index)+1)).attr("th_code", value.th_code);
            addAnimation('rubberBand', value.th_code);
            
            if(stage == 17){ //문제 17일 경우.
            
            	//칼럼들 지워놓음.
            	for(var i=1; i<6; i++){
            		$(imgselector(i)).attr("src", "");
            	}
            }
          //테이블 이름 변경
            $('#table_name').text("[table_name : ROBOT]");
         });
         //정답 리스트 돌기.
         $.each(anslist, function(index, value){
             var th_code = value;
             console.log("정답코드"+th_code);
             //테이블 안 칼럼들 중 정답 이미지에만 애니메이션 동작시키기.
             $('img[th_code="'+th_code+'"]').addClass('animated 2s flash');
          });
         
         //테이블 이름 변경
         $('#table_name').text("[table_name : ROBOT]");
         //배경 변경.
         changeBackimg(stage);
         break;
         
      case 19:
    	//칼럼들 지워놓음.
        	for(var i=1; i<6; i++){
        		$(imgselector(i)).attr("src", "");
        	}
        	//테이블 이름 변경
            $('#table_name').text("[table_name : ROBOT]");
        	 //배경 변경.
            $('.questionTable').css({"background":imgpath("bg"+stage+".png"), 'background-color':'black','background-size':'cover', 'background-repeat' : 'no-repeat', 'background-position':'bottom'});
            
            var tags = "<table class='table table-hover'><!-- Table head --><thead class='blue lighten-4'>";
      		
            tags += "<tr>";
			tags += "<th scope='row' class='t_head'>r_size</th>";
			tags += "<th class='t_head'>r_color</th>";
			tags += "<th class='t_head'>r_type</th>";
			tags += "<th class='t_head'>weapon</th>";
			tags += "</tr></thead>";
			
			tags += "<tbody><tr>";
			tags += "<td class='t_body'>small</td>";
			tags += "<td class='t_body'>white</td>";
			tags += "<td class='t_body'>R2</td>";
			tags += "<td class='t_body'>beam</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>medium</td>";
			tags += "<td class='t_body'>white</td>";
			tags += "<td class='t_body'>bighero</td>";
			tags += "<td class='t_body'>none</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>big</td>";
			tags += "<td class='t_body'>blue</td>";
			tags += "<td class='t_body'>jaeger</td>";
			tags += "<td class='t_body'>gun</td>";
			tags += "</tr>";
			tags += "<tr>";
			tags += "<td class='t_body'>big</td>";
			tags += "<td class='t_body'>red</td>";
			tags += "<td class='t_body'>optimus</td>";
			tags += "<td class='t_body'>sword</td>";
			tags += "</tr>";
			
			tags += "</tbody></table>";
			$('#table_data').html(tags);
           
           break;
    	  
      case 20:
    	//칼럼들 지워놓음.
      	for(var i=1; i<6; i++){
      		$(imgselector(i)).attr("src", "");
      	}
      //테이블 변경.
  		$('#table_data').html("");
      //배경 변경.
        $('.questionTable').css({"background":imgpath("bg"+stage+".png"), 'background-color':'black','background-size':'contain', 'background-repeat' : 'no-repeat', 'background-position':'bottom'});
        break;
         
   }
   // imgs - class 저장함.
	setClass();
}

function getDataByAJAX(stage, lang) {
	console.log('stage='+stage+' lang='+lang);
	document.getElementById('sql').value = "";
	
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
			
			//화면 값 갱신
			$('#LvInfo').text("Level "+obj.questext.lvstatus+" of 20"); 
			$('#currentLv').val(obj.questext.lvstatus);
			$('#qstext').text(obj.questext.qstext);
			$('#qstype').text(obj.questext.qstype);
			$('#qsdetail').text(obj.questext.qsdetail);
			$('#qsExm').text(obj.questext.qsExm);
			$('#currentLang').val(obj.questext.textLang);
			$('#progresslv').css('width', (obj.questext.lvstatus)*5+"%");
			
			// navigation question description font-family 변경
			$('.qs').removeClass('qs-kor qs-jp qs-eng');
			$('.qs-detail').removeClass('qs-kor qs-jp qs-eng');
			$('.qsExm').removeClass('qs-kor qs-jp qs-eng');
			if (lang == 1) {
				$('.qs').addClass('qs-eng');
				$('.qs-detail').addClass('qs-eng');
				$('.qsExm').addClass('qs-eng');
			} else if ( lang == 2 ) {
				$('.qs').addClass('qs-kor');
				$('.qs-detail').addClass('qs-kor');
				$('.qsExm').addClass('qs-kor');
			} else if ( lang == 3 ) {
				$('.qs').addClass('qs-jp');
				$('.qs-detail').addClass('qs-jp');
				$('.qsExm').addClass('qs-jp');
			}
			console.log('cookie= '+document.cookie);
			console.log('뾰로롱 lv=['+obj.questext.lvstatus+']');
			
	         //처음 화면 문제테이블 갱신
	         qlist = obj.qlist;
	         anslist = obj.ansList;
	         console.log('뾰롱');
	         console.log(qlist);
	         createQuiz(qlist, anslist, obj.questext.lvstatus);
	         setTableView(qlist, obj.questext.lvstatus); // navi 이동후에 table_data에 값 입력하기
	         setTd(); // mouserover event set
	         setView();
		},
		error : function(err){
			alert('가장 마지막 페이지입니다.');
		}
	});
}

function addAnimation(x, th_code) {
   $('img[th_code='+th_code+']').removeClass().addClass(x + ' animated tableColumes' ).one('tableColumes webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
       $(this).removeClass().addClass('animated tableColumes' );
     });
}

function changeBackimg(stage){
	//배경 변경.
    $('.questionTable').css({"background":imgpath("bg"+stage+".png"), 'background-size':'100%', 'background-repeat' : 'no-repeat', 'background-position':'bottom'});
}


//쿠키 전체 삭제
function delAllCookie(){
	$.ajax({
		url : "cookieCtrl",
		type : "post",
		data : {
			stage : 1,
			lang : 2,
			cookieCon: "delete"
		},
		dataType : "json",
		success : function(obj){
			console.log('ajax success');
			
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
			console.log('뾰로롱 lv=['+obj.questext.lvstatus+']');
			
	         //처음 화면 문제테이블 갱신
	         qlist = obj.qlist;
	         anslist = obj.ansList;
	         console.log('뾰롱');
	         console.log(qlist);
	         createQuiz(qlist, anslist, obj.questext.lvstatus);
	         setTableView(qlist, obj.questext.lvstatus); // navi 이동후에 table_data에 값 입력하기
	         setTd(); // mouserover event set
	         setView();
	         console.log('cookie= '+document.cookie);
		},
		error : function(err){
			alert('가장 마지막 페이지입니다.');
		}
	});
}
//쿠키 전체 완료상태로 바꿈
function ComAllCookie(){
	$.ajax({
		url : "cookieCtrl",
		type : "post",
		data : {
			stage : 20,
			lang : $('#currentLang').val(),
			cookieCon: "complete"
		},
		dataType : "json",
		success : function(obj){
			console.log('ajax success');
			console.log('cookiectrl='+obj);
			
		  var stage = 20;
		  var lang = $('#currentLang').val();
		  
		  getDataByAJAX(stage, lang);
		  //$('.level-menu').slideUp(4500);
		  $('.wrap').show();
		  return false;
		},
		error : function(err){
			alert('가장 마지막 페이지입니다.');
		}
	});
}

function closeControlbox() {
	$('#controlbox').addClass('hide-on-bush');
}


