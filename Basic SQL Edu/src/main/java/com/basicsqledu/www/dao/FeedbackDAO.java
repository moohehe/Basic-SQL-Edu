package com.basicsqledu.www.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basicsqledu.www.mapper.FeedbackMapper;
import com.basicsqledu.www.util.PageNavigator;
import com.basicsqledu.www.vo.Feedback_Board;

@Repository
public class FeedbackDAO {
	@Autowired
	SqlSession sqlSession;
	
	
	public int insertBoard(Feedback_Board board)
	{
		int result = 0;
		FeedbackMapper mapper = sqlSession.getMapper(FeedbackMapper.class);
	
		try{
			result = mapper.insertBoard(board);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}	
	
	public ArrayList<Feedback_Board> selectBoardAll(HashMap<String, Object>searchMap,int startRecord,int countPerPage){
		ArrayList<Feedback_Board> boardlist = null;
		FeedbackMapper mapper = sqlSession.getMapper(FeedbackMapper.class);
		
		// page 네비게이터 생성
		
				
		
		
		RowBounds rb = new RowBounds(startRecord, countPerPage);
		
		
		
		try{
			boardlist = mapper.selectBoardAll(searchMap, rb);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return boardlist;
	}
	
	public Feedback_Board selectBoardOne(int fb_no){
		Feedback_Board board = null;
		FeedbackMapper mapper = sqlSession.getMapper(FeedbackMapper.class);
		
		try{
			mapper.updateHits(fb_no);
			board = mapper.selectBoardOne(fb_no);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return board;
	}
	
	public int getTotal(HashMap<String, Object> searchMap){
		
		int result = 0;
		FeedbackMapper mapper = sqlSession.getMapper(FeedbackMapper.class);
		try{
			result = mapper.getTotal(searchMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public void updateMemo(Feedback_Board board){
		
		FeedbackMapper mapper = sqlSession.getMapper(FeedbackMapper.class);
		
		try{
			mapper.updateMemo(board);
			
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}

	public int updateStatus(Feedback_Board board) {
		int result = 0;
		FeedbackMapper mapper = sqlSession.getMapper(FeedbackMapper.class);
		
		try{
			result = mapper.updateStatus(board);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
}
