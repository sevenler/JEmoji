
package com.jemoji.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class MessageCenter {
	private static MessageCenter intsance;

	public static MessageCenter instance() {
		if (intsance == null) intsance = new MessageCenter();
		return intsance;
	}

	private HashMap<String, List<Emoji>> emojis;

	private MessageCenter() {
		emojis = new LinkedHashMap<String, List<Emoji>>();
	}

	public void pushUnread(String user, Emoji emoji) {
		List<Emoji> message = emojis.get(user);
		if(message == null) message = new LinkedList<Emoji>();
		message.add(emoji);
		
		emojis.put(user, message);
	}
	
	public int getUnreadCount(){
		int count = 0;
		for(List<Emoji> values : emojis.values()){
			count += values.size();
		}
		return count;
	}
	
	public List<Emoji> pokeUnread(String user){
		return getUnread(user, true);
	}
	
	public List<Emoji> getUnread(String user, boolean delete){
		List<Emoji> message = emojis.get(user);
		if(delete) {
			emojis.remove(user);
		}
		return message;
	}
}
