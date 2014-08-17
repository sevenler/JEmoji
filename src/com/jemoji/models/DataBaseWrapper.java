package com.jemoji.models;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseWrapper {
	private SQLiteDataBaseHelper sql_database_helper;
	private SQLiteDatabase sql_database;
	private String[] all_columns = { SQLiteDataBaseHelper.COLUMN_ID,
			SQLiteDataBaseHelper.COL_IMAGE,
			SQLiteDataBaseHelper.COL_IMAGE_URL, 
			SQLiteDataBaseHelper.COL_VOICE, 
			SQLiteDataBaseHelper.COL_VOICE_URL,
			SQLiteDataBaseHelper.COL_BACKGROUND, 
			SQLiteDataBaseHelper.COL_TYPE};
	
	public DataBaseWrapper(Context context) {
		sql_database_helper = new SQLiteDataBaseHelper(context);
		sql_database = sql_database_helper.getWritableDatabase();

	}
	
	public void open() throws SQLException {
		sql_database = sql_database_helper.getWritableDatabase();
	}
	
	public void close() {
		sql_database_helper.close();
	}
	
	public Emoji insertEmoji(Emoji emoji) {
		ContentValues values = new ContentValues();
		
		values.put(SQLiteDataBaseHelper.COL_IMAGE, emoji.getImage());
		values.put(SQLiteDataBaseHelper.COL_IMAGE_URL, emoji.getImageUrl());
		values.put(SQLiteDataBaseHelper.COL_VOICE, emoji.getVoice());
		values.put(SQLiteDataBaseHelper.COL_VOICE_URL, emoji.getVoiceUrl());
		values.put(SQLiteDataBaseHelper.COL_BACKGROUND, emoji.getBackground());
		values.put(SQLiteDataBaseHelper.COL_TYPE, emoji.getType());
		
		long insert_id = sql_database.insert(SQLiteDataBaseHelper.TABLE_TRANS, null,
				values);
		
		emoji.setId(insert_id);
		return emoji;
	}
	
	public long deleteEmoji(long row_id) {
		long row = sql_database.delete(SQLiteDataBaseHelper.TABLE_TRANS, SQLiteDataBaseHelper.COLUMN_ID
				+ " = " + row_id, null);
		return row;
	}
	
	public long deleteEmoji(Emoji emoji) {
		long row_id = emoji.getId();
		
		return deleteEmoji(row_id); 
	}
	
	public List<Emoji> getAllEmoji(int type) {
		List<Emoji> emoji_list = new ArrayList<Emoji>();

		String selection = String.format("%s = %s", SQLiteDataBaseHelper.COL_TYPE, type);
		Cursor cursor = sql_database.query(sql_database_helper.TABLE_TRANS,
				all_columns, selection, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Emoji emoji = cursorToComment(cursor);
			emoji_list.add(emoji);
			cursor.moveToNext();
		}
		
		cursor.close();
		return emoji_list;
	}
	
	public Emoji getEmoji(long id) {
		List<Emoji> emoji_list = new ArrayList<Emoji>();

		String selection = String.format("%s = %s", SQLiteDataBaseHelper.COLUMN_ID, id);
		Cursor cursor = sql_database.query(sql_database_helper.TABLE_TRANS, all_columns, selection, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Emoji emoji = cursorToComment(cursor);
			emoji_list.add(emoji);
			cursor.moveToNext();
		}
		
		cursor.close();
		return emoji_list.get(0);
	}
		
	private Emoji cursorToComment(Cursor cursor) {
		Emoji emoji = new Emoji(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
		return emoji;
	}
	
	public void dropTable() {
		sql_database_helper.dropTable(sql_database);
		sql_database = sql_database_helper.getWritableDatabase();
	}
	
}
