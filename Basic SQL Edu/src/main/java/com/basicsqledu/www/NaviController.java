package com.basicsqledu.www;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.CookieGenerator;

import com.basicsqledu.www.dao.QuestextDao;
import com.basicsqledu.www.vo.Questext;
import com.google.gson.Gson;


/**
 * Handles requests for the application home page.
 */
@Controller
public class NaviController {
	
	private static final Logger logger = LoggerFactory.getLogger(NaviController.class);
	
	@Autowired
	QuestextDao dao; 
	
	/**
	 * 초기 페이지로 들어오는 경우.
	 */
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public String home(Model model, HttpServletResponse response, HttpServletRequest request) {
		Questext qt = new Questext();
		//쿠키 생성
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("currentStage"); //현재 스테이지(어디까지 풀었나)
		cg.addCookie(response, "1"); //일단 1부터 시작이므로 1을 넣어줌.
		
		for(int i=1; i<4; i++ ){ //일단 전체 스테이지 이름의 쿠키를 만들어 놓는다. 단, 값은 "none"으로.
			cg.setCookieName("completeStage"+i);
			cg.addCookie(response, "non-pass");
		}
		
		
		int lang=0;
		int stage=0;
		
		if(qt.getLvstatus()==0 && qt.getTextLang()==0){
			stage = 1;
			lang= 2;
			qt.setLvstatus(stage);
			qt.setTextLang(lang);
			System.out.println("처음 홈 들어올때"+qt);
		}else{
			qt.setLvstatus(qt.getLvstatus());
			qt.setTextLang(qt.getTextLang());
		}
		
		qt = dao.selectLang(qt);
		
		ArrayList<Questext> stageList = dao.selectStageAll(qt);
		
		model.addAttribute("questext", qt);
		model.addAttribute("stageList", stageList);
		
		return "test";
	}
	
	//언어 변환 버튼 및 이전/다음 버튼 누를때, 전체 스테이지 맵에서 이동할 때 동작(비동기식)
	@ResponseBody
	@RequestMapping(value = "/langcheck", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public String langcheck(HttpServletRequest request,HttpServletResponse response, 
			String lang, String stage, @RequestParam(defaultValue="non-pass")String compl) {
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
		
		//여기로 올 때마다 쿠키의 현재 스테이지 값을 변경해주어야 한다.
		CookieGenerator cg = new CookieGenerator();
		cg.setCookieName("currentStage"); //현재 스테이지(어디까지 풀었나)
		cg.addCookie(response, stage); 
		
		if(compl.equals("pass")){
			int cstage = Integer.parseInt(stage)-1;
			cg.setCookieName("completeStage"+cstage);
			cg.addCookie(response, "pass"); 
		}
		
		//DB에서 해당 언어의 문제관련 택스트들을 가져옴.
		qt.setLvstatus(Integer.parseInt(stage));
		qt.setTextLang(Integer.parseInt(lang));
		qt = dao.selectLang(qt);
		
		naviContentMap.put("questext", qt); //vo 맵에 저장 (text내용들, 언어선택, 현레벨status포함)

		//사용자 배열 쿠키로 읽어오기.
		Cookie cks[] = request.getCookies();
		
		for(Cookie c : cks){
			if(c.getValue().equals("pass")){ 
				//문제를 완료한 스테이지 쿠키만 들어옴.
				naviContentMap.put(c.getName(),c.getValue()); //완료한 스테이지들 맵에 저장.
			}
		}
		
		//맵 변환 후 보내기.
		Gson gson = new Gson();
		String json = gson.toJson(naviContentMap);
		
		return json;
	}
	
	
}
