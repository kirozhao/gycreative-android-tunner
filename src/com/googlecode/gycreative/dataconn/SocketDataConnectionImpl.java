package com.googlecode.gycreative.dataconn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

import android.content.Context;

public class SocketDataConnectionImpl extends DataConnection {
	private Context context;



	private URI uri;
	Socket socket;
	Thread readerThread;


	public SocketDataConnectionImpl(Context context, URI uri) {
		super();
		this.context = context;
		this.uri = uri;

		init();
	}

	public void init() {
		try {
			socket = new Socket(uri.getHost(), uri.getPort());

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	@Override
	public void send(byte[] bytes) {
		try {
			
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(bytes);
			InputStream inputStream = socket.getInputStream();
			parser.setInputStream(inputStream);
			parseData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
