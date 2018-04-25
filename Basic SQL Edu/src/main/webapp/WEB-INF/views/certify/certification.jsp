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
<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>
<script type="text/javascript">
function onClick(){
	
	document.getElementById("btn1").style.display="none";
	
	var element = $('img');
	// ���� document.body�� html�� A4 ũ�⿡ ���� PDF�� ��ȯ
	html2canvas(element, {
	  onrendered: function(canvas) {
	 
	    // ĵ������ �̹����� ��ȯ
	    var imgData = canvas.toDataURL('image/png');
	     
	    var imgWidth = 210; // �̹��� ���� ����(mm) A4 ����
	    var pageHeight = imgWidth * 1.414;  // ��� ������ ���� ���� ��� A4 ����
	    var imgHeight = canvas.height * imgWidth / canvas.width;
	    var heightLeft = imgHeight;
	     
	        var doc = new jsPDF('p', 'mm');
	        var position = 0;
	         
	        // ù ������ ���
	        doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
	        heightLeft -= pageHeight;
	         
	        // �� ������ �̻��� ��� ���� ���鼭 ���
	        while (heightLeft >= 20) {
	          position = heightLeft - imgHeight;
	          doc.addPage();
	          doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
	          heightLeft -= pageHeight;
	        }
	 
	        // ���� ����
	        doc.save('certification_A4.pdf');
	  }
	});
}
</script>
<style type="text/css">

img .bg{
	min-height: 100%;
	min-width : 1024px;
	width: 100%;
	height: auto;
	position: fixed;
	top: 0;
	left: 0;
}
@media screen and (max-width: 1024px){
	img.bg{
	left:50%;
	margin-left: -512px;
	}
}
div#container{
position: relative;
}
</style>
</head>
<body>
<img class = "bg" alt="" src="resources/image/certi.png">
<div id="container">
</div>
<button id = "btn1" value="�־Ⱥ����ܶ߹�" onclick="javascript:onClick()"></button>
</body>
</html>