package com.googlecode.gycreative.faststorage;

import java.util.HashMap;

import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public interface PersistentData {
	public abstract HashMap<String, ProtoProcessor<?>> exportData(); // key object
	public abstract void writePersistentData(ProtoProcessor<?> o, String key);
	public abstract ProtoProcessor<?> getPersistentData(String key);
}
