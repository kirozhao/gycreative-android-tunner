package com.googlecode.gycreative.faststorage;

import java.io.File;
import java.util.HashMap;

import android.content.Context;

import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;
import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public class ImageFileCache extends FileCache {


	public ImageFileCache(Context context, String filePath) {
		this.filePath = filePath;
		this.context = context;
		makeFileCacheDir();
	}
	
	public ImageFileCache(Context context) {
		this.filePath = "GyImageFileCache";
		this.context = context;
		makeFileCacheDir();
	}

	@Override
	public HashMap<String, ProtoProcessor<?>> exportData() {
		// TODO Auto-generated method stub
		HashMap<String, ProtoProcessor<?>> data = new HashMap<String, ProtoProcessor<?>>();
		File[] files = this.fileCacheDir.listFiles();
		for (File f : files) {
			if (f.exists()) {
				byte[] d = Util.readFile(f);
				ImageProtoProcessor image = new ImageProtoProcessor();
				image.fromByteArray(d);
				data.put(f.getName(), image);
			}
		}
		return data;
	}

	@Override
	public ImageProtoProcessor getObject(String key) {
		// TODO Auto-generated method stub
		File f = new File(this.fileCacheDir, key);
		if (!f.exists()) {
			return null;
		}
		byte[] data = Util.readFile(f);
		ImageProtoProcessor image = new ImageProtoProcessor();
		image.fromByteArray(data);
		return image;
	}

}
