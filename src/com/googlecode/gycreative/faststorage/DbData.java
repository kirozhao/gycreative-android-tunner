package com.googlecode.gycreative.faststorage;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public abstract class DbData extends SQLiteOpenHelper implements PersistentData{
	

	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_DATA = "data";
	public static String TABLE_NAME = "dbdata"; // default table name
	public static final int VERSION = 1;
	
	public String CREATE_DB_SQL = "CREATE TABLE " + TABLE_NAME + " ( " + 
					BaseColumns._ID + " INT PRIMARY KEY, " + 
					COLUMN_KEY + " TEXT NOT NULL, " + 
					COLUMN_DATA + " BLOB NOT NULL " + ");";
	
	public DbData(Context context, String tablename) {
		super(context, tablename, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract HashMap<String, ProtoProcessor<?>> exportData();

	@Override
	public abstract void writePersistentData(ProtoProcessor<?> o, String key);
	

	@Override
	public abstract ProtoProcessor<?> getPersistentData(String key);

	@Override
	public abstract void onCreate(SQLiteDatabase db);

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	

}
