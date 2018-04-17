package com.basicsqledu.www.vo;

public class Robots {
	private int rownum;
	private String th_code;
	private String de_code;
	
	private String r_size;
	private String r_color; 
	private String r_type;
	private String weapon;
	
	
	public Robots() {
		super();
	}
	public Robots(int rownum, String th_code, String de_code, String r_size, String r_color, String r_type,
			String weapon) {
		super();
		this.rownum = rownum;
		this.th_code = th_code;
		this.de_code = de_code;
		this.r_size = r_size;
		this.r_color = r_color;
		this.r_type = r_type;
		this.weapon = weapon;
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
	public String getR_size() {
		return r_size;
	}
	public void setR_size(String r_size) {
		this.r_size = r_size;
	}
	public String getR_color() {
		return r_color;
	}
	public void setR_color(String r_color) {
		this.r_color = r_color;
	}
	public String getR_type() {
		return r_type;
	}
	public void setR_type(String r_type) {
		this.r_type = r_type;
	}
	public String getWeapon() {
		return weapon;
	}
	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
	@Override
	public String toString() {
		return "Robots [rownum=" + rownum + ", th_code=" + th_code + ", de_code=" + de_code + ", r_size=" + r_size
				+ ", r_color=" + r_color + ", r_type=" + r_type + ", weapon=" + weapon + "]";
	}
	
	
	
}
