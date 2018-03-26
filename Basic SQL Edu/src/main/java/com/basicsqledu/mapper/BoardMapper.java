package com.basicsqledu.mapper;

import java.util.List;

import com.basicsqledu.vo.Board;


public interface BoardMapper
{
	public List<Board> list();
	public int write(Board board);
	public int delete(Board board);
	
}
