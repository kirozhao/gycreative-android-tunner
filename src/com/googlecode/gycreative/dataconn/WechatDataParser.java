package com.googlecode.gycreative.dataconn;

import java.io.IOException;
import java.io.Reader;

public class WechatDataParser {
	//���弸��Э��ģʽ������ģ��
	
	//��Ϣ֪ͨ
	public static final int TYPE_NOTYFY = 0;
	//����ͼƬ
	public static final int TYPE_IMAGE= 1;
	//��������
	public static final int TYPE_VOICE= 2;

	public static final int HEADER_LENGTH = 16;
	private Reader reader;
	public WechatDataParser() {
		super();
	}
	
	public WechatDataParser(Reader reader) {
		super();
		this.reader = reader;
	}
	
	public void resetReader(Reader reader){
		this.reader = reader;
	}

	public Header.headerinfo getHeader(){
		char[] data = new char[HEADER_LENGTH];
		try {
			reader.read(data);
			Header.headerinfo.Builder builder=Header.headerinfo.newBuilder();
			builder.setProto(Integer.valueOf(String.valueOf(data, 0, 4)));
			builder.setTYPE(Integer.valueOf(String.valueOf(data, 4, 4)));
			builder.setSEQC(Integer.valueOf(String.valueOf(data, 8, 4)));
			builder.setContentLength(Integer.valueOf(String.valueOf(data, 12, 4)));
			Header.headerinfo header= builder.build();
			
			return header;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	
	}
	

}
