
package com.jemoji;

public class Emoji {
	String image;
	String mVoice;

	public Emoji() {
		super();
	}

	public Emoji(String image, String voice) {
		super();
		this.image = image;
		this.mVoice = voice;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public String getVoice() {
		return mVoice;
	}

	public void setVoice(String voice) {
		this.mVoice = voice;
	}
}
