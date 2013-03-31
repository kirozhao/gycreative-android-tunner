package com.googlecode.gycreative.faststorage.benchmark;

import java.util.HashMap;

import com.googlecode.gycreative.faststorage.FastImageStorage;
import com.googlecode.gycreative.faststorage.FastStorage;
import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public abstract class StorageBenchmark<DATA_TYPE extends ProtoProcessor> {
	
	protected int SIMULATE_SLOW_SLEEP_TIME = 100;
	protected FastStorage<DATA_TYPE> storage = null;
	
	/**
	 * test put a large number of data into storage
	 * @param data
	 * @param ifSimulateSlow if true, the main thread will sleep after each put() call
	 * @return elapsed time, in milliseconds
	 */
	public abstract long testPutData(HashMap<String, DATA_TYPE> data, boolean ifSimulateSlow);
	
	/**
	 * test get all data from the storage
	 * this method doesn't utilize the storage's thread pool, just call get()
	 * method for each key
	 * @param ifSimulateSlow if true, the main thread will sleep after each get() call
	 * @return elapsed time, in milliseconds
	 * @throws Exception if the storage is empty
	 */
	public abstract long testGetAllData(boolean ifSimulateSlow) throws Exception;
	
	public interface AsyncLoadFinishCallback {
		public abstract void onFinish(long time);
	}
	
	public abstract void testAsynGetAllData(boolean ifSimulateSlow, AsyncLoadFinishCallback callback) throws Exception;
}
