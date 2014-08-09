
package com.jemoji.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferManager {
	private static PreferManager instance = null;

	public static PreferManager instance() {
		if (instance == null) {
			synchronized (PreferManager.class) {
				if (instance == null) {
					instance = new PreferManager();
				}
			}
		}
		return instance;
	}

	private PreferManager() {
	}

	private static final String DEFAULT_SHARED_NAME = "jemoji-shared";
	private static final int DEFAULT_SHARED_MODEL = Context.MODE_WORLD_WRITEABLE;
	public static final String GUOKU_DEFAULT_PREFERENCE = "com.jemoji_preferences";

	public SharedPreferences getPrefs(Context context) {
		return getPrefs(context, DEFAULT_SHARED_NAME, DEFAULT_SHARED_MODEL);
	}

	public SharedPreferences getPrefs(Context context, String name, int mode) {
		return context.getSharedPreferences(name, mode);
	}

	public int getIntFromPrefs(Context context, String key, int def) {
		return getIntFromPrefs(context, DEFAULT_SHARED_NAME, key, def);
	}

	public int getIntFromPrefs(Context context, String name, String key, int def) {
		if (key == null) {
			return def;
		}
		SharedPreferences pref = getPrefs(context, name, Context.MODE_WORLD_WRITEABLE);
		return pref.getInt(key, def);
	}

	public long getLongFromPrefs(Context context, String key, long def) {
		return getLongFromPrefs(context, DEFAULT_SHARED_NAME, key, def);
	}

	public long getLongFromPrefs(Context context, String name, String key, long def) {
		if (key == null) {
			return def;
		}
		SharedPreferences pref = getPrefs(context, name, Context.MODE_WORLD_WRITEABLE);
		return pref.getLong(key, def);
	}

	public boolean getBooleanFromPrefs(Context context, String key, boolean def) {
		return getBooleanFromPrefs(context, DEFAULT_SHARED_NAME, key, def);
	}

	public boolean getBooleanFromPrefs(Context context, String name, String key, boolean def) {
		SharedPreferences pref = getPrefs(context, name, Context.MODE_WORLD_WRITEABLE);
		return pref.getBoolean(key, def);
	}

	public String getStringFromPrefs(Context context, String key, String def) {
		return getStringFromPrefs(context, DEFAULT_SHARED_NAME, key, def);
	}

	public String getStringFromPrefs(Context context, String name, String key, String def) {
		SharedPreferences pref = getPrefs(context, name, Context.MODE_WORLD_WRITEABLE);
		return pref.getString(key, def);
	}

	public void setIntToPrefs(Context context, String key, int value) {
		setIntToPrefs(context, DEFAULT_SHARED_NAME, key, value);
	}

	public void setIntToPrefs(Context context, String name, String key, int value) {
		SharedPreferences pref = getPrefs(context, name, Context.MODE_WORLD_WRITEABLE);
		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void setLongToPrefs(Context context, String key, long value) {
		setLongToPrefs(context, DEFAULT_SHARED_NAME, key, value);
	}

	public void setLongToPrefs(Context context, String name, String key, long value) {
		SharedPreferences pref = getPrefs(context, name, Context.MODE_WORLD_WRITEABLE);
		Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public void setStringToPrefs(Context context, String key, String value) {
		setStringToPrefs(context, DEFAULT_SHARED_NAME, key, value);
	}

	public void setStringToPrefs(Context context, String name, String key, String value) {
		SharedPreferences pref = getPrefs(context, name, Context.MODE_WORLD_WRITEABLE);
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void setBooleanToPrefs(Context context, String key, boolean value) {
		setBooleanToPrefs(context, DEFAULT_SHARED_NAME, key, value);
	}

	public void setBooleanToPrefs(Context context, String name, String key, boolean value) {
		SharedPreferences pref = getPrefs(context, name, Context.MODE_WORLD_WRITEABLE);
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

}
