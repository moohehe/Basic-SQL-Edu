package com.basicsqledu.www;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CompilerController
{
	
	@ResponseBody
	@RequestMapping(value="sqlCompiler", method = RequestMethod.POST)
	public String compiler(String sql) {
		System.out.println("sql="+sql);
		return "success!!!";
	}
	
	
}
