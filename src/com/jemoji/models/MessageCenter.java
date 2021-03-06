
package com.jemoji.models;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.jemoji.R;
import com.jemoji.http.GKHttpInterface;
import com.jemoji.http.GKJsonResponseHandler;
import com.jemoji.http.URLs;
import com.jemoji.utils.ErrorCenter;

public class MessageCenter {
	private static MessageCenter intsance;

	public static MessageCenter instance(Context context) {
		if (intsance == null) intsance = new MessageCenter(context);
		return intsance;
	}

	DataBaseWrapper db_wrapper;
	NotificationViocePlayer mNotificationViocePlayer;
	
	private MessageCenter(Context context) {
		db_wrapper = new DataBaseWrapper(context);
		mNotificationViocePlayer = new NotificationViocePlayer(context);
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
		
		cancalNotification(context);
		
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
		String messageCotent = messages[0];
		
		final Emoji emoji = new Emoji("", "", "");
		if(messageCotent.startsWith("voice:")){
			String voiceUrl = URLs.getAbsoluteUrl(String.format("/%s", messageCotent.substring(messageCotent.indexOf("voice:") + 6, messageCotent.length())));
			emoji.setVoiceUrl(voiceUrl);
		}else{
			String text = messageCotent.substring(messageCotent.indexOf("text:") + 5, messageCotent.length());
			emoji.setText(text);
		}
		
		String name = messages[1];
		emoji.setImageUrl(URLs.getAbsoluteUrl(String.format("/%s", name)));
		emoji.setBackground(Integer.parseInt(messages[2]));
		final String username = messages[3];

		String type = name.substring(name.lastIndexOf(".") + 1, name.length());
		String image = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
				+ "emojis_download" + File.separator + System.currentTimeMillis() + "." + type;

		onReceiving(context, emoji);
		GKHttpInterface.genFile(emoji.getImageUrl(), type, image, new GKJsonResponseHandler() {
			@Override
			public void onResponse(int code, Object file, Throwable error) {
				if(error == null){
					emoji.setImage((String)file);
					pushUnread(context, username, emoji);
					onDownloadFinish(context, emoji, (String)file);
				}else{
					onDownloadError(error);
					ErrorCenter.instance().onError(error);
				}
			}
		});
	}
	
	private static final int NOTIFICATION_INDEX = 1009;
	
	//发送通知栏提示 消息
	public void notificationMessage(Context context, int count) {
		context = context.getApplicationContext();

		Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.jemoji");
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new Notification(R.drawable.app_launcher, "收到新消息", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, String.format("你收到 %s 条消息", count), "点击查看消息", pendingNotificationIntent);

		NotificationManager mManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		mManager.notify(NOTIFICATION_INDEX, notification);
	}
	
	public void cancalNotification(Context context){
		NotificationManager mManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		mManager.cancel(NOTIFICATION_INDEX);
	}
	
	
	public List<OnReceiveMessageDelegate> messageDelegate = new LinkedList<MessageCenter.OnReceiveMessageDelegate>();
	public void regesterReceiveMessageDelegate(OnReceiveMessageDelegate delegate){
		messageDelegate.add(delegate);
	}
	
	public boolean unregesterReceiveMessageDelegate(OnReceiveMessageDelegate delegate){
		return messageDelegate.remove(delegate);
	}
	
	private void onReceiving(Context context, Emoji emoji){
		for(OnReceiveMessageDelegate delegate : messageDelegate){
			delegate.onReceiveMessage(emoji);
		}
	}
	
	private void onDownloadFinish(Context context, Emoji emoji, String file){
		int unread = getUnreadCount();
		notificationMessage(context, unread);
		mNotificationViocePlayer.play();
		
		for(OnReceiveMessageDelegate delegate : messageDelegate){
			delegate.onDownloadMessage(emoji, file);
		}
	}
	
	private void onDownloadError(Throwable error){
		for(OnReceiveMessageDelegate delegate : messageDelegate){
			delegate.onDownloadError(error);
		}
	}
	
	public static interface OnReceiveMessageDelegate{
		public void onReceiveMessage(Emoji emoji);
		public void onDownloadMessage(Emoji emoji, String file);
		public void onDownloadError(Throwable error);
	}
}
