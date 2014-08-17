package com.jemoji.models;

import java.util.ArrayList;
import java.util.List;

import com.jemoji.utils.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseWrapper {
	private SQLiteDataBaseHelper sql_database_helper;
	private SQLiteDatabase sql_database;
	
	public DataBaseWrapper(Context context) {
		sql_database_helper = new SQLiteDataBaseHelper(context);
		sql_database = sql_database_helper.getWritableDatabase();

	}
	
	public void open() throws SQLException {
		sql_database = sql_database_helper.getWritableDatabase();
	}
	
	public void dropTable() {
		sql_database_helper.dropTable(sql_database);
		sql_database = sql_database_helper.getWritableDatabase();
	}
	
	public void close() {
		sql_database_helper.close();
	}
	
	////////////////////////////////////////////////////// Emoji Begin//////////////////////////////////////////////////////////////////
	private String[] emoji_all_columns = { SQLiteDataBaseHelper.COLUMN_ID,
			SQLiteDataBaseHelper.COL_IMAGE,
			SQLiteDataBaseHelper.COL_IMAGE_URL, 
			SQLiteDataBaseHelper.COL_VOICE, 
			SQLiteDataBaseHelper.COL_VOICE_URL,
			SQLiteDataBaseHelper.COL_BACKGROUND, 
			SQLiteDataBaseHelper.COL_TYPE};
	
	public Emoji insertEmoji(Emoji emoji) {
		ContentValues values = new ContentValues();
		
		values.put(SQLiteDataBaseHelper.COL_IMAGE, emoji.getImage());
		values.put(SQLiteDataBaseHelper.COL_IMAGE_URL, emoji.getImageUrl());
		values.put(SQLiteDataBaseHelper.COL_VOICE, emoji.getVoice());
		values.put(SQLiteDataBaseHelper.COL_VOICE_URL, emoji.getVoiceUrl());
		values.put(SQLiteDataBaseHelper.COL_BACKGROUND, emoji.getBackground());
		values.put(SQLiteDataBaseHelper.COL_TYPE, emoji.getType());
		
		long insert_id = sql_database.insert(SQLiteDataBaseHelper.TABLE_EMOJI, null, values);

		emoji.setId(insert_id);
		return emoji;
	}
	
	public long deleteEmoji(long row_id) {
		long row = sql_database.delete(SQLiteDataBaseHelper.TABLE_EMOJI,
				SQLiteDataBaseHelper.COLUMN_ID + " = " + row_id, null);
		return row;
	}
	
	public long deleteEmoji(Emoji emoji) {
		long row_id = emoji.getId();
		return deleteEmoji(row_id); 
	}
	
	public List<Emoji> getAllEmoji(int type) {
		List<Emoji> emoji_list = new ArrayList<Emoji>();

		String selection = String.format("%s = %s", SQLiteDataBaseHelper.COL_TYPE, type);
		Cursor cursor = sql_database.query(sql_database_helper.TABLE_EMOJI,
				emoji_all_columns, selection, null, null, null, null);

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
		Cursor cursor = sql_database.query(sql_database_helper.TABLE_EMOJI, emoji_all_columns, selection, null, null, null, null);

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
	//////////////////////////////////////////////////////Emoji End//////////////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////Message Begin//////////////////////////////////////////////////////////////////
	private String[] message_all_columns = { SQLiteDataBaseHelper.COLUMN_ID,
			SQLiteDataBaseHelper.COL_FROM_USER,
			SQLiteDataBaseHelper.COL_TO_USER, 
			SQLiteDataBaseHelper.COL_EMOJI_ID};
	
	public Message insertMessage(Message message) {
		ContentValues values = new ContentValues();
		
		//先保存表情，注意表情的类型为消息表情
		long emoji_id = insertEmoji(message.getEmoji().setType(Emoji.EMOJI_TYPE_MESSAGE)).getId();
		
		values.put(SQLiteDataBaseHelper.COL_FROM_USER, message.getFromUser());
		values.put(SQLiteDataBaseHelper.COL_TO_USER, message.getToUser());
		values.put(SQLiteDataBaseHelper.COL_EMOJI_ID, emoji_id);
		
		long insert_id = sql_database.insert(SQLiteDataBaseHelper.TABLE_MESSAGE, null, values);

		message.setId(insert_id);
		return message;
	}
	
	public List<Message> getAllMessage(String from_user) {
		List<Message> message_list = new ArrayList<Message>();

		String selection = String.format("%s = %s", SQLiteDataBaseHelper.COL_FROM_USER, from_user);
		if(Utility.Strings.isEmptyString(from_user)) selection = null;
		Cursor cursor = sql_database.query(sql_database_helper.TABLE_MESSAGE, message_all_columns, selection,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			
			long emoji_id = cursor.getInt(3);
			Emoji emoji = getEmoji(emoji_id);
			
			Message messsage = new Message(cursor.getInt(0), cursor.getString(1), cursor.getString(2), emoji);
			message_list.add(messsage);
			cursor.moveToNext();
		}
		
		cursor.close();
		return message_list;
	}
	
	public List<Message> getAllMessage(long emoji_id) {
		List<Message> message_list = new ArrayList<Message>();

		String selection = String.format("%s = %s", SQLiteDataBaseHelper.COL_EMOJI_ID, emoji_id);
		Cursor cursor = sql_database.query(sql_database_helper.TABLE_MESSAGE, message_all_columns, selection,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			
			Emoji emoji = getEmoji(emoji_id);
			
			Message messsage = new Message(cursor.getInt(0), cursor.getString(1), cursor.getString(2), emoji);
			message_list.add(messsage);
			cursor.moveToNext();
		}
		
		cursor.close();
		return message_list;
	}
	
	public List<String> getAllFromUser(){
		List<String> user_list = new ArrayList<String>();
		
		String[] columns = { SQLiteDataBaseHelper.COL_FROM_USER };
		Cursor cursor = sql_database.query(sql_database_helper.TABLE_MESSAGE, columns, null, null,
				null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			user_list.add(cursor.getString(0));
			cursor.moveToNext();
		}
		
		return user_list;
	}
	
	public long deleteMessage(long row_id) {
		long row = sql_database.delete(SQLiteDataBaseHelper.TABLE_MESSAGE,
				SQLiteDataBaseHelper.COLUMN_ID + " = " + row_id, null);
		return row;
	}
	
	public long deleteMessage(Message message) {
		return deleteEmoji(message.getId());
	}
	
	public long deleteMessageWithEmoji(Emoji emoji) {
		Message message = getAllMessage(emoji.getId()).get(0);
		return deleteMessage(message.getId());
	}
	//////////////////////////////////////////////////////Message End//////////////////////////////////////////////////////////////////
}
