package com.basicsqledu.www;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController
{
	
	@ResponseBody
	@RequestMapping(value = "getTime", method= RequestMethod.GET
			, produces = "application/text; charset=utf8")
	public String getTime(Locale locale, HttpServletResponse response) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		response.setContentType("text/html;charset=UTF-8");
		String result = dateFormat.format(date);
		
		System.out.println(result);
		
		return result;
	}
}
