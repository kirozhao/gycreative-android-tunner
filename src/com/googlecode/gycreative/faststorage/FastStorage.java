package com.googlecode.gycreative.faststorage;

import java.util.concurrent.ExecutorService;

import android.content.Context;

import com.googlecode.gycreative.faststorage.exception.DataNotExistException;
import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protodata.Imageproto.Image;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;
import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public abstract class FastStorage<DATA_TYPE extends ProtoProcessor> {
	/**
	 * get, write object, db, files(cache), memory cache 
	 */
	protected MemoryCache memoryCache = null;
	protected FileCache fileCache = null;
	protected DbData dbData = null;
	protected ExecutorService executorService = null; // thread pool
	protected int threadNum = 0; // the number of threads in thread pool
	protected int memoryLimit = 0;
	protected int cachePolicy = 0;
	protected Context context = null;
	protected StorageCallback loadCallback = null;
	
	public static final String TAG = "FastStorage";

	class CachePolicy {
		public static final int MEM_CACHE = 1; // first find from mem cache
		public static final int MEM_FILE_CACHE = 2; // first find from mem cache, then from file cache
		public static final int MEM_DB_CACHE = 3; // first find from mem cache, then from db
	}
	
	interface StorageCallback {
		public abstract void afterLoad(String key, ProtoProcessor<?> data);
		public abstract void loadError(String key, Exception e);
	}
	
	public FastStorage(Context context, int cachePolicy) throws ErrorCachePolicy {
		this.context = context;
		if (cachePolicy != CachePolicy.MEM_CACHE || cachePolicy != CachePolicy.MEM_DB_CACHE || cachePolicy != CachePolicy.MEM_FILE_CACHE) {
			throw new ErrorCachePolicy(cachePolicy);
		}
		this.cachePolicy = cachePolicy;
	}
	
	public int getCachePolicy() {
		return this.cachePolicy;
	}
	
	public void setStorageCallback(StorageCallback callback) {
		this.loadCallback = callback;
	}

	public void loadData(String key) {
		ProtoProcessor<?> data = this.memoryCache.getObject(key);
		if (data != null) {
			if (this.loadCallback != null) {
				loadCallback.afterLoad(key, data);
			}
		}
		else {
			executorService.submit(new LoaderTask(key));
		}
	}

	
	class LoaderTask implements Runnable {
		
		public String key = null;
		
		public LoaderTask(String key) {
			this.key = key;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				ProtoProcessor<?> data = get(key);
				if (data == null) {
					loadCallback.loadError(key, new DataNotExistException(key)); // regard no data as an error
				}
				memoryCache.writeObject(key, data);
				if (loadCallback != null) {
					loadCallback.afterLoad(key, data);
				}
			} catch (Exception e) {
				loadCallback.loadError(key, e);
			}
		}
		
	}
	
	/**
	 * first get from cache, if not exist, then from file or db, according to the cache policy
	 * if the policy is MEM_CACHE, when the object is not exist in the mem, return null
	 * if mem cache not exist and exist in file or db, it will first insert in mem cache and then return the object
	 * @return null if the object is not exist from the mem cache(MEM_CACHE), or file(MEM_FILE_CACHE), db(MEM_DB_CACHE), 
	 * otherwise, return the object, is a ProtoProcessor
	 */
	public abstract DATA_TYPE get(String key);
	
	/**
	 * put data into storage, if cachePolicy is MEM_CACHE, put it into memCache, otherwise put into file or db
	 * @param key
	 * @param o
	 */
	public abstract void put(String key, DATA_TYPE o);
	
	

}
