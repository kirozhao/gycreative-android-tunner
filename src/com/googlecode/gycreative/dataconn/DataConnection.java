package com.googlecode.gycreative.dataconn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.googlecode.gycreative.dataconn.Header.headerinfo;

public abstract class  DataConnection {
	public static final String  HTTPPOST_KEY = "HTTP_POST";
	WechatDataParser parser;
	LoadImageCallBack loadImageCallBack;
	LoadTextCallBack loadTextCallBack;
	
	
	/**
	 * ��ʼ��Э�����ݽ�����
	 */
	void init() {
		
		   parser = new WechatDataParser();
	}

	/**
	 * ͬ��������ʽ
	 * @param bytes 
	 * socketͨ��ʱ��ʾҪ����Э����ֽ�����(Ĭ�ϳ����ӣ�������write��close()�����ӹر�)��httpͨ��ʱ��ʾҪpost��Э����ֽ�����
	 */
	public abstract void send(byte[] bytes);
	/**
	 * �ر�����
	 */
	public abstract void shutdown();
	/**
	 * �������ǰһ��send��onRecv,���������ݵ���callback
	 */
	void onRecv(){
		
		if(loadImageCallBack != null){
			Bitmap bitmap = BitmapFactory.decodeStream(parser.getInputStream());
			if(bitmap!=null)
			loadImageCallBack.imageLoaded(bitmap);
			
		}
		
		if(loadTextCallBack != null){
			String textString = parser.getBody().toString();
			loadTextCallBack.textLoaded(textString);
		}
		loadImageCallBack = null;
		loadTextCallBack = null;
	}
	
	/**
	 * Ϊ�������͵���Ϣ�����ݲ�ͬ���ͽ��зַ���������listeners
	 * 
	 */
	void onNotify(){
		byte[] bytes = parser.getBody();
	}
	
	void onStatus(){
		
	}
	
	/**
	 * �������ݲ�����ͷ������״̬�ַ��ص�
	 */
	protected void parseData() {
		do{
		headerinfo header = parser.getHeader();
		//�������Ѿ��ر����ӣ��������ݴ���
		if(header == null){
			
				shutdown();
				
		
			break;
			
		}
		int type = header.getTYPE();
		
		switch (type) {
		case WechatDataParser.TYPE_NOTYFY:
			onNotify();
			
			break;
		case WechatDataParser.TYPE_IMAGE:
		case WechatDataParser.TYPE_TEXT:
			onRecv();

		default:
			break;
		}
		}while(true);
		
		

	}
	

	
	public interface LoadImageCallBack{
		public void imageLoaded(Bitmap bitmap);
	}
	
	public interface LoadTextCallBack{
		public void textLoaded(String text);
	}
	
	
	public LoadImageCallBack getLoadImageCallBack() {
		return loadImageCallBack;
	}

	public void setLoadImageCallBack(LoadImageCallBack loadImageCallBack) {
		this.loadImageCallBack = loadImageCallBack;
	}

	public LoadTextCallBack getLoadTextCallBack() {
		return loadTextCallBack;
	}

	public void setLoadTextCallBack(LoadTextCallBack loadTextCallBack) {
		this.loadTextCallBack = loadTextCallBack;
	}

	
}
