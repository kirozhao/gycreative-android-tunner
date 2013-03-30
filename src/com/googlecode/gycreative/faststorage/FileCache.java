package com.googlecode.gycreative.faststorage;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.os.Environment;

import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public abstract class FileCache<T extends ProtoProcessor> implements Cache<T>, PersistentData<T> {
	
	protected String filePath = null;
	protected static final String TAG = "FileCache";
	protected File fileCacheDir = null;
	protected Context context = null;
	
	protected void makeFileCacheDir() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			this.fileCacheDir = new File(Environment.getExternalStorageDirectory(), this.filePath);
		else {
			this.fileCacheDir = context.getCacheDir();
		}
		if (!fileCacheDir.exists()) {
			fileCacheDir.mkdirs();
		}
	}

	@Override
	public abstract HashMap<String, T> exportData();

	@Override
	public void writePersistentData(T o, String key) {
		// TODO Auto-generated method stub
		this.writeObject(key, o);
	}

	@Override
	public abstract T getPersistentData(String key);

	@Override
	public abstract T getObject(String key);

	@Override
	public void writeObject(String key, T o) {
		// TODO Auto-generated method stub
		File f = new File(fileCacheDir, key);
		if (f.exists()) {
			f.delete();
		}
		byte[] data = o.serializeDataToByteArray();
		Util.writeFile(f, data);
	}

	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		File[] files = this.fileCacheDir.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			f.delete();
		}
	}



}
