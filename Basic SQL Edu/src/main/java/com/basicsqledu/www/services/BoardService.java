package com.basicsqledu.www.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basicsqledu.www.dao.BoardDAO;
import com.basicsqledu.www.vo.Board;

@Repository
public class BoardService
{
	
	@Autowired
	BoardDAO boarddao;

	public List<Board> list()
	{
		List<Board> list = null;
		
		list = boarddao.list();
		
		return list;
	}

	public int write(Board board)
	{
		int result = 0;
		
		result = boarddao.insert(board);
		
		return result;
	}

	public int delete(Board board)
	{
		int result = 0;
		
		result = boarddao.delete(board);
		
		return result;
	}
	
	
	
	
}
