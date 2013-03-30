package com.googlecode.gycreative.faststorage.benchmark;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.googlecode.gycreative.faststorage.FastImageStorage;
import com.googlecode.gycreative.faststorage.FastStorage;
import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;

public class ImageStorageBenchmark extends StorageBenchmark<ImageProtoProcessor> {

	public static final String TAG = "ImageStorageBenchmark";
	private Set<String> keySet = null;
	private long asyncTimeCount = 0;
	
	public ImageStorageBenchmark(Context context, int cachePolicy) {
		try {
			this.storage = new FastImageStorage(context, cachePolicy);
		} catch (ErrorCachePolicy e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.getLocalizedMessage());
		}
	}
	
	@Override
	public long testPutData(HashMap<String, ImageProtoProcessor> data, boolean ifSimulateSlow) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		keySet = data.keySet();
		for (String key : keySet) {
			ImageProtoProcessor image = data.get(key);
			storage.put(key, image);
			if (ifSimulateSlow) {
				try {
					Thread.sleep(SIMULATE_SLOW_SLEEP_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		long endTime = System.currentTimeMillis();
		return endTime - startTime;
	}

	@Override
	public long testGetAllData(boolean ifSimulateSlow) throws Exception {
		// TODO Auto-generated method stub
		if (keySet == null) {
			throw new Exception("you haven't put any data to storage yet");
		}
		else {
			long startTime = System.currentTimeMillis();
			for (String key : this.keySet) {
				storage.get(key);
				if (ifSimulateSlow) {
					try {
						Thread.sleep(SIMULATE_SLOW_SLEEP_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			long endTime = System.currentTimeMillis();
			return endTime - startTime;
		}
	}


	@Override
	public void testAsynGetAllData(final boolean ifSimulateSlow, final AsyncLoadFinishCallback callback) throws Exception {
		// TODO Auto-generated method stub
		if (keySet == null) {
			throw new Exception("you haven't put any data to storage yet");
		}
		else {
			if (callback != null) {
				final long startTime = System.currentTimeMillis();
				storage.setStorageCallback(new FastStorage.StorageCallback<ImageProtoProcessor>() {
	
					@Override
					public void afterLoad(String key, ImageProtoProcessor data) {
						// TODO Auto-generated method stub
						if (ifSimulateSlow) {
							try {
								Thread.sleep(SIMULATE_SLOW_SLEEP_TIME);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (key.equals(String.valueOf(keySet.size() - 1))) {
							long currentTime = System.currentTimeMillis();
							callback.onFinish(currentTime - startTime);
						}
					}
	
					@Override
					public void loadError(String key, Exception e) {
						// TODO Auto-generated method stub
						if (ifSimulateSlow) {
							try {
								Thread.sleep(SIMULATE_SLOW_SLEEP_TIME);
							} catch (InterruptedException e2) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (key.equals(String.valueOf(keySet.size() - 1))) {
							long currentTime = System.currentTimeMillis();
							callback.onFinish(currentTime - startTime);
						}
					}
				});
				for (String key : this.keySet) {
					storage.loadData(key);
				}
			}
		}
	}

}
