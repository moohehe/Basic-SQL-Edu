package com.basicsqledu.www;

import java.util.List;

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
		
		return "";
	}
}
