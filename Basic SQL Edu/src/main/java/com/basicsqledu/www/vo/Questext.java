package com.basicsqledu.www.vo;

public class Questext {
	private String qstext; //문제
    private int lvstatus; //레벨
    private String qstype; //문제 타입
    private String qsdetail; //문제 세부내용
    private String qsExm; //예시
    private int textLang; //언어
	
    public Questext() {
		super();
	}
	public Questext(String qstext, int lvstatus, String qstype, String qsdetail, String qsExm, int textLang) {
		super();
		this.qstext = qstext;
		this.lvstatus = lvstatus;
		this.qstype = qstype;
		this.qsdetail = qsdetail;
		this.qsExm = qsExm;
		this.textLang = textLang;
	}
	public String getQstext() {
		return qstext;
	}
	public void setQstext(String qstext) {
		this.qstext = qstext;
	}
	public int getLvstatus() {
		return lvstatus;
	}
	public void setLvstatus(int lvstatus) {
		this.lvstatus = lvstatus;
	}
	public String getQstype() {
		return qstype;
	}
	public void setQstype(String qstype) {
		this.qstype = qstype;
	}
	public String getQsdetail() {
		return qsdetail;
	}
	public void setQsdetail(String qsdetail) {
		this.qsdetail = qsdetail;
	}
	public String getQsExm() {
		return qsExm;
	}
	public void setQsExm(String qsExm) {
		this.qsExm = qsExm;
	}
	public int getTextLang() {
		return textLang;
	}
	public void setTextLang(int textLang) {
		this.textLang = textLang;
	}
	@Override
	public String toString() {
		return "Questext [qstext=" + qstext + ", lvstatus=" + lvstatus + ", qstype=" + qstype + ", qsdetail=" + qsdetail
				+ ", qsExm=" + qsExm + ", textLang=" + textLang + "]";
	}
    
    
}
