
package com.jemoji;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;

import com.jemoji.models.Emoji;

public class EmojiSelector {
	private static EmojiSelector instance;

	public static EmojiSelector instance() {
		if (instance == null) instance = new EmojiSelector();
		return instance;
	}

	public List<Emoji> emojis = new LinkedList<Emoji>();

	public EmojiSelector() {
		emojis.add(new Emoji("001.png", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("1002.gif", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("1001.gif", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("1003.png", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("1004.gif", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("1005.gif", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("IMG_0272.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0278.JPG", Color.parseColor("#FEFFBB")));
		emojis.add(new Emoji("IMG_0284.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0267.JPG", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("IMG_0291.JPG", Color.parseColor("#AADFFF")));
		emojis.add(new Emoji("IMG_0273.JPG", Color.parseColor("#ffffff")));

		emojis.add(new Emoji("despicable-me-2-Minion-icon-5.png", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0262.JPG", Color.parseColor("#ffffff")));
		emojis.add(new Emoji("IMG_0268.JPG", Color.parseColor("#ffffff")));
	}

	public String getEmojiName(int index) {
		return String.format("%s/%s", "/sdcard/emojis", emojis.get(index).getImage());
	}

	public int getEmojiBackground(int index) {
		return emojis.get(index).getBackground();
	}

	public int size() {
		return emojis.size();
	}
}
