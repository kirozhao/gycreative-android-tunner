package com.googlecode.gycreative.faststorage;

import java.util.concurrent.Executors;

import android.content.Context;

import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;

public class FastImageStorage extends FastStorage<ImageProtoProcessor> {
	
	private Context context = null;
	public static final String TAG = "FastImageStorage";

	public FastImageStorage(Context context, int threadNum, int memoryLimit, int cachePolicy) throws ErrorCachePolicy {
		// TODO Auto-generated constructor stub
		super(context, cachePolicy);
		this.memoryLimit = memoryLimit;
		this.threadNum = threadNum;
		setUpCache();
	}
	
	public FastImageStorage(Context context, int cachePolicy) throws ErrorCachePolicy {
		super(context, cachePolicy);
		this.memoryLimit = -1; // use the default value;
		this.threadNum = 5;
		setUpCache();
	}
	
	/**
	 * set up the cache according to cache policy
	 * @throws ErrorCachePolicy 
	 */
	private void setUpCache() {
		this.executorService = Executors.newFixedThreadPool(threadNum);
		if (this.memoryLimit == -1) { // use default value
			this.memoryCache = new MemoryCache();
		}
		else {
			this.memoryCache = new MemoryCache(this.memoryLimit);
		}
		if (this.cachePolicy == CachePolicy.MEM_CACHE) {
			return;
		}
		else if (this.cachePolicy == CachePolicy.MEM_DB_CACHE) {
			this.dbData = new ImageDbData(context);
		}
		else if (this.cachePolicy == CachePolicy.MEM_FILE_CACHE) {
			this.fileCache = new ImageFileCache(context);
		}
	}


	@Override
	public ImageProtoProcessor get(String key) {
		// TODO Auto-generated method stub
		ImageProtoProcessor memData = (ImageProtoProcessor) this.memoryCache.getObject(key);
		if (cachePolicy == CachePolicy.MEM_CACHE) {
			if (memData != null) {
				return memData;
			}
			else {
				return null;
			}
		}
		else if (cachePolicy == CachePolicy.MEM_FILE_CACHE) {
			if (memData != null) {
				return memData;
			}
			else {
				// get data from file
				ImageProtoProcessor fileData = (ImageProtoProcessor) this.fileCache.getObject(key);
				if (fileData != null) {
					// put fileData into memCache
					this.memoryCache.writeObject(key, fileData);
					return fileData;
				}
				else {
					return null;
				}
			}
		}
		else if (cachePolicy == CachePolicy.MEM_DB_CACHE) {
			if (memData != null) {
				return memData;
			}
			else {
				// get data from db
				ImageProtoProcessor dbData = (ImageProtoProcessor) this.dbData.getPersistentData(key);
				if (dbData != null) {
					// put dbData into memCache
					this.memoryCache.writeObject(key, dbData);
					return dbData;
				}
				else {
					return null;
				}
			}
		}
		return null;
	}

	public void put(String key, ImageProtoProcessor data) {
		// TODO Auto-generated method stub
		if (cachePolicy == CachePolicy.MEM_CACHE) {
			this.memoryCache.writeObject(key, data);
		}
		else if (cachePolicy == CachePolicy.MEM_DB_CACHE) {
			this.dbData.writePersistentData(data, key);
		}
		else if (cachePolicy == CachePolicy.MEM_FILE_CACHE) {
			this.fileCache.writeObject(key, data);
		}
	}


}
