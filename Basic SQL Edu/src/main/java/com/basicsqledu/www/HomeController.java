package com.basicsqledu.www;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.CookieGenerator;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	CookieGenerator cg = new CookieGenerator();
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		//쿠키 읽어서 언어 설정 변경해주기.
		
		Cookie cks[] = request.getCookies();
		if(cks != null && cks.length>1){
			for(Cookie c : cks){
				if(c.getName().equals("currentLang")){
					model.addAttribute("selectedLang", c.getValue());
				}else{
					
					cg.setCookieName("currentLang");//현재 언어(무슨 언어인지)
					cg.addCookie(response, "2"); //홈에서 받아온 값을 넣어준다.(쿠키가 없으므로)
					cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
				}
			}
		}else{
			cg.setCookieName("currentLang");//현재 언어(무슨 언어인지)
			cg.addCookie(response, "2"); //홈에서 받아온 값을 넣어준다.(쿠키가 없으므로)
			cg.setCookieMaxAge(72*60*60); //유효시간 3일 설정.
		}
		return "home";
		
	}
	@RequestMapping(value = "intro", method = RequestMethod.GET)
	public String tointro(Model model) {				
		System.out.println("여기");
		return "intro";
	}
	
}
	

