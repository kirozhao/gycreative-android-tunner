package com.googlecode.gycreative.faststorage;

import java.util.HashMap;

import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public interface PersistentData<DATA_TYPE extends ProtoProcessor> {
	public abstract HashMap<String, DATA_TYPE> exportData(); // key object
	public abstract void importData(HashMap<String, DATA_TYPE> data);
	public abstract void writePersistentData(DATA_TYPE o, String key);
	public abstract DATA_TYPE getPersistentData(String key);
}
