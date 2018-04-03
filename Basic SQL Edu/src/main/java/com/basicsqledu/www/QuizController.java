package com.basicsqledu.www;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.basicsqledu.www.dao.QuizDAO;

// QuizController 는 컴파일러 관련 페이지 이동
@Controller
public class QuizController
{
	
	@Autowired
	QuizDAO quizdao;
	
	/**
	 * getTable 테이블 갖고 나오기
	 * @return
	 */
	@RequestMapping(value = "getTable", method=RequestMethod.GET)
	public String getTable() {
		// 임시로 세팅 - 나중에 parameter로 받아올것
		String table_name = "animal_view";
		
		HashMap<String, Object> data = null;
		
		data = quizdao.getTable(table_name);
		
		System.out.println("요기");
		System.out.println(data.get("table_value"));
		
		return "test";
	}
	

}
