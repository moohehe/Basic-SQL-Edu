package com.basicsqledu.www;


import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.basicsqledu.www.dao.CertDAO;
import com.basicsqledu.www.vo.Certification;

@Controller
public class CertifyController {
	
	@Autowired
	CertDAO certDAO;

	@RequestMapping(value = "goCertify", method=RequestMethod.GET)
	public String getTable() {
		return "certify/certification";
	}



	@RequestMapping(value = "certify", method=RequestMethod.POST)
	public String certify(String cert_name, String cert_email) {
		//일련 번호 생성하고 넣자
		String cert;
		String numStr = "1";
		String plusNumStr = "1";
		Random random = new Random();
		int result = 0;
		
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
		
		
		certDAO.insertCert(certify);
		
		
		return "redirect:certification?"+certify.getCert_user();
	}

}
