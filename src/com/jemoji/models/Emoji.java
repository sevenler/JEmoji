
package com.jemoji.models;

import com.jemoji.FileUploader;

public class Emoji {
	String image;
	String imageUrl;
	String mVoice;
	String mVoiceUrl;
	int background = -1;

	public Emoji() {
		super();
	}

	public Emoji(String image, int background) {
		super();
		this.image = image;
		this.background = background;
	}
	
	public Emoji(String image, String voice, String voiceUrl) {
		super();
		this.image = image;
		this.mVoice = voice;
		this.mVoiceUrl = voiceUrl;
	}
	
	public Emoji(String image, String imageUrl, int background) {
		super();
		this.image = image;
		this.imageUrl = imageUrl;
	}

	public Emoji setImage(String image) {
		this.image = image;
		return this;
	}

	public String getImage() {
		return image;
	}
	
	public int getBackground() {
		return background;
	}
	
	public Emoji setBackground(int background) {
		this.background = background;
		return this;
	}
	
	public Emoji setImageUrl(String image) {
		this.imageUrl = image;
		return this;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getVoice() {
		return mVoice;
	}

	public Emoji setVoice(String voice) {
		this.mVoice = voice;
		return this;
	}
	
	public String getVoiceUrl() {
		return mVoiceUrl;
	}

	public Emoji setVoiceUrl(String voice) {
		this.mVoiceUrl = voice;
		return this;
	}
	
	public void send(final String toChatUser){
		System.out.println(String.format(" send emoji %s ", this));
		new FileUploader().send(this, toChatUser);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(String.format("%s:%s,", "image", image));
		sb.append(String.format("%s:%s,", "imageurl", imageUrl));
		sb.append(String.format("%s:%s,", "voice", mVoice));
		sb.append(String.format("%s:%s", "voiceUrl", mVoiceUrl));
		sb.append("}");
		return sb.toString();
	}
}
