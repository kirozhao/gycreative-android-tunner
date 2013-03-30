package com.googlecode.gycreative.faststorage.protoprocessor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.gycreative.faststorage.protodata.Imageproto;
import com.googlecode.gycreative.faststorage.protodata.Imageproto.Image;

public class ImageProtoProcessor implements ProtoProcessor<Imageproto.Image> {

	public Imageproto.Image image = null;
	

	@Override
	/**
	 * data is [("imageKey", keyValue), ("imageData", byteStringData)]
	 */
	public Image genProtoObject(HashMap<String, Object> data) {
		// TODO Auto-generated method stub
		Imageproto.Image.Builder builder = Imageproto.Image.newBuilder();
		String key = (String) data.get("imageKey");
		ByteString byteData = (ByteString) data.get("imageData");
		builder.setImageKey(key).setImageData(byteData);
		this.image = builder.build();
		return image;
 	}


	@Override
	public byte[] serializeDataToByteArray() {
		// TODO Auto-generated method stub
		if (this.image != null)
			return image.toByteArray();
		return null;
	}


	@Override
	public ByteString serializeDataToByteString() {
		// TODO Auto-generated method stub
		if (this.image != null)
			return image.toByteString();
		return null;
	}

	public Bitmap toBitmap() {
		byte[] data = this.image.toByteArray();
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}


	@Override
	public void fromByteArray(byte[] data) {
		// TODO Auto-generated method stub
		try {
			this.image = Image.parseFrom(data);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void writeOutData(OutputStream os) {
		// TODO Auto-generated method stub
		try {
			this.image.writeTo(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
