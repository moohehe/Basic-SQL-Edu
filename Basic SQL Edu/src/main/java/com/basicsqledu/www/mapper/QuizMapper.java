package com.basicsqledu.www.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import com.basicsqledu.www.vo.Animal;
import com.basicsqledu.www.vo.Person;
import com.basicsqledu.www.vo.Robots;

public interface QuizMapper
{
	//전체 데이터 뷰
	public ArrayList<Animal> getAnimal(int questionNumber);
	public ArrayList<Animal> getAnimal2(String table_name);
	public ArrayList<Person> getPerson(int questionNumber);
	public ArrayList<Person> getPerson2(String table_name);
	public ArrayList<Robots> getRobots(int questionNumber);
	public ArrayList<Robots> getRobot2(String table_name);

	//정답 뷰
	public ArrayList<Animal> getAnswerAnimal(int questionNumber);
	public ArrayList<Person> getAnswerPerson(int questionNumber);
	public ArrayList<Robots> getAnswerRobots(int questionNumber);

	
}
