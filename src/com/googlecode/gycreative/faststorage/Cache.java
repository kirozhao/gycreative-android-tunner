package com.googlecode.gycreative.faststorage;

import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public interface Cache<DATA_TYPE extends ProtoProcessor> {
	public abstract DATA_TYPE getObject(String key);
	public abstract void writeObject(String key, DATA_TYPE o);
	public abstract void clear();
}
