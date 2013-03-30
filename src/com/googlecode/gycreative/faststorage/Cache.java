package com.googlecode.gycreative.faststorage;

import com.googlecode.gycreative.faststorage.protoprocessor.ProtoProcessor;

public interface Cache {
	public abstract ProtoProcessor<?> getObject(String key);
	public abstract void writeObject(String key, ProtoProcessor<?> o);
	public abstract void clear();
}
