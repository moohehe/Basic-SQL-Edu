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
	
	
	
	
	private final static String[] ANIMALS = {"animal","tiger","bird","lion","rabbit","fish"};
	private final static String[] ROBOTS = {"robot"};
	private final static String[] PERSONS = {"person"};
	
	
	public HashMap<String, Object> getTable(String table_name){
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try
		{
			QuizMapper mapper = session.getMapper(QuizMapper.class);
			switch (table_name) {
			case "animal_view" : 
				ArrayList<Animal> animals = mapper.getAnimal2();
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
	
	
	public String[][] getTables(String table_key) {
		System.out.println("들어가기는 하는가?");
		table_key = table_key.toLowerCase();
		String type = "";
		String[][] table = null;
		
		
		for (String s : ANIMALS) {
			if (table_key.contains(s)) {
				type = "animal";
				break;
			}
		}
		for (String s : ROBOTS) {
			if (table_key.contains(s)) {
				type = "robot";
				break;
			}
		}

		for (String s : PERSONS) {
			if (table_key.contains(s)) {
				type = "person";
				break;
			}
		}
		
		
		
		
		System.out.println("여기까지 왔는가?");
		switch (type) {
		case "animal":
			try
			{
				QuizMapper mapper = session.getMapper(QuizMapper.class);
				ArrayList<Animal> list = mapper.getAnimal(table_key+"_view");
				if (list == null)
				{
					return null;
				}

				if (list.size() == 0)
				{
					table = new String[0][0];
					return null;
				}

				// Animal 타입의 데이터면
				if (list.get(0) instanceof Animal)
				{
					int col = 6, row = list.size();
					table = new String[row + 1][col];
					// 테이블 속성(attribute) 명칭 입력
					table[0][0] = "animal_size";
					table[0][1] = "animal_species";
					table[0][2] = "animal_legs";
					table[0][3] = "animal_color";
					table[0][4] = "animal_habitat";
					table[0][5] = "animal_feed";

					int i = 1;
					for (Object a : list)
					{
						Animal animal = (Animal) a;
						table[i][0] = animal.getAnimal_size();
						table[i][1] = animal.getAnimal_species();
						table[i][2] = animal.getAnimal_legs();
						table[i][3] = animal.getAnimal_habitat();
						table[i][4] = animal.getAnimal_feed();
						table[i][5] = animal.getAnimal_size();

						i++;
					}

				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			break;
		case "robots":
			break;
		case "person":
			break;
		}
		
		
		
		
		return table;
	}


	
	
}
