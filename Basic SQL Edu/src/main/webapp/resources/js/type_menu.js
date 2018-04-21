// table row - mouserover되면 view의 그림에 그림자 보여주기
function setTd() {
	console.log('setTd run');
	$('td').mouseover(function() {
		var selected = $(this).attr('th_code');
		console.log('th_code='+selected);
		$('div .shadow[th_code="'+selected+'"]').show();
	})
	$('td').mouseout(function() {
		$('div .shadow').hide();
	})
}
// table tag 만들기
function setTableView(table) { // parameter는 2차원 배열이거나 arraylist임
	console.log('setTableView run');
	console.log(table);
	var table_data = $('#table_data');
	var tags = "<table class='table table-hover'><!-- Table head --><thead class='blue lighten-4'>";
	// 첫번째 줄
	for (var i = 0; i < table.length; i++) {
		if (i == 0 ) {
			tags += "<tr>";
			//tags += "<th></th>";
			tags += "<th scope='row'>species</th>";
			tags += "<th>color</th>";
			tags += "<th>habitat</th>";
			tags += "<th>legs</th>";
			tags += "<th>size</th>";
			tags += "</tr></thead>";
		}
		tags += "<tbody><tr>";
		//tags += "<td scope='row' th_code="+table[i].th_code+">"+table[i].rownum+"</td>";
		tags += "<td th_code="+table[i].th_code+">"+table[i].animal_species+"</td>";
		tags += "<td th_code="+table[i].th_code+">"+table[i].animal_color+"</td>";
		tags += "<td th_code="+table[i].th_code+">"+table[i].animal_habitat+"</td>";
		tags += "<td th_code="+table[i].th_code+">"+table[i].animal_legs+"</td>";
		tags += "<td th_code="+table[i].th_code+">"+table[i].animal_size+"</td>";
		tags += "</tr>";
	}
	tags += "</tbody></table>";
	console.log(tags);
	table_data.html(tags);
}
// server 로 데이터를 전송함.
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
