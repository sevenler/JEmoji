
package com.jemoji.models;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.os.Environment;

import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;
import com.jemoji.http.URLs;

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

	// 添加未读消息
	private void pushUnread(String user, Emoji emoji) {
		System.out.println(String.format(" push message to %s ", user));
		List<Emoji> message = emojis.get(user);
		if (message == null) message = new LinkedList<Emoji>();
		message.add(emoji);

		emojis.put(user, message);
	}

	// 获取未读消息数量
	public int getUnreadCount() {
		int count = 0;
		for (List<Emoji> values : emojis.values()) {
			count += values.size();
		}
		System.out.println(String.format(" unread message count: %s ", count));
		return count;
	}

	// 获取有未读消息的用户
	public String getTopUser() {
		Set<String> users = emojis.keySet();
		for (String user : users) {
			List<Emoji> message = emojis.get(user);
			if (message != null && message.size() > 0) {
				return user;
			}
		}
		return null;
	}

	// 将消息标记为已读
	public boolean pokeUnread(String user, Emoji emoji) {
		List<Emoji> message = emojis.get(user);
		if (message != null) {
			return message.remove(emoji);
		}
		return false;
	}

	// 获取用户的未读表情
	public List<Emoji> getUnread(String user, boolean delete) {
		List<Emoji> message = emojis.get(user);
		if (delete) {
			emojis.remove(user);
		}
		return message;
	}

	public void onReceiveMessage(String cotent) {
		//处理接收到的消息
		String[] messages = cotent.split(",");
		String voiceUrl = URLs.getAbsoluteUrl(String.format("/%s", messages[0]));
		String name = messages[1];
		final Emoji emoji = new Emoji("", "", voiceUrl);
		emoji.setImageUrl(String.format("http://emoji.b0.upaiyun.com/test/%s", name));
		emoji.setBackground(Integer.parseInt(messages[2]));
		final String username = messages[3];

		String type = name.substring(name.lastIndexOf(".") + 1, name.length());
		String image = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
				+ "emojis_download" + File.separator + System.currentTimeMillis() + "." + type;

		onReceiving(emoji);
		GKHttpInterface.genFile(emoji.getImageUrl(), type, image, new GKJsonResponseHandler() {
			@Override
			public void onResponse(int code, Object file, Throwable error) {
				emoji.setImage((String)file);
				pushUnread(username, emoji);
				
				onDownload(emoji, (String)file);
			}
		});
	}
	
	public List<OnReceiveMessageDelegate> messageDelegate = new LinkedList<MessageCenter.OnReceiveMessageDelegate>();
	public void regesterReceiveMessageDelegate(OnReceiveMessageDelegate delegate){
		messageDelegate.add(delegate);
	}
	
	public boolean unregesterReceiveMessageDelegate(OnReceiveMessageDelegate delegate){
		return messageDelegate.remove(delegate);
	}
	
	private void onReceiving(Emoji emoji){
		for(OnReceiveMessageDelegate delegate : messageDelegate){
			delegate.onReceiveMessage(emoji);
		}
	}
	
	private void onDownload(Emoji emoji, String file){
		for(OnReceiveMessageDelegate delegate : messageDelegate){
			delegate.onDownloadMessage(emoji, file);
		}
	}
	
	public static interface OnReceiveMessageDelegate{
		public void onReceiveMessage(Emoji emoji);
		public void onDownloadMessage(Emoji emoji, String file);
	}
}
