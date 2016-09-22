package com.googlecode.gycreative.fastlist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.googlecode.gycreative.R;
import com.googlecode.gycreative.faststorage.FastImageStorage;
import com.googlecode.gycreative.faststorage.FastStorage;
import com.googlecode.gycreative.faststorage.FastStorage.StorageCallback;
import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

public class FastStorageBitmapLoader {
	private ImageView imageView;
	private String imageString;
	private Context context;
	
	public void showImage(ImageView imageView,String imageString, Context context) {
		this.imageString = imageString;
		this.context = context;
		this.imageView = imageView;
		FastImageStorage storage;
		try {
			storage = new FastImageStorage(context,
					FastStorage.CachePolicy.MEM_FILE_CACHE);
			storage.loadData(imageString, callback);
		} catch (ErrorCachePolicy e) {
			e.printStackTrace();
		}
	}
	
	StorageCallback<ImageProtoProcessor> callback = new StorageCallback<ImageProtoProcessor>(){
		@Override
		//从本地获得数据
		public void afterLoad(String key, ImageProtoProcessor data) {
			if(NetworkImageItem2.imageViewReused(imageString,imageView))
		                    return;
			imageView.setImageBitmap(data.toBitmap());
		}
		@Override
		//本地没有，从网络获取
		public void loadError(String key, Exception e) {
			if(NetworkImageItem2.imageViewReused(imageString,imageView))
		                    return;
			imageView.setImageResource(R.drawable.default_item);
			File f = new File(Environment
					.getExternalStorageDirectory()
					+ "/listCache/"+System.currentTimeMillis());
			try {
				URL imageUrl = new URL(key);
				HttpURLConnection conn = (HttpURLConnection) imageUrl
						.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				Utils.CopyStream(is, os);
				os.close();
				conn.disconnect();
				Bitmap bitmap = decodeFile(f);
				if(NetworkImageItem2.imageViewReused(imageString,imageView))
			                    return;
				imageView.setImageBitmap(bitmap);
				// 将数据缓存起来
				FastImageStorage storage = null;
				storage = new FastImageStorage(
						context,
						FastStorage.CachePolicy.MEM_FILE_CACHE);
				ImageProtoProcessor image = new ImageProtoProcessor(
						imageString, bitmap);
				storage.put(imageString, image);
			} catch (ErrorCachePolicy ex) {
				ex.printStackTrace();
			} catch (MalformedURLException me) {
				// TODO Auto-generated catch block
				me.printStackTrace();
			} catch (IOException ioe) {
				// TODO Auto-generated catch block
				ioe.printStackTrace();
			}
		}
	};
	
	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power
			// of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2,
					null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
