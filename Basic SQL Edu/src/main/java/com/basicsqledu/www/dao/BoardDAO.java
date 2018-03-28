package com.basicsqledu.www.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basicsqledu.www.mapper.BoardMapper;
import com.basicsqledu.www.vo.Board;

@Repository
public class BoardDAO
{

	@Autowired
	SqlSession session;
	
	
	
	public int insert(Board board)
	{
		int result = 0;
		try
		{
			BoardMapper mapper = session.getMapper(BoardMapper.class);
			result = mapper.write(board);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public int delete(Board board)
	{
		int result = 0;
		try
		{
			BoardMapper mapper = session.getMapper(BoardMapper.class);
			result = mapper.delete(board);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public List<Board> list()
	{
		List<Board> list = null;
		try
		{
			BoardMapper mapper = session.getMapper(BoardMapper.class);
			list = mapper.list();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	
	
}
