
package com.jemoji.models;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;

public class EmojiSelector {
	private static EmojiSelector instance;

	public static EmojiSelector instance() {
		if (instance == null) instance = new EmojiSelector();
		return instance;
	}

	public List<Emoji> emojis = new LinkedList<Emoji>();

	public EmojiSelector() {
		emojis.add(new Emoji(getFullName("001.png"), Color.parseColor("#ffffff")));
		
//		emojis.add(new Emoji("", "http://emoji.b0.upaiyun.com/test/1407907183208.gif", Color.parseColor("#ffffff")));
//		emojis.add(new Emoji("", "http://emoji.b0.upaiyun.com/test/1407983737416.gif", Color.parseColor("#ffffff")));
//		emojis.add(new Emoji("", "http://emoji.b0.upaiyun.com/test/1407983767294.png", Color.parseColor("#ffffff")));
		
		emojis.add(new Emoji(getFullName("1002.gif"), Color.parseColor("#ffffff")));
		emojis.add(new Emoji(getFullName("1001.gif"), Color.parseColor("#ffffff")));
		emojis.add(new Emoji(getFullName("1003.png"), Color.parseColor("#ffffff")));
		emojis.add(new Emoji(getFullName("1004.gif"), Color.parseColor("#ffffff")));
		emojis.add(new Emoji(getFullName("1005.gif"), Color.parseColor("#ffffff")));

		emojis.add(new Emoji(getFullName("IMG_0272.JPG"), Color.parseColor("#ffffff")));
		emojis.add(new Emoji(getFullName("IMG_0278.JPG"), Color.parseColor("#FEFFBB")));
		emojis.add(new Emoji(getFullName("IMG_0284.JPG"), Color.parseColor("#ffffff")));
		emojis.add(new Emoji(getFullName("IMG_0267.JPG"), Color.parseColor("#ffffff")));

		emojis.add(new Emoji(getFullName("IMG_0291.JPG"), Color.parseColor("#AADFFF")));
		emojis.add(new Emoji(getFullName("IMG_0273.JPG"), Color.parseColor("#ffffff")));

		emojis.add(new Emoji(getFullName("despicable-me-2-Minion-icon-5.png"), Color.parseColor("#ffffff")));
		emojis.add(new Emoji(getFullName("IMG_0262.JPG"), Color.parseColor("#ffffff")));
		emojis.add(new Emoji(getFullName("IMG_0268.JPG"), Color.parseColor("#ffffff")));
	}

	private String getFullName(String name) {
		return String.format("%s/%s", "/sdcard/emojis/", name);
	}

	public int getEmojiBackground(int index) {
		return emojis.get(index).getBackground();
	}
	
	public Emoji get(int index){
		return emojis.get(index);
	}

	public int size() {
		return emojis.size();
	}
}
