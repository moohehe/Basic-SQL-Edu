package com.basicsqledu.www;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.basicsqledu.www.services.BoardService;
import com.basicsqledu.www.vo.Board;

@Controller
public class TestController
{
	@Autowired
	BoardService boardService;

	@ResponseBody
	@RequestMapping(value = "dbtest", method= RequestMethod.GET)
	public String dbTest() {
		List<Board> list = null;
		System.out.println("dbtest들어옴");
		list = boardService.list();
		if (list == null) {
			return "null";
		}
		for ( Board b : list ) {
			System.out.println(b);
		}
		System.out.println("end of dbtest");
		return "test";
	}
	
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
