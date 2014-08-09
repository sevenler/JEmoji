package com.jemoji;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.jpush.android.api.JPushInterface;

public class BaseActivity extends FragmentActivity{
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		registerMessageReceiver();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterMessageReceiver();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}
	
	public void unregisterMessageReceiver(){
		unregisterReceiver(mMessageReceiver);
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " :-------------" + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}

				onReceiveMessage(messge);
			}
		}
	}
	
	public void onReceiveMessage(String values){
	}
	
	public void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}
	
	
	
	private static Map<String, Object> mValues = new LinkedHashMap<String, Object>();//用于传递数据，static变量，记得一定要清理哦！！
    public static void putValus(String key, Object values){
    	mValues.put(key, values);
    }
    
    /**
     * 默认阅后即焚哦
     * @param key
     * @return
     */
    public static Object pokeValus(String key){
    	return getValus(key, true);
    }
    
    private static Object getValus(String key, boolean isNeedDelete){
    	Object values = mValues.get(key);
    	if(isNeedDelete)mValues.remove(key);
    	return values;
    }
}
