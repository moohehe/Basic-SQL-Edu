var sql_text;
$(function() {
	sql_text = $('textarea').val();
})
function text_keypress() {
	if ($('#sql').val().split('\n').length < 5) {
		sql_text = "";
	} 
	if ($('#sql').val().split('\n').length <= 9) {
		sql_text = $('#sql').val();
	}
}
function text_keyup() {
	if ($('#sql').val().split('\n').length > 9) {
		console.log('10줄 넘어감');
		$('textarea').val(sql_text);

	}
}
//table row - mouseover되면 view의 그림에 그림자 보여주기
var data = new Array();
function setClass() {
	for (var k = 0; k < 5 ; k++) {
		data[k] = $('img[columesimg='+(k+1)+']').attr('class');
		console.log('data['+k+']='+data[k]);
	}
}
function setTd() {
	var c_data;
	console.log('setTd run');
	var selected;
	$('td').mouseover(function() {
		console.log('mouseover');
		$('div.tcolumes img').removeClass().addClass('tableColumes');
		selected = $(this).attr('th_code');
		$('img[th_code='+selected+']').removeClass().addClass('tada infinite animated tableColumes' );
	});
	$('td').mouseout(function() {
		console.log('mouseout');
		$('div.tcolumes img').removeClass();
		for (var k = 0; k < 5 ; k++) {
			//console.log('addClass='+data[k]);
			$('img[columesimg='+(k+1)+']').addClass(data[k]);
		}
	});
}
//table tag 만들기
function setTableView(table, lv) { // parameter는 2차원 배열이거나 arraylist임
	console.log('setTableView run');
	console.log(table);
	var table_data = $('#table_data');
	var tags = "<table class='table table-hover'><!-- Table head --><thead class='blue lighten-4'>";

	// 첫번째 줄
	for (var i = 0; i < table.length; i++) {
		if (i == 0 ) {

			if (lv < 12 ) {
				//animal일 경우.
				tags += "<tr>";
				//tags += "<th></th>";
				tags += "<th scope='row' class='t_head'>species</th>";
				tags += "<th class='t_head'>color</th>";
				tags += "<th class='t_head'>habitat</th>";
				tags += "<th class='t_head'>legs</th>";
				tags += "<th class='t_head'>size</th>";
				tags += "</tr></thead>";

			}
			else if (lv < 16) {
				//person일 경우.
				tags += "<tr>";
				//tags += "<th></th>";
				tags += "<th scope='row' class='t_head'>hair_color</th>";
				tags += "<th class='t_head'>job</th>";
				tags += "<th class='t_head'>height</th>";
				tags += "<th class='t_head'>gender</th>";
				tags += "</tr></thead>";
			}
			else {
				//robot일 경우.
				tags += "<tr>";
				//tags += "<th></th>";
				tags += "<th scope='row' class='t_head'>r_size</th>";
				tags += "<th class='t_head'>r_color</th>";
				tags += "<th class='t_head'>r_type</th>";
				tags += "<th class='t_head'>weapon</th>";
				tags += "</tr></thead>";
			}
		}
		if (lv < 12 ) {
			tags += "<tbody><tr>";
			//tags += "<td scope='row' th_code="+table[i].th_code+">"+table[i].rownum+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].animal_species+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].animal_color+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].animal_habitat+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].animal_legs+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].animal_size+"</td>";
			tags += "</tr>";
		} else if (lv < 16) {
			tags += "<tbody><tr>";
			//tags += "<td scope='row' th_code="+table[i].th_code+">"+table[i].rownum+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].hair_color+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].job+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].height+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].gender+"</td>";
			tags += "</tr>";
		} else {
			tags += "<tbody><tr>";
			//tags += "<td scope='row' th_code="+table[i].th_code+">"+table[i].rownum+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].r_size+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].r_color+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].r_type+"</td>";
			tags += "<td class='t_body' th_code="+table[i].th_code+">"+table[i].weapon+"</td>";
			tags += "</tr>";
		}
	}
	tags += "</tbody></table>";
	console.log(tags);
	table_data.html(tags);
}
//server 로 데이터를 전송함.
function sqlrun() {
	var str = document.getElementById('sql').value;
	var stage = $('#currentLv').val();
	var table_name =  $('#table_name').text()+"_view";
	console.log(str);
	if (str == '') {
		return;
	}
	$.ajax({
		type:"POST"
			, url:"sqlCompiler"
				, data:{
					sql:str,
					table_name: table_name,
					questionNumber:stage
				}
	, dataType: 'json'
		, success: function(e) {

			if(stage==11){
				var step = 0;
				//정답이 맞는 게 확인된 경우, 바꾸어 준다.
				try{
					if(e.drop == true){
						step++;
					}
					if(e.change == true){
						step++
					}
					if(e.add == true){
						step++;
					}
					if(e.modify == true){
						step++;
					}
					if(e.rename == true){
						step++;
					}

					switch(step){ //alter의 단계를 확인하여 그림을 바꾸어 줌.
					case 1:
						//한문제 맞췄을 때의 그림 보여주기
						alert("히죽");
						break;
					case 2:
						//두문제 맞췄을 때의 그림 보여주기

						break;
					case 3:
						//세문제 맞췄을 때의 그림 보여주기

						break;
					case 4:
						//네문제 맞췄을 때의 그림 보여주기

						break;
					case 5:
						if(e.alterComplete == true){
							sql_success();
						}
						break;
					default :	//실패
						sql_fail(e.errorMessage);
						break;
					}
				}catch(Exception){
					sql_fail(e.errorMessage);
				}
			}else{ //11번 alter문제를 제외하고는 모두 아래 로직을 따라간다.
				console.log(e);
				if (e.password == 'pass' ) {
					location.href = e.url;
					return false;
				}
				$('#resultView').val(e);
				// data 로 맞췄다 틀렸다 표시할것
				if (e.success == '1') {
					// 맞춤.
					sql_success();
					if(e.goCerti == true){
						//인증서 고
						goCertify();
					}
				}else {
					// 문제 틀림
					sql_fail(e.errorMessage);
				}
			}
		}
	, error : function(e) {
		console.log('error:'+e);	
	}
	});
}
var editor_text;
/* 입력한 sql 구문이 틀렸을 때 */
function sql_fail(errorMessage) {
	console.log('fail run');
	// 실패했을 경우에는 errorMessage를 div로 띄우고 (textarea 위에 겹쳐서)
	// 이 div는 클릭하면 사라진다. fadeout()
	// view_menu 쪽의 그림파일들을 순간적으로 boo-! 하는 느낌의 class를 입력한다. animation 효과 삽입
	// 위 animation은 1번만 or 2~3초에 한번 정도 boo! 하고 2~3번정도만 반복한다.
	$('.fail').fadeIn("slow");
	$('.errorMessage').text(errorMessage);
	$('.fail').on('click',function() {
		$('.fail').fadeOut();
	});
}
/* 정답일 경우 */
function sql_success() {
	console.log('success run');
	// 성공했다는 메세지를 div로 띄우고
	// Next 버튼을 만들고
	// next 버튼을 누르면
	// next 버튼+ success 메세지가 있는 div가 사라지고(fadeout)
	// 스테이지 버튼 눌렀을 때 나오는 레벨 색상 변경 후
	$('.stagebtn'+$('#currentLv').val()).css('color', 'red'); //(정답 맞추었을 때만 해당 작업 처리.)
	// nextBtn에 있는 function 실행
	// 

	// 성공하면 view_menu 쪽의 그림파일에 class를 입력한다. (뛰어노는 듯한 기쁜 이미지 동작을 부여한다.)
	$('.success').fadeIn("slow");
}
function successStage() {
	nextStage();
	$('.success').hide();
	$('textarea').val("");
}

//20스테이지를 모두 종료햇을 경우에는 인증서 발급 메뉴로 간다.
function goCertify(){
	location.href("goCertify");
}
