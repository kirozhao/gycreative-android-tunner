package com.googlecode.gycreative.dataconn;

public interface DataConnection {

	void send();
	
	void onRecv();
	
	void onNotify();
	
	void onStatus();
}
