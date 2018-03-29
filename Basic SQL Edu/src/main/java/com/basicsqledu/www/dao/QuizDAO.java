package com.basicsqledu.www.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basicsqledu.www.mapper.QuizMapper;
import com.basicsqledu.www.vo.Animal;

@Repository
public class QuizDAO
{
	@Autowired
	SqlSession session;
	
	public HashMap<String, Object> getTable(String table_name){
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try
		{
			QuizMapper mapper = session.getMapper(QuizMapper.class);
			switch (table_name) {
			case "animal_view" : 
				ArrayList<Animal> animals = mapper.getAnimal();
				result.put("table_name", "animal_view");
				result.put("table_value", animals);
				break;
			case "person_view" :
				result.put("table_name","person_view");
				result.put("table_value","persons 라는 테이블 ArrayList");
				break;
				
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
}
