package com.basicsqledu.www.vo;

public class Board
{
	int bnumber;
	String id;
	String content;
	String inputdate;
	String reinputdate;
	
	public Board()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Board(int bnumber, String id, String content, String inputdate, String reinputdate)
	{
		super();
		this.bnumber = bnumber;
		this.id = id;
		this.content = content;
		this.inputdate = inputdate;
		this.reinputdate = reinputdate;
	}

	public int getBnumber()
	{
		return bnumber;
	}

	public void setBnumber(int bnumber)
	{
		this.bnumber = bnumber;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getInputdate()
	{
		return inputdate;
	}

	public void setInputdate(String inputdate)
	{
		this.inputdate = inputdate;
	}

	public String getReinputdate()
	{
		return reinputdate;
	}

	public void setReinputdate(String reinputdate)
	{
		this.reinputdate = reinputdate;
	}

	@Override
	public String toString()
	{
		return "Board [bnumber=" + bnumber + ", id=" + id + ", content=" + content + ", inputdate=" + inputdate
				+ ", reinputdate=" + reinputdate + "]";
	}
	
	
	
	
}
