package com.basicsqledu.www.vo;

public class Person {
	private int rownum;
	private String th_code;
	private String de_code;
	
	private String hair_color; 
	private String job;
	private String height;
	private String gender;
	
	
	public Person() {
		super();
	}
	public Person(int rownum, String th_code, String de_code, String hair_color, String job, String height,
			String gender) {
		super();
		this.rownum = rownum;
		this.th_code = th_code;
		this.de_code = de_code;
		this.hair_color = hair_color;
		this.job = job;
		this.height = height;
		this.gender = gender;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public String getTh_code() {
		return th_code;
	}
	public void setTh_code(String th_code) {
		this.th_code = th_code;
	}
	public String getDe_code() {
		return de_code;
	}
	public void setDe_code(String de_code) {
		this.de_code = de_code;
	}
	public String getHair_color() {
		return hair_color;
	}
	public void setHair_color(String hair_color) {
		this.hair_color = hair_color;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return "Person [rownum=" + rownum + ", th_code=" + th_code + ", de_code=" + de_code + ", hair_color="
				+ hair_color + ", job=" + job + ", height=" + height + ", gender=" + gender + "]";
	}
	
	
}
