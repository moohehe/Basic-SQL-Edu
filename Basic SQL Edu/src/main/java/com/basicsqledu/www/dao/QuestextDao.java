package com.basicsqledu.www.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basicsqledu.www.mapper.QuestextMapper;
import com.basicsqledu.www.vo.Questext;

@Repository
public class QuestextDao {
	
	
	@Autowired
	SqlSession sqlsession;
	
	
	public Questext selectLang(Questext qt){ //언어 변환. 1문제에 대한 내용만 가져옴.
		
		QuestextMapper mapper = sqlsession.getMapper(QuestextMapper.class);
		
		qt = mapper.selectLang(qt);
		
		return qt;
	}
	
public ArrayList<Questext> selectStageAll(Questext qt){ //전체 문제를 가져옴. 언어는 기본값으로.
		
		QuestextMapper mapper = sqlsession.getMapper(QuestextMapper.class);
		
		
		ArrayList<Questext> stageList = null;
		
		stageList = mapper.selectStageAll(qt);
		
		return stageList;
	}

}
