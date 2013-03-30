package com.googlecode.gycreative.dataconn;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author desmo
 * 解析微信的协议头部和获得数据内容
 */
public class WechatDataParser {
	// 定义几种协议模式，仅供模拟

	// 消息通知
	public static final int TYPE_NOTYFY = 0;
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

			Header.headerinfo header = Header.headerinfo.parseFrom(data);
			content_length = header.getContentLength();
			/*
			 * Header.headerinfo.Builder builder=Header.headerinfo.newBuilder();
			 * builder.setProto(Integer.valueOf(String.valueOf(data, 0, 4)));
			 * builder.setTYPE(Integer.valueOf(String.valueOf(data, 4, 4)));
			 * builder.setSEQC(Integer.valueOf(String.valueOf(data, 8, 4)));
			 * builder.setContentLength(Integer.valueOf(String.valueOf(data, 12,
			 * 4)));
			 * 
			 * Header.headerinfo header= builder.build();
			 */
			return header;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

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

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
