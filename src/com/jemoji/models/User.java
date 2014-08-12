package com.jemoji.models;

public class User {
	private String user;
	private String nickname;
	private int header;
	
	public User(String username, String nickname, int header) {
		super();
		this.user = username;
		this.nickname = nickname;
		this.header = header;
	}
	
	public String getUsername() {
		return user;
	}
	public void setUsername(String username) {
		this.user = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getHeader() {
		return header;
	}
	public void setHeader(int header) {
		this.header = header;
	}
}
