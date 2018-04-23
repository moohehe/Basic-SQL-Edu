<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title></title>
<script type="text/javascript" src="<c:url value="/resources/js/bluebird.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/html2canvas.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jspdf.min.js"/>"></script>
<script type="text/javascript">
function onClick(){
	
	document.getElementById("btn1").style.display="none";
	// 현재 document.body의 html을 A4 크기에 맞춰 PDF로 변환
	html2canvas(document.body, {
	  onrendered: function(canvas) {
	 
	    // 캔버스를 이미지로 변환
	    var imgData = canvas.toDataURL('image/png');
	     
	    var imgWidth = 210; // 이미지 가로 길이(mm) A4 기준
	    var pageHeight = imgWidth * 1.414;  // 출력 페이지 세로 길이 계산 A4 기준
	    var imgHeight = canvas.height * imgWidth / canvas.width;
	    var heightLeft = imgHeight;
	     
	        var doc = new jsPDF('p', 'mm');
	        var position = 0;
	         
	        // 첫 페이지 출력
	        doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
	        heightLeft -= pageHeight;
	         
	        // 한 페이지 이상일 경우 루프 돌면서 출력
	        while (heightLeft >= 20) {
	          position = heightLeft - imgHeight;
	          doc.addPage();
	          doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
	          heightLeft -= pageHeight;
	        }
	 
	        // 파일 저장
	        doc.save('certification_A4.pdf');
	  }
	});
}
</script>
<style type="text/css">
* {
    margin: 0px;
    padding: 0px;
}
html, body {
    height: 95%;
    background-image: url("resources/image/certification.png");
    background-repeat: no-repeat;
    background-position: 50% 50%;
    background-attachment: fixed;
    background-size: cover;
}
.certi {
    height: 95%; 
}
</style>
</head>
<body>
<div class="certi">
	<div id="btn1">
		<button value="왜안보여줌뜨발" onclick="javascript:onClick()"></button>
	</div>
</div>
</body>
</html>