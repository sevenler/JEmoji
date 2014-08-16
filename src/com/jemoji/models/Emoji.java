
package com.jemoji.models;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.hipmob.gifanimationdrawable.GifAnimationDrawable;
import com.jemoji.FileUploader;
import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;
import com.jemoji.image.FileImageDecoder;
import com.jemoji.image.ImageSize;
import com.jemoji.image.ImageDecoder.ImageScaleType;
import com.jemoji.utils.Utility;

public class Emoji {
	long id;
	String image;
	String imageUrl;
	String mVoice;
	String mVoiceUrl;
	int background = -1;
	
	public long getId() {
		return id;
	}

	public Emoji setId(long id) {
		this.id = id;
		return this;
	}

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

	public Emoji(long id, String image, String imageUrl, String mVoice, String mVoiceUrl,
			int background) {
		super();
		this.id = id;
		this.image = image;
		this.imageUrl = imageUrl;
		this.mVoice = mVoice;
		this.mVoiceUrl = mVoiceUrl;
		this.background = background;
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
	
	public void showEmoji(final ImageView imageView){
		showEmoji(imageView, true);
	}
	
	public void showEmoji(final ImageView imageView, final boolean showGif){
		String filename = (String)getImage();
		System.out.println(String.format(" filename:%s ", filename));
		if(!Utility.Strings.isEmptyString(filename) && new File(filename).exists()){
			showFile(imageView, filename, showGif);
			System.out.println(String.format(" =======exists========= "));
		}else{
			filename = (String)getImageUrl();
			String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			System.out.println(String.format(" =======no no no  %s========= ", type));
			String image = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ File.separator + "emojis_download" + File.separator
					+ System.currentTimeMillis() + "." + type;
			GKHttpInterface.genFile(getImageUrl(), type, image, new GKJsonResponseHandler() {
				@Override
				public void onResponse(int code, Object file, Throwable error) {
					System.out.println(String.format(" file:%s ", file));
					showFile(imageView, (String)file, showGif);
				}
			});
		}
	}
	
	private static void showFile(ImageView imageView, String filename, boolean showGif){
		if(filename.endsWith(".gif")){
			System.out.println(String.format("GIF Image %s ", filename));
			
			if(showGif){
				try {
					GifAnimationDrawable little = new GifAnimationDrawable(new File(filename), false);
					little.setOneShot(false);
					imageView.setImageDrawable(little);
					little.setVisible(true, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				try {
					FileImageDecoder decoder = new FileImageDecoder(new File(filename));
					Bitmap bitmap = decoder.decode(new ImageSize(510, 510), ImageScaleType.POWER_OF_2);
					imageView.setImageBitmap(bitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			System.out.println(String.format("Image %s ", filename));
			try {
				FileImageDecoder decoder = new FileImageDecoder(new File(filename));
				Bitmap bitmap = decoder.decode(new ImageSize(510, 510), ImageScaleType.POWER_OF_2);
				imageView.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
