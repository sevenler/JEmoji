package com.jemoji.models;

public class User {
	private String username;
	private int header;
	
	public User(String username, int header) {
		super();
		this.username = username;
		this.header = header;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getHeader() {
		return header;
	}
	public void setHeader(int header) {
		this.header = header;
	}
}
