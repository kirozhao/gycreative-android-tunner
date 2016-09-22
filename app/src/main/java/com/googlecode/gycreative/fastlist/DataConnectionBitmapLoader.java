package com.googlecode.gycreative.fastlist;

import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.googlecode.gycreative.dataconn.DataConnection;
import com.googlecode.gycreative.dataconn.DataConnectionFactory;
import com.googlecode.gycreative.dataconn.DataConnection.LoadImageCallBack;
import com.googlecode.gycreative.faststorage.FastImageStorage;
import com.googlecode.gycreative.faststorage.FastStorage;
import com.googlecode.gycreative.faststorage.FastStorage.StorageCallback;
import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;

public class DataConnectionBitmapLoader {
	ImageView imageView;
	Context context;
	String imageString;
	
	public void loadBitmap(String imageString, ImageView imageView, Context context){
		FastImageStorage storage;
		this.imageView = imageView;
		this.imageString = imageString;
		this.context = context;
		try {
			storage = new FastImageStorage(context, FastStorage.CachePolicy.MEM_FILE_CACHE);
			storage.loadData(imageString, callback);
		} catch (ErrorCachePolicy e) {
			e.printStackTrace();
		}
	}
	
	StorageCallback<ImageProtoProcessor> callback = new StorageCallback<ImageProtoProcessor>(){
		@Override
		//从本地获得数据
		public void afterLoad(String key, ImageProtoProcessor data) {
			imageView.setImageBitmap(data.toBitmap());
		}
		@Override
		//本地没有，从网络获取
		public void loadError(String key, Exception e) {
			DataConnection dataConnection = DataConnectionFactory
										.openConnection(context, imageString);
			dataConnection.setLoadImageCallBack(new LoadImageCallBack() {
				@Override
				public void imageLoaded(Bitmap bitmap) {
					imageView.setImageBitmap(bitmap);
					//将数据缓存起来
					FastImageStorage storage=null;
					try {
						storage = new FastImageStorage(context,FastStorage.CachePolicy.MEM_FILE_CACHE);
						ImageProtoProcessor image = new ImageProtoProcessor(imageString,bitmap);
						storage.put(imageString,image);
					} catch (ErrorCachePolicy e) {
						e.printStackTrace();
					}
				}
			});
			dataConnection.send(null);//传递协议的自己数组
		}
	};
}
