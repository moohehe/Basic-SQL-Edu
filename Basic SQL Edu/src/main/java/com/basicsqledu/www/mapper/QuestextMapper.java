package com.basicsqledu.www.mapper;

import java.util.ArrayList;

import com.basicsqledu.www.vo.Questext;

public interface QuestextMapper {
	
	
	public Questext selectLang(Questext qt);

	public ArrayList<Questext> selectStageAll(Questext qt);
	
}
