package com.googlecode.gycreative.faststorage;

import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public abstract class DbData<T extends ProtoProcessor> extends SQLiteOpenHelper implements PersistentData<T> {

	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_DATA = "data";
	public static final int VERSION = 1;
	
	public static final String TAG = "DbData";

	
	protected DbData(Context context, String name) {
		super(context, name, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract HashMap<String, T> exportData();

	@Override
	public abstract void writePersistentData(T o, String key);

	@Override
	public abstract T getPersistentData(String key);

	@Override
	public void importData(HashMap<String, T> data) {
		// TODO Auto-generated method stub
		for (String key : data.keySet()) {
			T t = data.get(key);
			this.writePersistentData(t, key);
		}
	}

	public abstract void clear();

}
