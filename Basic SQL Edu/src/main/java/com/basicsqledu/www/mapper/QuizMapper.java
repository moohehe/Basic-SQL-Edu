package com.basicsqledu.www.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import com.basicsqledu.www.vo.Animal;
import com.basicsqledu.www.vo.Person;
import com.basicsqledu.www.vo.Robots;

public interface QuizMapper
{
	public ArrayList<Animal> getAnimal(int questionNumber);
	public ArrayList<Animal> getAnimal2(String table_name);
	public ArrayList<Person> getPerson(int questionNumber);
	public ArrayList<Robots> getRobots(int questionNumber);
}
