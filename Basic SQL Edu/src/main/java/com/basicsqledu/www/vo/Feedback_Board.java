package com.basicsqledu.www.vo;

public class Feedback_Board {
	
	private int fb_no;
	private String fb_user;
	private String email;
	private String title;
	private String content;
	private String fb_indate;
	private int status;
	private String memo;
	
	public Feedback_Board() {
		super();
	}

	public Feedback_Board(int fb_no, String fb_user, String email, String title, String content, String fb_indate,
			int status, String memo) {
		super();
		this.fb_no = fb_no;
		this.fb_user = fb_user;
		this.email = email;
		this.title = title;
		this.content = content;
		this.fb_indate = fb_indate;
		this.status = status;
		this.memo = memo;
	}

	public int getFb_no() {
		return fb_no;
	}

	public void setFb_no(int fb_no) {
		this.fb_no = fb_no;
	}

	public String getFb_user() {
		return fb_user;
	}

	public void setFb_user(String fb_user) {
		this.fb_user = fb_user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFb_indate() {
		return fb_indate;
	}

	public void setFb_indate(String fb_indate) {
		this.fb_indate = fb_indate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "Feedback_Board [fb_no=" + fb_no + ", fb_user=" + fb_user + ", email=" + email + ", title=" + title
				+ ", content=" + content + ", fb_indate=" + fb_indate + ", status=" + status + ", memo=" + memo + "]";
	}
	
	
	
	
	
}
