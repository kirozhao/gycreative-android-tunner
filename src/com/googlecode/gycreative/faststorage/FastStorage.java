package com.googlecode.gycreative.faststorage;

import java.util.concurrent.ExecutorService;

import android.content.Context;
import android.util.Log;

import com.googlecode.gycreative.faststorage.exception.DataNotExistException;
import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public abstract class FastStorage<DATA_TYPE extends ProtoProcessor> {
	/**
	 * get, write object, db, files(cache), memory cache 
	 */
	protected MemoryCache<DATA_TYPE> memoryCache = null;
	protected FileCache<DATA_TYPE> fileCache = null;
	protected DbData<DATA_TYPE> dbData = null;
	protected ExecutorService executorService = null; // thread pool
	protected int threadNum = 0; // the number of threads in thread pool
	protected int memoryLimit = 0;
	protected int cachePolicy = 0;
	protected Context context = null;
	protected StorageCallback<DATA_TYPE> loadCallback = null;
	
	public static final String TAG = "FastStorage";

	public class CachePolicy {
		public static final int MEM_CACHE = 1; // first find from mem cache
		public static final int MEM_FILE_CACHE = 2; // first find from mem cache, then from file cache
		public static final int MEM_DB_CACHE = 3; // first find from mem cache, then from db
	}
	
	public interface StorageCallback<T extends ProtoProcessor> {
		public abstract void afterLoad(String key, T data);
		public abstract void loadError(String key, Exception e);
	}
	
	public FastStorage(Context context, int cachePolicy) throws ErrorCachePolicy {
		this.context = context;
		if (cachePolicy > 3 || cachePolicy < 0) {
			throw new ErrorCachePolicy(cachePolicy);
		}
		this.cachePolicy = cachePolicy;
	}
	
	public int getCachePolicy() {
		return this.cachePolicy;
	}
	
	public void setStorageCallback(StorageCallback<DATA_TYPE> callback) {
		this.loadCallback = callback;
	}

	public void loadData(String key) {
		DATA_TYPE data = this.memoryCache.getObject(key);
		if (data != null) {
			if (Util.DEBUG)
				Log.d(TAG, "in loadData, got data from memoryCache");
			if (this.loadCallback != null) {
				loadCallback.afterLoad(key, data);
			}
		}
		else {
			executorService.submit(new LoaderTask(key));
		}
	}
	
	public void loadData(String key, StorageCallback<DATA_TYPE> callback) {
		if (callback != null) {
			this.loadCallback = callback;
			DATA_TYPE data = this.memoryCache.getObject(key);
			if (data != null) {
				if (Util.DEBUG)
					Log.d(TAG, "in loadData, got data from memoryCache");
				if (this.loadCallback != null) {
					loadCallback.afterLoad(key, data);
				}
			}
			else {
				executorService.submit(new LoaderTask(key));
			}
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
				DATA_TYPE data = get(key);
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
	
	public MemoryCache<DATA_TYPE> getMemoryCache() {
		return this.memoryCache;
	}
	
	public FileCache<DATA_TYPE> getFileData() {
		return this.fileCache;
	}
	
	public DbData<DATA_TYPE> getDbData() {
		return this.dbData;
	}
	
	public void clearData() {
		if (this.memoryCache != null) {
			this.memoryCache.clear();
		}
		if (this.dbData != null) {
			this.dbData.clear();
		}
		if (this.fileCache != null) {
			this.fileCache.clear();
		}
	}

}
