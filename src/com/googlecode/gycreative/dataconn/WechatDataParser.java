package com.googlecode.gycreative.dataconn;

import java.io.IOException;
import java.io.InputStream;

import com.google.protobuf.ByteString;

/**
 * @author desmo
 * 解析微信的协议头部和获得数据内容
 */
public class WechatDataParser {
	// 定义几种协议消息类型，仅供模拟

	// 消息通知
	public static final int TYPE_NOTYFY_TEXT = 4;
	public static final int TYPE_NOTYFY_VOICE = 5;
	public static final int TYPE_NOTYFY_IMAGE = 6;
	/****以下3种类型表示是上次请求***/
	// 接受图片
	public static final int TYPE_IMAGE = 1;
	//文本
	public static final int TYPE_TEXT=2;
	// 接受语音
	public static final int TYPE_VOICE = 3;

	public static final int HEADER_LENGTH = 16;
	public static final int BUFFER_SIZE = 4096;
	private int content_length = 0;
	private InputStream inputStream;

	public WechatDataParser() {
		super();
	}

	public WechatDataParser(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}

	public void resetReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Header.headerinfo getHeader() {
		byte[] data = new byte[HEADER_LENGTH];

		try {
			int n = inputStream.read(data);
			//已读完或者头部数据不足
			if (n==-1&&n!=HEADER_LENGTH)
				return null;

			//Header.headerinfo header = Header.headerinfo.parseFrom(data);
			//content_length = header.getContentLength();
			
			String string = data.toString();
			 Header.headerinfo.Builder builder=Header.headerinfo.newBuilder();
			 builder.setProto(Integer.valueOf(string.substring(0, 4)));
			 builder.setTYPE(Integer.valueOf(string.substring(4, 8)));
			 builder.setSEQC(Integer.valueOf(string.substring(8, 12)));
			 builder.setContentLength(Integer.valueOf(string.substring(12, 16)));
			 
			 Header.headerinfo header= builder.build();
			 
			
			content_length = header.getContentLength();
			return header;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static Header.headerinfo getHeaderFromBytes(byte[] msg){
		

		String string = new String(msg, 0, HEADER_LENGTH);
		 Header.headerinfo.Builder builder=Header.headerinfo.newBuilder();
		 builder.setProto(Integer.valueOf(string.substring(0, 4)));
		 builder.setTYPE(Integer.valueOf(string.substring(4, 8)));
		 builder.setSEQC(Integer.valueOf(string.substring(8, 12)));
		 builder.setContentLength(Integer.valueOf(string.substring(12, 16)));
		 
		 Header.headerinfo header= builder.build();
		 
		
		return header;
		
	}
	

	public byte[] getBody() {
		byte[] data = new byte[content_length];
		try {
			inputStream.read(data);

			return data;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public int getContent_length() {
		return content_length;
	}

	public void setContent_length(int content_length) {
		this.content_length = content_length;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
