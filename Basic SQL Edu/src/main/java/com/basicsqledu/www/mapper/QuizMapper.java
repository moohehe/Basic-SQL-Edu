package com.basicsqledu.www.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import com.basicsqledu.www.vo.Animal;

public interface QuizMapper
{
	public ArrayList<Animal> getAnimal2();
	public ArrayList<Animal> getAnimal(String table_name);
	public int getAnimal3(String talbe_name);
	
}
