package com.googlecode.gycreative.dataconn;

import java.net.URI;

import android.content.Context;
import android.net.Uri;

public class SocketDataConnectionImpl implements DataConnection{
	private Context context;

	private URI uri;



	public SocketDataConnectionImpl(Context context, URI uri) {
		super();
		this.context = context;
		this.uri = uri;
	}



	@Override
	public void send() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRecv() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotify() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatus() {
		// TODO Auto-generated method stub
		
	}
	
	
}
