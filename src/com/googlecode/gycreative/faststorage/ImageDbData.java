package com.googlecode.gycreative.faststorage;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.googlecode.gycreative.faststorage.protodata.Imageproto.Image;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;
import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public class ImageDbData extends DbData<ImageProtoProcessor> {
	
	public static final String TABLE_NAME = "imgdbdata";
	public static final String CREATE_DB_SQL = "CREATE TABLE " + TABLE_NAME + " ( " + 
			BaseColumns._ID + " INT PRIMARY KEY, " + 
			COLUMN_KEY + " TEXT NOT NULL, " + 
			COLUMN_DATA + " BLOB NOT NULL " + ");";
	public static final String TAG = "ImageDbData";
	
	private static ImageDbData instance = null;
	private static SQLiteDatabase db = null; // keep one connection
	
	public static ImageDbData getInstance(Context context) {
		if (instance == null) {
			instance = new ImageDbData(context);
			return instance;
		}
		else {
			return instance;
		}
	}
	
	public static void closeDbConnection() {
		db.close();
	}

	protected ImageDbData(Context context) {
		super(context, TABLE_NAME);
		// TODO Auto-generated constructor stub
		db = this.getWritableDatabase();
	}

	/**
	 * return is [(Key, ImageProtoProcessor), ....], so you can force cast to ImageProtoProcessor, then
	 * call toBitmap on ImageProtoProcessor object to get the bitmap
	 */
	@Override
	public HashMap<String, ImageProtoProcessor> exportData() {
		// TODO Auto-generated method stub
		//SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[] {COLUMN_KEY, COLUMN_DATA}, 
				null, null, null, null, null);
		HashMap<String, ImageProtoProcessor> data = new HashMap<String, ImageProtoProcessor>();
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
		//db.close();
		return data;
	}

	@Override
	public ImageProtoProcessor getPersistentData(String key) {
		// TODO Auto-generated method stub
		//SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[] {COLUMN_KEY, COLUMN_DATA}, 
				COLUMN_KEY + " = '" + key + "'", null, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			int dataIndex = cursor.getColumnIndex(COLUMN_DATA);
			//Log.d(TAG, "in getPersistentData, dataIndex is " + dataIndex);
			byte[] data = cursor.getBlob(dataIndex);
			ImageProtoProcessor image = new ImageProtoProcessor();
			image.fromByteArray(data);
			if (Util.DEBUG)
				Log.d(TAG, "in getPersistentData, successfully get image");
			cursor.close();
			//db.close();
			return image;
		}
		else {
			if (Util.DEBUG)
				Log.d(TAG, "in getPersistentData, return null");
			return null;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_DB_SQL);
	}

	@Override
	public void writePersistentData(ImageProtoProcessor o, String key) {
		// TODO Auto-generated method stub
		byte[] data = o.serializeDataToByteArray();
		ContentValues values = new ContentValues();
		values.put(COLUMN_KEY, key);
		values.put(COLUMN_DATA, data);
		//SQLiteDatabase db = this.getWritableDatabase();
		// first delete old values
		db.delete(TABLE_NAME, COLUMN_KEY + "= '" + key + "'", null);
		db.insert(TABLE_NAME, null, values);
		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE " + TABLE_NAME + ";");
		db.execSQL(CREATE_DB_SQL);
		//db.close();
	}

}
