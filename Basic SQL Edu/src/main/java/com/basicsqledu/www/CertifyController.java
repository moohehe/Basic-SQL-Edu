package com.basicsqledu.www;


import java.util.Random;

<<<<<<< HEAD
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

=======
>>>>>>> branch 'master' of https://github.com/moohehe/Basic-SQL-Edu.git
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.basicsqledu.www.dao.CertDAO;
import com.basicsqledu.www.vo.Certification;

@Controller
public class CertifyController {
<<<<<<< HEAD

	private static final Logger logger = LoggerFactory.getLogger(CertifyController.class);
	
=======
	private static final Logger logger = LoggerFactory.getLogger(CertifyController.class);

>>>>>>> branch 'master' of https://github.com/moohehe/Basic-SQL-Edu.git
	@Autowired
	CertDAO certDAO;

	@RequestMapping(value = "goCertify", method=RequestMethod.GET)
	public String getTable(HttpServletRequest request, Model model) {
		logger.info("start of Certfiform");
		// 기본 언어는 1로 세팅함
		model.addAttribute("lang",1);
		Cookie cg[] = request.getCookies();
		for (Cookie c : cg) {
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
		
		return "certify/certifyForm";
	}

	@RequestMapping(value = "certify", method=RequestMethod.POST)
	public String certify(String cert_name, String cert_email) {
		//일련 번호 생성하고 넣자
		String cert;
		String numStr = "1";
		String plusNumStr = "1";
		Random random = new Random();
		int result = 0;
		
		
		if(cert_email == null || !(cert_email.equals(""))){
			return "certify/certifyForm";
		}
		
		
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
		Certification certify = new Certification();
		certify.setCert_user(cert);
		certify.setCert_email(cert_email);
		
		System.out.println(certify);
		
		certDAO.insertCert(certify);
		
		return "redirect:gocertification?user="+cert_name + "&email="+cert_email + "&cert_no="+certify.getCert_user();
	}
	

	@RequestMapping(value = "gocertification", method=RequestMethod.GET, produces = "application/text; charset=utf8")
	public String gocertification(String cert_no, String user, String email, Model model) {
		logger.info("String user:{}",user);
		Certification cert = new Certification();
		
		cert.setCert_user(cert_no);
		cert.setCert_email(email);
		cert = certDAO.selectCert(cert);
		
		model.addAttribute("cert",cert);
		model.addAttribute("user",user);
		System.out.println("cert : " + cert + "/ user : " + user);
		return "certify/certification";
	}
	

}
