package com.jemoji.models;

public class Message {
	private String toUser;
	private String fromUser;
	private Emoji emoji;
	private long id;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Message(long id, String toUser, String fromUser, Emoji emoji) {
		super();
		this.toUser = toUser;
		this.fromUser = fromUser;
		this.emoji = emoji;
		this.id = id;
	}

	public String getToUser() {
		return toUser;
	}

	public Message setToUser(String toUser) {
		this.toUser = toUser;
		return this;
	}

	public String getFromUser() {
		return fromUser;
	}

	public Message setFromUser(String fromUser) {
		this.fromUser = fromUser;
		return this;
	}

	public Emoji getEmoji() {
		return emoji;
	}

	public Message setEmoji(Emoji emoji) {
		this.emoji = emoji;
		return this;
	}

	@Override
	public String toString() {
		return "Message [toUser=" + toUser + ", fromUser=" + fromUser + ", emoji=" + emoji
				+ ", id=" + id + "]";
	}
	
	
}
