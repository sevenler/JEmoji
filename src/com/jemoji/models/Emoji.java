
package com.jemoji.models;

import android.graphics.Bitmap;

import com.jemoji.FileUploader;

public class Emoji {
	String image;
	String imageUrl;
	Bitmap imageBitmap;
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

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}
	
	public void setBitmap(Bitmap image) {
		this.imageBitmap = image;
	}
	
	public int getBackground() {
		return background;
	}
	
	public void setBackground(int background) {
		this.background = background;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}
	
	public void setImageUrl(String image) {
		this.imageUrl = image;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getVoice() {
		return mVoice;
	}

	public void setVoice(String voice) {
		this.mVoice = voice;
	}
	
	public String getVoiceUrl() {
		return mVoiceUrl;
	}

	public void setVoiceUrl(String voice) {
		this.mVoiceUrl = voice;
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
		sb.append(String.format("%s:%s", "voice", mVoice));
		sb.append("}");
		return sb.toString();
	}
}
