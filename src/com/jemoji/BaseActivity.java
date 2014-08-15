package com.jemoji;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class BaseActivity extends FragmentActivity{
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

	public void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}
	
	protected void setTag(String tag) {
		Set<String> tagSet = new LinkedHashSet<String>();
		if (ExampleUtil.isValidTagAndAlias(tag)) {
			tagSet.add(tag);
		}
		JPushInterface.setAliasAndTags(getApplicationContext(), null, tagSet,
				new TagAliasCallback() {
					@Override
					public void gotResult(int code, String alias, Set<String> tags) {
						String logs;
						switch (code) {
							case 0:
								logs = "Set tag and alias success";
								break;
							case 6002:
								logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
								break;
							default:
								logs = "Failed with errorCode = " + code;
						}
						System.out.println(String.format(" logs: %s ", logs));
					}
				});
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
