<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<!DOCTYPE html>
<html lang="en">

  <head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    
    <title>BSE - Basic SQL Education</title>

    <!-- Bootstrap core CSS -->
    <link href="resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom fonts for this template -->
    <link href="resources/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet" type="text/css">

    <!-- Plugin CSS -->
    <link href="resources/vendor/magnific-popup/magnific-popup.css" rel="stylesheet" type="text/css">
	<link href="resources/css/title.css" type="text/css" rel="stylesheet">
	
    <!-- Custom styles for this template -->
    <link href="resources/css/freelancer.min.css" rel="stylesheet">

    <script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>

<script type="text/javascript">
$(function(){

	// last stage
	var value = $('#certiBtn').val();
	var url = $('#url').val();
	if (value=='true') {
		console.log('asdf');
		setTimeout(function() {
			location.href=url;
		},5000);
	}
	
	$('#goTest').on('click', function(){
	 var language = $('#langop').val(); //사용자가 select Box에서 선택한 언어.	
	 $('#selectedLang').val(language);
	 /* $(location).attr('href', "test?langop="+language); */
	 $('#enter').submit();
		
	});
	
	
	
	
	/* 영어
	header_text : With a simple and intuitive mini-game<br>everyone can easily learn the basic SQL syntax.
	story1 : When someone picked up the lost watch on the street, 
	you suddenly moved into this mysterious place. Suddenly, it becomes to time travel. 
	I hear a voice coming into my head ...
    story2 : "If you pass through a total 20 of quiz, I will send you home." 
	Let's solve all 20 SQL questions and return safely to the original world.
	*/
	
	/* 한국어
	header_text : 단순하고 직관적인 방식의 미니게임을 통해서<br> 누구나 쉽게 기초 SQL구문을 익힐 수 있습니다.
	story1 : 길에서 누군가가 잃어버린 시계를 주운 당신, 
    갑자기 시계의 초침과 분침이 빠르게 돌아가며 알 수 없는 이공간으로 빨려 들어가게 되었다.
    느닷없이 타임워프 하게 된 당신… 머리 속으로 갑자기 어떤 목소리가 들려오는데…
    story2 : “총 20가지의 관문을 통과하면 자네를 다시 집으로 보내주도록 하지.” 
	20개의 SQL 문제를 모두 풀고, 무사히 본래 세계로 돌아가도록 하자.
	*/
	
	/* 일본어
	header_text : 単純で直観的な方式のミニゲームを<br>通じて誰でも簡単に基礎SQL構文を身につけることができます。
	story1 : 道で誰かがなくした時計を拾ったあなた、急に時計の秒針と長針が早く回りながら、分からないこの空間に吸い込まれて入るようになってきた。 突然タイム・ワープすることになったあなた…頭の中で突然ある声が聞こえて来るのに…
    story2 : \"全部20つの問題を通過すれば、君をまた家に送ってあげてもいいわ\" 20個のSQL問題を全て解いて、無事に本来世界に戻るようにしよう。
	*/
	
	//메인 화면  언어 교환
	$("select#langop").change(function(){
		if($('#langop option:selected').val()==1){
			$('#header_text').html('Anyone can easily learn the basics of SQL <br> by playing through a simple and intuitive mini-game.');
			$('#story1').html('You have picked up a watch that someone had lost. All the sudden, the clock began to acclerate and you got sucked into a mysterious space. And a voice came direct into your head saying ');
			$('#story2').html('\"You must clear 20 trials if you wish to go back to the world where you belong<br>\"Let\'s beat 20 sql questions to get back home safe!');
		}else if($('#langop option:selected').val()==2){
			$('#header_text').html('단순하고 직관적인 방식의 미니게임을 통해서<br> 누구나 쉽게 기초 SQL구문을 익힐 수 있습니다.');
			$('#story1').html('길에서 누군가가 잃어버린 시계를 주운 당신, 갑자기 시계의 초침과 분침이 빠르게 돌아가며 알 수 없는 이공간으로 빨려 들어가게 되었다. 느닷없이 타임워프 하게 된 당신… 머리 속으로 갑자기 어떤 목소리가 들려오는데…');
			$('#story2').html('\“총 20가지의 관문을 통과하면 자네를 다시 집으로 보내주도록 하지.\” 20개의 SQL 문제를 모두 풀고, 무사히 본래 세계로 돌아가도록 하자.');
		}else if($('#langop option:selected').val()==3){
			$('#header_text').html('単純で直観的な方式のミニゲームを<br>通じて誰でも簡単に基礎SQL構文を身につけることができます。');
			$('#story1').html('道で誰かがなくした時計を拾ったあなた、急に時計の秒針と長針が早く回りながら、分からないこの空間に吸い込まれて入るようになってきた。 突然タイム・ワープすることになったあなた…頭の中で突然ある声が聞こえて来るのに…');
			$('#story2').html('\"全部20つの問題を通過すれば、君をまた家に送ってあげてもいいわ\" <br>20個のSQL問題を全て解いて、無事に本来世界に戻るようにしよう。');
		}
	});
	
	
});
</script>
<style type="text/css">
	.main_logo{
		
	}
</style>
  </head>

  <body id="page-top">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg bg-secondary fixed-top text-uppercase" id="mainNav">
      <div class="container">
        <a class="navbar-brand js-scroll-trigger" href="#page-top">◈Basic SQL Education</a>
        <button class="navbar-toggler navbar-toggler-right text-uppercase bg-primary text-white rounded" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
          Menu
          <i class="fa fa-bars"></i>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
          <ul class="navbar-nav ml-auto">
            <li class="nav-item mx-0 mx-lg-1">
              <a class = "nav-link py-3 px-0 px-lg-3 rounded js-scroll-trigger" href="#start" id="goTest">START</a>
            </li>
            <li class="nav-item mx-0 mx-lg-1">
              <a class="nav-link py-3 px-0 px-lg-3 rounded js-scroll-trigger" href="#about">Story</a>
            </li>
            <li class="nav-item mx-0 mx-lg-1">
              <p class="nav-link py-3 px-0 px-lg-3 rounded js-scroll-trigger" style="color: white; padding-left: 10%;">Select Language</p>
              </li>
              <li class="nav-item mx-0 mx-lg-1">
              <select id="langop" name="lang" style="font-size: 15px;" class="form-control">
						<option value="1" <c:if test="${selectedLang == 1}">selected</c:if>> English</option>
						<option value="2" <c:if test="${selectedLang == 2}">selected</c:if>> Korean</option>
						<option value="3" <c:if test="${selectedLang == 3}">selected</c:if>> Japanese</option>
					</select>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <!-- Header -->
    <header class="masthead bg-primary text-white text-center">
      <div class="container">
        <img class="img-fluid mb-5 d-block mx-auto" src="resources/image/profile.png" alt="">
		<h1 class="animate one"><span>B</span><span>a</span><span>s</span><span>i</span><span>c</span> <span>S</span><span>Q</span><span>L</span>
											<span>E</span><span>d</span><span>u</span><span>c</span><span>a</span><span>t</span><span>i</span><span>o</span><span>n</span></h1>
		<hr class="star-light">
        <h2 class="font-weight-light mb-0" id="header_text">단순하고 직관적인 방식의 미니게임을 통해서<br> 누구나 쉽게 기초 SQL구문을 익힐 수 있습니다.</h2>
      </div>
    </header> 
   

    <!-- Story Line Section -->
    <section class="bg-primary text-white mb-0" id="about">
      <div class="container">
        <h2 class="text-center text-uppercase text-white">Story</h2>
        <hr class="star-light mb-5">
        <div class="row">
          <div class="col-lg-4 ml-auto">
            <p class="lead" id="story1">길에서 누군가가 잃어버린 시계를 주운 당신, 
            갑자기 시계의 초침과 분침이 빠르게 돌아가며 알 수 없는 이공간으로 빨려 들어가게 되었다.
            느닷없이 타임워프 하게 된 당신… 머리 속으로 갑자기 어떤 목소리가 들려오는데…
          </div>
          <div class="col-lg-4 mr-auto">
            <p class="lead" id="story2">
    	“총 20가지의 관문을 통과하면 자네를 다시 집으로 보내주도록 하지.” 
    	20개의 SQL 문제를 모두 풀고, 무사히 본래 세계로 돌아가도록 하자.
    	   </div>
        </div>
        <div class="text-center mt-4">
          </div>
      </div>
    </section>

        
    <!-- 히든 폼 -->
    <form action="test" method="post" id="enter">
		<input type="hidden" id="selectedLang" name="langop" value="${selectedLang}">
	</form>
	<input type="hidden" id="certiBtn" value="${certiBtn}">
	<input type="hidden" id="url" value="${url}"> 

    <!-- Footer -->
    <footer class="footer text-center">
      <div class="container">
        <div class="row">
          <div class="col-md-4 mb-5 mb-lg-0">
            <h4 class="text-uppercase mb-4">Creator</h4>
            <p class="lead mb-0">J.H / C.S / Y.J
              <br>S.W / H.S</p>
          </div>
          <div class="col-md-4 mb-5 mb-lg-0">
			<h4 class="text-uppercase mb-4">CERTIFIED<br>BY</h4>
            <ul class="list-inline mb-0">
              <li class="list-inline-item">
				<img alt="" src="resources/image/main2.PNG" class = "main_logo">                
              </li>
              
            </ul>
          </div>
          <div class="col-md-4">
            <h4 class="text-uppercase mb-4">About BSE</h4>
            <p class="lead mb-0">Basic SQL Education is<br> open to everyone. 
            							<br>If you have any inquiries, <br>please feel free to contact us.<br>Email:
              <a href="mailto:chansu369@naver.com">chansu369@naver.com</a></p>
          </div>
        </div>
      </div>
    </footer>

    <div class="copyright py-4 text-center text-white">
      <div class="container">
        <small>Copyright &copy; BSE 2018</small>
      </div>
    </div>

    <!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->
    <div class="scroll-to-top d-lg-none position-fixed ">
      <a class="js-scroll-trigger d-block text-center text-white rounded" href="#page-top">
        <i class="fa fa-chevron-up"></i>
      </a>
    </div>

  
    <!-- Bootstrap core JavaScript -->
    <script src="resources/vendor/jquery/jquery.min.js"></script>
    <script src="resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Plugin JavaScript -->
    <script src="resources/vendor/jquery-easing/jquery.easing.min.js"></script>
    <script src="resources/vendor/magnific-popup/jquery.magnific-popup.min.js"></script>

    <!-- Contact Form JavaScript -->
    <script src="resources/js/home/jqBootstrapValidation.js"></script>
    <script src="resources/js/home/contact_me.js"></script>

    <!-- Custom scripts for this template -->
    <script src="resources/js/home/freelancer.min.js"></script>

  </body>

</html>
