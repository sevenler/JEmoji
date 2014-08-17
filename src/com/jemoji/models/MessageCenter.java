
package com.jemoji.models;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Environment;

import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;
import com.jemoji.http.URLs;

public class MessageCenter {
	private static MessageCenter intsance;

	public static MessageCenter instance(Context context) {
		if (intsance == null) intsance = new MessageCenter(context);
		return intsance;
	}

	DataBaseWrapper db_wrapper;

	private MessageCenter(Context context) {
		db_wrapper = new DataBaseWrapper(context);
	}

	// 添加未读消息
	private void pushUnread(Context context , String user, Emoji emoji) {
		db_wrapper.insertMessage(new Message(0, null, user, emoji));
	}

	// 获取未读消息数量
	public int getUnreadCount() {
		int count = db_wrapper.getAllMessage(null).size();
		return count;
	}

	// 获取有未读消息的用户
	public String getTopUser() {
		List<String> list = db_wrapper.getAllFromUser();
		return list.get(0);
	}

	// 将消息标记为已读
	public boolean pokeUnread(Context context, String user, Emoji emoji) {
		db_wrapper.deleteMessageWithEmoji(emoji);
		return false;
	}

	// 获取用户的未读表情
	public List<Emoji> getUnread(String from_user) {
		List<Emoji> message = new LinkedList<Emoji>();
		List<Message> list = db_wrapper.getAllMessage(from_user);
		for(Message me : list){
			message.add(me.getEmoji());
		}
		return message;
	}

	public void onReceiveMessage(final Context context, String cotent) {
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
				pushUnread(context, username, emoji);
				
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
