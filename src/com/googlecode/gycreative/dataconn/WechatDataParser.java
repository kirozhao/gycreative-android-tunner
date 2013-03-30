package com.googlecode.gycreative.dataconn;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author desmo
 * ����΢�ŵ�Э��ͷ���ͻ����������
 */
public class WechatDataParser {
	// ���弸��Э��ģʽ������ģ��

	// ��Ϣ֪ͨ
	public static final int TYPE_NOTYFY = 0;
	/****����3�����ͱ�ʾ���ϴ�����***/
	// ����ͼƬ
	public static final int TYPE_IMAGE = 1;
	//�ı�
	public static final int TYPE_TEXT=2;
	// ��������
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
			//�Ѷ������ͷ�����ݲ���
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
