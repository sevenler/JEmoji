package com.jemoji.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "jemoji.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_EMOJI = "emoji_table";
	public static final String TABLE_MESSAGE = "message_table";
	
	public static final String COL_IMAGE = "image";
	public static final String COL_IMAGE_URL = "image_url";
	public static final String COL_VOICE = "voice";
	public static final String COL_VOICE_URL = "voice_url";
	public static final String COL_BACKGROUND = "background";
	public static final String COL_TYPE = "type";
	public static final String COLUMN_ID = "_id";
	
	public static final String COL_FROM_USER = "from_user";
	public static final String COL_TO_USER = "to_user";
	public static final String COL_EMOJI_ID = "emoji_id";
	
	private static final String CREATE_EMOJI_TABLE = "create table "
			+ TABLE_EMOJI + "( " + COLUMN_ID + " integer primary key autoincrement, " 
			+ COL_IMAGE + " text , "
			+ COL_IMAGE_URL + " text, " 
			+ COL_VOICE + " text , "
			+ COL_VOICE_URL + " text , "
			+ COL_TYPE +  " real,"
			+ COL_BACKGROUND +  " real);";
	
	private static final String CREATE_MESSAGE_TABLE = "create table "
			+ TABLE_MESSAGE + "( " + COLUMN_ID + " integer primary key autoincrement, " 
			+ COL_FROM_USER + " text , "
			+ COL_TO_USER + " text, " 
			+ COL_EMOJI_ID +  " real);";


	public SQLiteDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMOJI);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
		
		db.execSQL(CREATE_EMOJI_TABLE);
		db.execSQL(CREATE_MESSAGE_TABLE);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_EMOJI_TABLE);
		database.execSQL(CREATE_MESSAGE_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO 保存旧数据
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMOJI);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
		
		onCreate(db);
	}
	
	
}
