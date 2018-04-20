$.ready(function() {
	$('td').mouseover(function() {
		console.log('asdf');
	})
});
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
