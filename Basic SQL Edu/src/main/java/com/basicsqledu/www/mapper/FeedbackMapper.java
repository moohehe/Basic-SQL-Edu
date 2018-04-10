package com.basicsqledu.www.mapper;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;

import com.basicsqledu.www.vo.Feedback_Board;

public interface FeedbackMapper {

	public int insertBoard(Feedback_Board board);
	
	public Feedback_Board selectBoardOne(int fb_no);
	
	public void updateHits(int fb_no);

	public ArrayList<Feedback_Board> selectBoardAll(HashMap<String, Object> searchMap, RowBounds rb);

	public int getTotal(HashMap<String, Object> searchMap);
	
	public ArrayList<Feedback_Board> listBoard(String searchText, RowBounds rb);

	public void insertMemo(Feedback_Board board);
	
}
