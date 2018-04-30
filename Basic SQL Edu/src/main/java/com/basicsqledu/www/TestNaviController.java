package com.basicsqledu.www;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.CookieGenerator;

import com.basicsqledu.www.dao.QuestextDao;
import com.basicsqledu.www.dao.QuizDAO;
import com.basicsqledu.www.vo.Animal;
import com.basicsqledu.www.vo.Person;
import com.basicsqledu.www.vo.Questext;
import com.basicsqledu.www.vo.Robots;
import com.basicsqledu.www.vo.SQLCompiler;
import com.google.gson.Gson;


/**
 * Handles requests for the application home page.
 */
@Controller
public class TestNaviController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestNaviController.class);
	
	@Autowired
	QuestextDao dao;
	@Autowired
	QuizDAO quizdao;
	@Autowired
	private SQLCompiler compiler;
	
	CookieGenerator cg = new CookieGenerator();
	
	/**
	 * 초기 페이지로 들어오는 경우.
	 */
	@RequestMapping(value = "test", method = {RequestMethod.POST, RequestMethod.GET})
	public String test(Model model, HttpServletResponse response, HttpServletRequest request, 
			@RequestParam(defaultValue="1") String langop) {
		
		Questext qt = new Questext();
		int lang=0; //언어
		int stage=0; //단계(레벨)
		
		//사용자 배열 쿠키로 읽어오기.
		Cookie cks[] = request.getCookies();
		if(cks != null && cks.length>1){ //이미 사용자 아이디가 하나 들어가므로 length의 초기는 1이다.
			System.out.println("requestmapping= test에서 쿠키 읽어따!");
			//쿠키 있으면 읽기
			for(Cookie c : cks){
				//저장된 현재 스테이지 정보가 있으면 이를 vo에 넣음.
				
				if(c.getName().equals("currentStage")){
					if(c.getValue() == null || c.getValue().equals("")){
						stage = 1;
						cg.setCookieName("currentStage"); //현재 스테이지(어디까지 풀었나)
						cg.addCookie(response, "1");
					}
					else{
						stage = Integer.parseInt(c.getValue());
						System.out.println("쿠키에서 초기에 읽은 stage"+stage);
					}
					qt.setLvstatus(stage);
				}else{
					cg.setCookieName("currentStage"); //현재 스테이지(어디까지 풀었나)
					cg.addCookie(response, "1");
				}
				if(c.getName().equals("currentLang")){
					if(c.getValue() == null || c.getValue().equals("") || c.getValue().equals(" ")){
						cg.setCookieName("currentLang");//현재 언어(무슨 언어인지)
						cg.addCookie(response, "2");
					}
					else{
						lang = Integer.parseInt(c.getValue());
						langop = c.getValue();
						System.out.println("쿠키에서 초기에 읽은 lang"+lang);
					}
					qt.setTextLang(Integer.parseInt(langop));
				}else{
					cg.setCookieName("currentLang");//현재 언어(무슨 언어인지)
					cg.addCookie(response, langop);
				}
			}
			
		}else{
			//쿠키없으면 쿠키 생성
			System.out.println("requestmapping= test에서 쿠키 없단다!");
			
			cg.setCookieName("currentStage"); //현재 스테이지(어디까지 풀었나)
			cg.addCookie(response, "1"); //일단 1부터 시작이므로 1을 넣어줌.
			cg.setCookieName("currentLang");//현재 언어(무슨 언어인지)
			cg.addCookie(response, langop); //홈에서 받아온 값을 넣어준다.(쿠키가 없으므로)
			cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
		
			for(int i=1; i<21; i++ ){ //일단 전체 스테이지 이름의 쿠키를 만들어 놓는다. 단, 값은 "non-pass"로.
				cg.setCookieName("completeStage"+i);
				cg.addCookie(response, "non-pass");
			}
		
			//아무 값이 없는 경우, 기본 영어와 레벨1을 넣는다.
				stage = 1;
				//lang= 2;
				qt.setLvstatus(stage);
				qt.setTextLang(Integer.parseInt(langop));
		}
		
		System.out.println("처음 홈 들어올때"+qt);
		
		//DB에서 해당 문제 택스트 및 언어에 대한 정보를 읽어옴.
		qt = dao.selectLang(qt);
		//DB에서 문제 전체에 대한 정보를 불러옴. (얘는 stage 전체 단계에서 쓰임)
		ArrayList<Questext> stageList = dao.selectStageAll(qt);
		
		
		model.addAttribute("questext", qt);
		model.addAttribute("stageList", stageList);
		
		return "test";
	}
	
	//언어 변환 버튼 및 이전/다음 버튼 누를때, 전체 스테이지 맵에서 이동할 때 동작(비동기식)
	@ResponseBody
	@RequestMapping(value = "langcheck", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public String langcheck(HttpServletRequest request,HttpServletResponse response, 
			String lang, String stage, @RequestParam(defaultValue="non-pass")String compl,
			@RequestParam(defaultValue = "2") int questionNumber) {
		// setup UTF-8  
		response.setContentType("text/html;charset=UTF-8"); 
		
		//jsp로 보낼 데이터를 모두 담은 해쉬맵 생성. (전체내용포함된 VO, 완료한 스테이지 등)
		HashMap<String, Object> naviContentMap = new HashMap<String, Object>();
		//DB에서 온 데이터 담을 vo생성.
		Questext qt = new Questext();
		
		//일단 오류방지를 위해? lang과 stage 값이 넘어오지 않는 경우 기본값으로 처리. (테스트 후 정상작동시 삭제요망)
		if(lang==null && stage == null){
			lang = "1";
			stage = "1";
		}
		/*//사용자 배열 쿠키로 읽어오기.
				Cookie cks[] = request.getCookies();
				if(cks != null && cks.length>1){ //이미 사용자 아이디가 하나 들어가므로 length의 초기는 1이다.
					//쿠키 있으면 읽기
					for(Cookie c : cks){
						//저장된 현재 스테이지 정보가 있으면 이를 vo에 넣음.
						
						if(c.getName().equals("currentStage")){
							stage = c.getValue();
							qt.setLvstatus(Integer.parseInt(stage));
						}
						if(c.getName().equals("currentLang")){
							lang = c.getValue();
							qt.setTextLang(Integer.parseInt(lang));
						}
						if(c.getValue().equals("pass")){ 
							//문제를 완료한 스테이지 쿠키만 들어옴.
							naviContentMap.put(c.getName(),c.getValue()); //완료한 스테이지들 맵에 저장.
						}
					}
				}*/ 
		//여기로 올 때마다 쿠키의 현재 스테이지 값을 변경해주어야 한다.
		
		cg.setCookieName("currentStage"); //현재 스테이지(어디까지 풀었나)
		cg.addCookie(response, stage); 
		cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
		
				
		//현재 언어 값에 대한 변경(쿠키)
		cg.setCookieName("currentLang");
		cg.addCookie(response, lang);
		cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
		
		//DB에서 해당 언어의 문제관련 택스트들을 가져옴.
		qt.setLvstatus(Integer.parseInt(stage));
		qt.setTextLang(Integer.parseInt(lang));
		qt = dao.selectLang(qt);
		
		naviContentMap.put("questext", qt); //vo 맵에 저장 (text내용들, 언어선택, 현레벨status포함)

		
			
		questionNumber = Integer.parseInt(stage);
		
		String table_key = null;
		if(questionNumber >1 && questionNumber<=11){
			table_key = "animal_view";
		}else if(questionNumber >11 && questionNumber<=16){
			table_key = "person_view";
		}else{
			table_key = "robot_view";
		}
		
		//DB에서 문제 그림 관련 정보를 가져옴.
		//문제 그림 관련 정보 읽어오기.
		HashMap<String, Object> quizData = null;
		//지금 현재 없는 문제 뷰. (1번, 11번, 15, 16, 17, 19, 20)
				if(qt.getLvstatus() == 1 || qt.getLvstatus() == 11 || qt.getLvstatus() == 15
					|| qt.getLvstatus() == 16 || qt.getLvstatus() == 17 || qt.getLvstatus() == 19 || qt.getLvstatus() == 20 ){
					
					
				}else{
					//문제 뷰가 있는 애들만 해당 DAO의 함수에서 문제를 불러온다.
					quizData = quizdao.getTable(qt.getLvstatus()); //Map으로 return
					
					if(quizData.get("table_name").equals("animal_view")){
						
						ArrayList<Animal> animalqlist = (ArrayList<Animal>) quizData.get("table_value");
						//jsp에서 꺼낼때에는 jpg파일명을 animal의 경우, species와 color를 합쳐놓을것.
						naviContentMap.put("qlist", animalqlist);
						
					}
					else if(quizData.get("table_name").equals("person_view")){
						ArrayList<Person> personqlist = (ArrayList<Person>) quizData.get("table_value");
						naviContentMap.put("qlist", personqlist);
						
					}else if(quizData.get("table_name").equals("robots_view")){
						ArrayList<Robots> robotsqlist = (ArrayList<Robots>) quizData.get("table_value");
						naviContentMap.put("qlist", robotsqlist);
					}
					//System.out.println(quizData.get("table_value"));
				
					//정답 뷰 가져온후 화면으로 전달
					compiler.setQuestionNumber(questionNumber, table_key);
					String [][] temp = compiler.getAnswerTable();
					ArrayList<String> anserView = new ArrayList<>();
					
					System.out.println(temp.length);
					System.out.println(temp[0].length);
					
					System.out.println("========== 테스트 정답 뷰 셋팅(TestNavi) ===========");
					if(temp.length != 0){
						for(int j= 1;j<temp.length;j++){
							anserView.add(temp[j][0]);
						}
					}
					naviContentMap.put("ansList", anserView);
					
				}

		//System.out.println("qlist="+naviContentMap.get("qlist").toString());
				
				
		
		
		//맵 변환 후 보내기.
		Gson gson = new Gson();
		String json = gson.toJson(naviContentMap);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value="cookieCtrl", method=RequestMethod.POST)
	public String cookieCtrl(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,	String lang, String stage, String cookieCon){
		//jsp로 보낼 데이터를 모두 담은 해쉬맵 생성. (전체내용포함된 VO, 완료한 스테이지 등)
		HashMap<String, Object> naviContentMap = new HashMap<String, Object>();
		//DB에서 온 데이터 담을 vo생성.
		Questext qt = new Questext();
		session.invalidate();
		
		if(cookieCon.equals("complete")){
			
			for(int i=1; i<21; i++ ){ //일단 전체 스테이지 이름의 쿠키를 만들어 놓는다. 단, 값은 "pass"로.
				cg.setCookieName("completeStage"+i);
				cg.addCookie(response, "pass");
			}
			
			cg.setCookieName("currentStage"); //현재 스테이지(어디까지 풀었나)
			cg.addCookie(response, "20"); 
			cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
					
			//현재 언어 값에 대한 변경(쿠키)
			cg.setCookieName("currentLang");
			cg.addCookie(response, lang);
			cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
			
			//DB에서 해당 언어의 문제관련 택스트들을 가져옴.
			qt.setLvstatus(2);
			qt.setTextLang(Integer.parseInt(lang));
			qt = dao.selectLang(qt);
			
		}else{//쿠키 전체 초기화할 경우.
			
			for(int i=1; i<21; i++ ){ //일단 전체 스테이지 이름의 쿠키를 만들어 놓는다. 단, 값은 "non-pass"로.
				cg.setCookieName("completeStage"+i);
				cg.addCookie(response, "non-pass");
			}
			cg.setCookieName("currentStage"); //현재 스테이지(어디까지 풀었나)
			cg.addCookie(response, "1"); 
			cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
					
			//현재 언어 값에 대한 변경(쿠키)
			cg.setCookieName("currentLang");
			cg.addCookie(response, lang);
			cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
			
			//DB에서 해당 언어의 문제관련 택스트들을 가져옴.
			qt.setLvstatus(1);
			qt.setTextLang(Integer.parseInt(lang));
			qt = dao.selectLang(qt);
		}
		
		
		
		
		naviContentMap.put("questext", qt);
			
		
		//맵 변환 후 보내기.
		Gson gson = new Gson();
		String json = gson.toJson(naviContentMap);
		System.out.println("return of json="+json);
		return json;
	}
	
	
}
