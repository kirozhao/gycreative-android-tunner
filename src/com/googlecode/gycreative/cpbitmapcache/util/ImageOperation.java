package com.googlecode.gycreative.cpbitmapcache.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;

/**
 * @author zhouml
 * @version  createTime:20132013-3-30上午7:59:33
 * */
public class ImageOperation {
	
	public static boolean loadImageFromUrl(Context context, String path,
			String saveUrl) {
		InputStream is = null;
		
		File cacheDir;
		 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
	        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),saveUrl);
	    }else{
	        cacheDir=context.getCacheDir();
	    }
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		URL url = null;
		try {
			url = new URL(path);
			is = (InputStream) url.getContent();
			FileOutputStream fos = new FileOutputStream(cacheDir + "/"+path.hashCode());
			int data = is.read();
			while (data != -1) {
				fos.write(data);
				data = is.read();
			}
			fos.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("异常--》下载图片异常");
			return true;
		}
		return false;
	}

}
