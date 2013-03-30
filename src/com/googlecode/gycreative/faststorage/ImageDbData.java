package com.googlecode.gycreative.faststorage;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;
import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public class ImageDbData extends DbData {
	
	public static String TABLE_NAME = "imgdbdata";
	public String CREATE_DB_SQL = "CREATE TABLE " + TABLE_NAME + " ( " + 
			BaseColumns._ID + " INT PRIMARY KEY, " + 
			COLUMN_KEY + " TEXT NOT NULL, " + 
			COLUMN_DATA + " BLOB NOT NULL " + ");";
	public static final String TAG = "ImageDbData";

	public ImageDbData(Context context) {
		super(context, TABLE_NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, ProtoProcessor<?>> exportData() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[] {COLUMN_KEY, COLUMN_DATA}, 
				null, null, null, null, null);
		HashMap<String, ProtoProcessor<?>> data = new HashMap<String, ProtoProcessor<?>>();
		cursor.moveToFirst();
		int count = cursor.getCount();
		for (int i = 0; i < count; i++) {
			String key = cursor.getString(cursor.getColumnIndex(COLUMN_KEY));
			byte[] byteData = cursor.getBlob(cursor.getColumnIndex(COLUMN_DATA));
			ImageProtoProcessor image = new ImageProtoProcessor();
			image.fromByteArray(byteData);
			data.put(key, image);
		}
		cursor.close();
		db.close();
		return data;
	}

	@Override
	public ProtoProcessor<?> getPersistentData(String key) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[] {COLUMN_KEY, COLUMN_DATA}, 
				COLUMN_KEY + " = '" + key + "'", null, null, null, null);
		byte[] data = cursor.getBlob(cursor.getColumnIndex(COLUMN_DATA));
		ImageProtoProcessor image = new ImageProtoProcessor();
		image.fromByteArray(data);
		cursor.close();
		db.close();
		return image;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_DB_SQL);
	}

	@Override
	public void writePersistentData(ProtoProcessor<?> o, String key) {
		// TODO Auto-generated method stub
		byte[] data = o.serializeDataToByteArray();
		ContentValues values = new ContentValues();
		values.put(key, data);
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

}
