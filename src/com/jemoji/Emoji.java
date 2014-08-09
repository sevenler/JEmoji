
package com.jemoji;

import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;

public class Emoji {
	String image;
	String mVoice;
	String mVoiceUrl;
	int mVoiceStatus = STATUS_REMOTE;
	
	public static final int STATUS_REMOTE = 0;
	public static final int STATUS_DOWNLOADING = 1;
	public static final int STATUS_LOCAL = 2;

	public Emoji() {
		super();
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
			public void onResponse(int code, Object json, Throwable error) {
				setVoiceStatus(Emoji.STATUS_LOCAL);
				handler.onResponse(code, json, error);
			}
		});
	}
}
