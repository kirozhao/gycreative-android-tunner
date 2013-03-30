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
public class MemoryCache implements Cache{
	
	private int limit; // number of byte
	private int size;
	private Map<String, ProtoProcessor<?>> cache= null; // a LRU cache
	private static final String TAG = "MemoryCache";
	
	public MemoryCache(int limit) {
		this.limit = limit;
		this.cache = Collections.synchronizedMap(new LinkedHashMap<String, ProtoProcessor<?>>(10,1.5f,true));
		this.size = 0;
	}
	
	public MemoryCache() {
		this.limit = 1000000;
		this.cache = Collections.synchronizedMap(new LinkedHashMap<String, ProtoProcessor<?>>(10,1.5f,true));
		this.size = 0;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public int getLimit() {
		return this.limit;
	}
	
	private void checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			Iterator<Entry<String, ProtoProcessor<?>>> iter = cache.entrySet().iterator();
			// least recently accessed item will be the first one iterated
			while (iter.hasNext()) {
				Entry<String, ProtoProcessor<?>> entry = iter.next();
				size -= getSize(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}
	
	private int getSize(ProtoProcessor<?> o) {
		int size = o.serializeDataToByteArray().length;
		return size;
	}

	@Override
	public ProtoProcessor<?> getObject(String key) {
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
	public void writeObject(String key, ProtoProcessor<?> o) {
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

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.cache.clear();
		this.size = 0;
	}

}
