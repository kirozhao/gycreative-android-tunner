package com.googlecode.gycreative.dataconn;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.googlecode.gycreative.dataconn.Header.headerinfo;

public abstract class  DataConnection {
	public static final String  HTTPPOST_KEY = "HTTP_POST";
	WechatDataParser parser;
	LoadImageCallBack loadImageCallBack;
	LoadTextCallBack loadTextCallBack;
	
	
	void init() {
		
		   parser = new WechatDataParser();
	}

	/**
	 * @param bytes 
	 * socket通信时表示要发送协议的字节数组，http通信时表示要post的协议的字节数组
	 */
	public abstract void send(byte[] bytes);
	
	/**
	 * 这个包是前一次send的onRecv,接受完数据调用callback
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
	 * 为其他推送的消息
	 */
	void onNotify(){
		byte[] bytes = parser.getBody();
	}
	
	void onStatus(){
		
	}
	
	/**
	 * 接受数据并解析头部进行状态分发回调
	 */
	protected void parseData() {
		do{
		headerinfo header = parser.getHeader();
		if(header == null){
			try {
				parser.getInputStream().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		}
		int type = header.getTYPE();
		
		switch (type) {
		case WechatDataParser.TYPE_NOTYFY:
			onNotify();
			
			break;
		case WechatDataParser.TYPE_IMAGE:
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
