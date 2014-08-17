
package com.jemoji.models;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Environment;

public class EmojiSelector {
	private static EmojiSelector instance;

	public static EmojiSelector instance(Context mContext) {
		if (instance == null) instance = new EmojiSelector(mContext);
		return instance;
	}
	
	public static String getFullUrl(String name) {
		return String.format("%s/%s", "http://emoji.b0.upaiyun.com/test", name);
	}
	
	public String getFullEmojiPath(String name) {
		return String.format("%s/%s", emojiCachePath, name);
	}
	
	public String getEmojiCachePath(){
		return emojiCachePath;
	}

	DataBaseWrapper db_wrapper;
	String emojiCachePath;
	
	public EmojiSelector(Context mContext) {
		db_wrapper = new DataBaseWrapper(mContext);
		emojiCachePath =  Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "jemoji" + File.separator + "emojis";
	}
	
	public boolean isOfficialEmojiExsit(){
		return (db_wrapper.getAllEmoji(Emoji.EMOJI_TYPE_OFFICAL).size() != 0);
	}

	public Emoji getOfficial(long id){
		return db_wrapper.getEmoji(id);
	}

	public int officialSize() {
		return db_wrapper.getAllEmoji(Emoji.EMOJI_TYPE_OFFICAL).size();
	}
	
	public Emoji getCollect(long id){
		return db_wrapper.getEmoji(id);
	}

	public int collectSize() {
		return db_wrapper.getAllEmoji(Emoji.EMOJI_TYPE_COLLECT).size();
	}
	
	public boolean addCollect(Emoji emoji){
		db_wrapper.insertEmoji(emoji);
		return true;
	}
	
	public List<Emoji> getEmojiData(int type) {
		List<Emoji> list = new LinkedList<Emoji>();

		switch (type) {
			case Emoji.EMOJI_TYPE_COLLECT:
				list.addAll(db_wrapper.getAllEmoji(Emoji.EMOJI_TYPE_COLLECT));
				break;

			default:
				list.addAll(db_wrapper.getAllEmoji(Emoji.EMOJI_TYPE_OFFICAL));
				break;
		}
		return list;
	}
}
