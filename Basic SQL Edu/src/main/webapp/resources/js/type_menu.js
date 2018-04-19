	/* 사용자 input form 에 들어가는 Line 수      */
	function getLineNumberAndColumnIndex(textarea){
	    //var textLines = textarea.value.substr(0, textarea.value.length).split("\n");
	    var textLines = textarea.val().split("\n");
	    //console.log ('t ='+t);
	    var currentLineNumber = textLines.length;
	    var currentColumnIndex = textLines[textLines.length-1].length;
	    console.log("Current Line Number "+ currentLineNumber+" Current Column Index "+currentColumnIndex );
	    var linesView = document.getElementById('.lined');
	    var lines = "";
	    for (var k = 1; k <= currentLineNumber; k++){
	    	lines += k+"."+"\n";
	    }
	    console.log(lines);
	    $('.lined').val(lines);
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
