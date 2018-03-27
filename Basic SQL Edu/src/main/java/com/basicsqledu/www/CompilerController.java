package com.basicsqledu.www;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.basicsqledu.www.vo.SQLCompiler;

@Controller
public class CompilerController
{
	private SQLCompiler compiler = new SQLCompiler();
	
	@ResponseBody
	@RequestMapping(value="sqlCompiler", method = RequestMethod.POST)
	public String compiler(String sql) {
		//System.out.println("sql="+sql);
		compiler.setText(sql);
		
		return "success!!!";
	}
	
	
}
