package com.basicsqledu.www.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import com.basicsqledu.www.vo.Animal;

public interface QuizMapper
{
	public ArrayList<Animal> getAnimal(int questionNumber);
	public ArrayList<Animal> getAnimal2(String table_name);
}
