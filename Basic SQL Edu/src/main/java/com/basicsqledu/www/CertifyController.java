package com.basicsqledu.www;


import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.CookieGenerator;

import com.basicsqledu.www.dao.CertDAO;
import com.basicsqledu.www.vo.Certification;
import com.google.gson.Gson;

@Controller
public class CertifyController {
	private static final Logger logger = LoggerFactory.getLogger(CertifyController.class);

	@Autowired
	CertDAO certDAO;

	CookieGenerator cg = new CookieGenerator();

	// certification information input form
	@RequestMapping(value = "goCertify", method=RequestMethod.GET)
	public String getTable(HttpServletRequest request, Model model) {
		logger.info("start of Certfiform");
		// 기본 언어는 1로 세팅함
		model.addAttribute("lang",1);

		Cookie ck[] = request.getCookies();

		if(ck != null){
			for (Cookie c : ck) {
				if(c.getName().equals("currentLang")){
					try {
						int lang = Integer.valueOf(c.getValue());
						// 언어 설정이 되어있으면 그 언어로 세팅
						model.addAttribute("lang", lang);
					} catch (Exception e) {
						model.addAttribute("lang",1);
					}
				}
			}
		}
		return "certify/certifyForm";
	}

	
	
	
	// certifyForm에서 받은 값을 insert 해줌
	@RequestMapping(value = "certify", method=RequestMethod.POST)
	public String certify(String cert_name, String cert_email) {
		logger.info("cert_name:{}, cert_email:{}",cert_name, cert_email);
		
		
		//일련 번호 생성하고 넣자
		String cert;
		String numStr = "1";
		String plusNumStr = "1";
		Random random = new Random();
		int result = 0;
		Certification certify = new Certification();

		if(cert_email == null || cert_email.equals("")){
			System.out.println("여기야?");
			return "certify/certifyForm";
		}
		int count = 0;
		int count2 = 0;
		while(count2 == 0) {
			//난수 생성
			for (int i = 0; i < 8; i++) {
				numStr += "0";

				if (i != 8 - 1) {
					plusNumStr += "0";
				}
			}

			result = random.nextInt(Integer.parseInt(numStr)) + Integer.parseInt(plusNumStr);

			if (result > Integer.parseInt(numStr)) {
				result = result - Integer.parseInt(plusNumStr);
			}

			cert = "BSE"+result;		//일련 번호 생성

			//DB 갔다오자

			certify.setCert_user(cert);
			certify.setCert_email(cert_email);

			System.out.println(certify);

			count2 = certDAO.insertCert(certify);
			count++;
			if (count > 10 ) {
				count2 = 0;
				break;
			}
		}
		if (certify == null || count == 0 ) {
			logger.info("erorr : DB에 업데이트가 안됨");
			return "redirect:goCertify";
		}
		logger.info("DB에 사용자 추가됨 : " + certify);
		return "redirect:gocertification?cert_user="+cert_name + "&cert_email="+cert_email;
	}

	// read certification
	@RequestMapping(value = "gocertification", method=RequestMethod.GET, produces = "application/text; charset=utf8")
	public String gocertification(Certification cert, Model model) {
		logger.info("String user:{}",cert);
		/*Certification cert = new Certification();*/

		cert = certDAO.selectCert(cert);

		model.addAttribute("cert",cert);
		model.addAttribute("user",cert.getCert_user());
		System.out.println("cert : " + cert + "/ user : " + cert.getCert_user());
		return "certify/certification";
	}
	
	
	// read certification
	@RequestMapping(value = "readCerti", method = RequestMethod.POST)
	public String searchCertification(Model model, String email,HttpServletResponse response) {
		logger.info("start of readCerti() email : {}",email);
		
		
		Certification cert = new Certification();
		cert = certDAO.searchCert(email);
		
		for(int i=1; i<21; i++ ){ //일단 전체 스테이지 이름의 쿠키를 만들어 놓는다. 단, 값은 "non-pass"로.
			cg.setCookieName("completeStage"+i);
			cg.addCookie(response, "non-pass");
		}
		model.addAttribute("cert",cert);
		model.addAttribute("user",cert.getCert_user());
		return "certify/certification";
	}

	@ResponseBody
	@RequestMapping(value = "certi-search", method =RequestMethod.POST)
	public String searchCerti(String text) {
		logger.info("start of certi-search, text: {}",text);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("is",false);
		Certification cert = null;
		try {
			cert = certDAO.searchCert(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if( cert != null) {
			map.put("is",true);
			map.put("cert_email",cert.getCert_email());
			map.put("cert_no",cert.getCert_no());
			map.put("cert_user",cert.getCert_user());
			map.put("url","gocertification?cert_email="
			+cert.getCert_email()+"&cert_user="+cert.getCert_user()
			+"&cert_no="+cert.getCert_no());
		}
		Gson gson = new Gson();
		String json = gson.toJson(map);
		System.out.println("json : " + json);
		return json; 
	}


}
