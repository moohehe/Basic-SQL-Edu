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
	// ���� document.body�� html�� A4 ũ�⿡ ���� PDF�� ��ȯ
	html2canvas(document.body, {
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
		<button value="�־Ⱥ����ܶ߹�" onclick="javascript:onClick()"></button>
	</div>
</div>
</body>
</html>