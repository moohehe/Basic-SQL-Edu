package com.basicsqledu.www.vo;

public class Certification {

	 private int cert_no;
	 private String cert_user;
	 private String cert_email;
	 private String cert_indate;
	 
	public Certification() {
		super();
	}
	public Certification(int cert_no, String cert_user, String cert_email, String cert_indate) {
		super();
		this.cert_no = cert_no;
		this.cert_user = cert_user;
		this.cert_email = cert_email;
		this.cert_indate = cert_indate;
	}
	public int getCert_no() {
		return cert_no;
	}
	public void setCert_no(int cert_no) {
		this.cert_no = cert_no;
	}
	public String getCert_user() {
		return cert_user;
	}
	public void setCert_user(String cert_user) {
		this.cert_user = cert_user;
	}
	public String getCert_email() {
		return cert_email;
	}
	public void setCert_email(String cert_email) {
		this.cert_email = cert_email;
	}
	public String getCert_indate() {
		return cert_indate;
	}
	public void setCert_indate(String cert_indate) {
		this.cert_indate = cert_indate;
	}
	@Override
	public String toString() {
		return "Certification [cert_no=" + cert_no + ", cert_user=" + cert_user + ", cert_email=" + cert_email
				+ ", cert_indate=" + cert_indate + "]";
	}
	
	
}
