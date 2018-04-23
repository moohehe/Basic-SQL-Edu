/*	//화면 처리. (문제 view menu 에서 mouser_over할때 처리)
	$(function(){
		$('.shadow').hide();
		
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
	});*/
	
function setView() { // type_menu.js의 setTd()와 동일함
	console.log('setView run');
	$('img').mouseover(function() {
		console.log('mouseover');
		var selected = $(this).attr('th_code');
		console.log('th_code='+selected);
		$('td[th_code="'+selected+'"]').addClass('selected');
	})
	$('img').mouseout(function() {
		$('td').removeClass('selected');
	})
}	