package com.jemoji.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "emoji.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_TRANS = "emoji_table";
	
	public static final String COL_IMAGE = "image";
	public static final String COL_IMAGE_URL = "image_url";
	public static final String COL_VOICE = "voice";
	public static final String COL_VOICE_URL = "voice_url";
	public static final String COL_BACKGROUND = "background";
	public static final String COL_TYPE = "type";
	public static final String COLUMN_ID = "_id";
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_TRANS + "( " + COLUMN_ID + " integer primary key autoincrement, " 
			+ COL_IMAGE + " text , "
			+ COL_IMAGE_URL + " text, " 
			+ COL_VOICE + " text , "
			+ COL_VOICE_URL + " text , "
			+ COL_TYPE +  " real,"
			+ COL_BACKGROUND +  " real);";


	public SQLiteDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANS);
		db.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO 保存旧数据
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANS);
		onCreate(db);
	}
	
	
}
