package com.basicsqledu.www.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basicsqledu.www.dao.QuizDAO;


@Repository
public class QuizServices
{
	@Autowired
	QuizDAO quizDAO;
	
	
	public HashMap<String,Object> compiler() {
		HashMap<String,Object> result = new HashMap<String, Object>();
		return result;
	}
}
