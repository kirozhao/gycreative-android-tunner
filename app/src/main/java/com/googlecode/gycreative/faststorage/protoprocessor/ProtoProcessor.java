package com.googlecode.gycreative.faststorage.protoprocessor;

import java.io.OutputStream;
import java.util.HashMap;

import com.google.protobuf.ByteString;

public interface ProtoProcessor<T extends Object> {
	public byte[] serializeDataToByteArray();
	public ByteString serializeDataToByteString();
	public T genProtoObject(HashMap<String, Object> data);
	public void fromByteArray(byte[] data);
	public void writeOutData(OutputStream os);
}
