package com.googlecode.gycreative.dataconn;

import com.googlecode.gycreative.dataconn.Header.headerinfo;

public abstract class  DataConnection {
	public static final String  HTTPPOST_KEY = "HTTP_POST";
	WechatDataParser parser;
	void send(char[] bytes){
		
	}
	
	void onRecv(){
		
	}
	
	void onNotify(){
		
	}
	
	void onStatus(){
		
	}
	
	protected void parseData() {
		headerinfo header = parser.getHeader();
		int type = header.getTYPE();
		
		switch (type) {
		case WechatDataParser.TYPE_NOTYFY:
			onNotify();
			
			break;
		case WechatDataParser.TYPE_IMAGE:
			

		default:
			break;
		}

	}

	
}
