package com.googlecode.gycreative.faststorage;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

/**
 * something reference to LazyList
 * @author ragnarok
 *
 */
public class MemoryCache<T extends ProtoProcessor> implements Cache<T> {
	
	private int limit; // number of byte
	private int size;
	private Map<String, T> cache= null; // a LRU cache
	private static final String TAG = "MemoryCache";
	
	public MemoryCache(int limit) {
		this.limit = limit;
		this.cache = Collections.synchronizedMap(new LinkedHashMap<String, T>(10, 1.5f, true));
		this.size = 0;
	}
	
	public MemoryCache() {
		this.limit = 1000000;
		this.cache = Collections.synchronizedMap(new LinkedHashMap<String, T>(10, 1.5f, true));
		this.size = 0;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLimit() {
		return this.limit;
	}
	
	private void checkSize() {
		if (Util.DEBUG)
			Log.d(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			Iterator<Entry<String, T>> iter = cache.entrySet().iterator();
			// least recently accessed item will be the first one iterated
			while (iter.hasNext()) {
				Entry<String, T> entry = iter.next();
				size -= getSize(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			if (Util.DEBUG)
				Log.d(TAG, "Clean cache. New size " + cache.size());
		}
	}
	
	private int getSize(T o) {
		int size = 0;
		/*
		 * in multi threads environment, some object may be deleted by other threads, so at this
		 * time, the Object 'o' may be null, at the result, the system will waste lots of time to
		 * handle this shit NULLPOINTEREXCEPTION!!!!!!!! >_<
		 */
		if (o != null)
			size = o.serializeDataToByteArray().length;
		return size;
	}

	@Override
	public T getObject(String key) {
		// TODO Auto-generated method stub
		try {
			if (!cache.containsKey(key))
				return null;
			// NullPointerException sometimes happen here
			// http://code.google.com/p/osmdroid/issues/detail?id=78
			return cache.get(key);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.cache.clear();
		this.size = 0;
	}

	@Override
	public void writeObject(String key, T o) {
		// TODO Auto-generated method stub
		try {
			if (cache.containsKey(key))
				size -= getSize(cache.get(key));
			cache.put(key, o);
			size += getSize(o);
			checkSize(); // check the size when put image into memoryCache
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
	
	public boolean checkIfExist(String key) {
		if (cache.containsKey(key))
			return true;
		return false;
	}

}
