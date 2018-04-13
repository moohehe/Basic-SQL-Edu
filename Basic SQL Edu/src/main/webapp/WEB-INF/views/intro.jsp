<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
#titles
{
	position: absolute;
	width: 18em;
	height: 50em;
	bottom: 0;
	left: 50%;
	margin-left: -9em;
	font-size: 350%;
	font-weight: bold;
	text-align: justify;
	overflow: hidden;
	transform-origin: 50% 100%;
	transform: perspective(300px) rotateX(25deg);
}
#titles:after
{
	position: absolute;
	content: ' ';
	left: 0;
	right: 0;
	top: 0;
	bottom: 60%;
	background-image: linear-gradient(top, rgba(0,0,0,1) 0%, transparent 100%);
	pointer-events: none;
}
#titlecontent
{
	position: absolute;
	top: 100%;
	animation: scroll 100s linear 4s infinite;
}

@keyframes scroll {
	0% { top: 100%; }
	100% { top: -170%; }
}

</style>
</head>
<body>
<div id="title">
	<div id="titlecontent">

	<p> 길에서 누군가가 잃어버린 시계를 주운 당신, 갑자기 시계의 초침과 분침이 빠르게 돌아가며 알 수 없는 이공간으로 빨려 들어가게 되었다.<br>
	 	느닷없이 타임워프 하게 된 당신…<br> 
   		머리 속으로 갑자기 어떤 목소리가 들려오는데…<br>
    	“총 20가지의 관문을 통과하면 자네를 다시 집으로 보내주도록 하지.” <br>
    	20개의 SQL 문제를 모두 풀고, 무사히 본래 세계로 돌아가도록 하자. 
	</p>

	</div>
</div>
</body>
</html>