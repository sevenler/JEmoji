
package com.jemoji.models;

import android.graphics.Bitmap;

import com.jemoji.FileUploader;
import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;

public class Emoji {
	String image;
	String imageUrl;
	Bitmap imageBitmap;
	String mVoice;
	String mVoiceUrl;
	int mVoiceStatus = STATUS_REMOTE;
	int background = -1;
	
	public static final int STATUS_REMOTE = 0;
	public static final int STATUS_DOWNLOADING = 1;
	public static final int STATUS_LOCAL = 2;
	public static final int STATUS_MEMORY = 3;

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
	
	public int getVoiceStatus() {
		return mVoiceStatus;
	}
	
	public void setVoiceStatus(int status) {
		this.mVoiceStatus = status;
	}
	
	public void download(final GKJsonResponseHandler handler){
		setVoiceStatus(Emoji.STATUS_DOWNLOADING);
		GKHttpInterface.genFile(getVoiceUrl(), "amr", new GKJsonResponseHandler() {
			@Override
			public void onResponse(int code, Object file, Throwable error) {
				setVoiceStatus(Emoji.STATUS_LOCAL);
				handler.onResponse(code, file, error);
			}
		});
	}
	
	public void send(final String user){
		new FileUploader().send(this, user);
	}
}
