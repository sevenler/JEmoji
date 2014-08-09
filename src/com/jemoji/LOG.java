package com.jemoji;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class LOG {
	private static final String PREFIX_FORMAT = "<%s>%s";
	public static int mLogLevel = Log.DEBUG;
	
	public static final String TAG_API = "guoku.api";
	public static final String TAG_REFRESH = "guoku.refresh";
	public static final String TAG_DATA = "guoku.data";
	public static final String TAG_UI = "guoku.ui";
	public static final String TAG_TESTCASE = "guoku.testcase";

	public static void level(int level) {
		mLogLevel = level;
	}
	
	public static void v(String tag, String msg) {
		if (mLogLevel > Log.VERBOSE) return;
		Log.v(tag, String.format(PREFIX_FORMAT, tag, msg));
	}
	
	public static void v(Object o, String msg) {
		if (mLogLevel > Log.VERBOSE) return;
		String name = o.getClass().getSimpleName();
		Log.v(name, String.format(PREFIX_FORMAT, name, msg));
	}
	
	public static void d(Object o, String msg) {
		if (mLogLevel > Log.DEBUG) return;
		String name = o.getClass().getSimpleName();
		Log.d(name, String.format(PREFIX_FORMAT, name, msg));
	}

	public static void d(String tag, String msg) {
		if (mLogLevel > Log.DEBUG) return;
		Log.d(tag, String.format(PREFIX_FORMAT, tag, msg));
	}

	public static void i(Object o, String msg) {
		if (mLogLevel > Log.INFO) return;
		String name = o.getClass().getSimpleName();
		Log.i(name, String.format(PREFIX_FORMAT, name, msg));
	}

	public static void i(String tag, String msg) {
		if (mLogLevel > Log.INFO) return;
		Log.i(tag, String.format(PREFIX_FORMAT, tag, msg));
	}

	public static void w(Object o, String msg) {
		if (mLogLevel > Log.WARN) return;
		String name = o.getClass().getSimpleName();
		Log.w(name, String.format(PREFIX_FORMAT, name, msg));
	}

	public static void w(String tag, String msg) {
		if (mLogLevel > Log.WARN) return;
		Log.w(tag, String.format(PREFIX_FORMAT, tag, msg));
	}

	public static void e(Object o, String msg, Throwable ex) {
		if (mLogLevel > Log.ERROR) return;
		String name = o.getClass().getSimpleName();
		if(msg != null) Log.e(name, String.format(PREFIX_FORMAT, name, msg));
		
		if(ex != null){
			String message = convertThrowable(ex);
			ex.printStackTrace();
		}
	}

	public static void e(String tag, String msg, Throwable ex) {
		if (mLogLevel > Log.ERROR) return;
		if(msg != null) Log.e(tag, String.format(PREFIX_FORMAT, tag, msg));
		
		if(ex != null){
			String message = convertThrowable(ex);
			ex.printStackTrace();
		}
	}
	
	private static String convertThrowable(Throwable ex){
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	public static void e(Object o, String msg) {
		e(o, msg, null);
	}

	public static void e(String tag, String msg) {
		e(tag, msg, null);
	}
	
	public static void e(Object o, Throwable ex) {
		e(o, null, ex);
	}

	public static void e(String tag, Throwable ex) {
		e(tag, null, ex);
	}
	
	/**
	 * 修改日志的打印级别，低于改日志级别的日志将不会打印出来
	 * @param level 日志级别 例如：Log.DEBUG
	 */
	public static void setLogLevel(int level){
		mLogLevel = level;
	}
}
