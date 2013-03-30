package com.googlecode.gycreative.dataconn;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

import android.content.Context;

public class SocketDataConnectionImpl implements DataConnection {
	private Context context;
	Reader reader;
	Writer writer;

	private URI uri;
	Socket socket;

	public SocketDataConnectionImpl(Context context, URI uri) {
		super();
		this.context = context;
		this.uri = uri;

	}

	public void init() {
		try {
			socket = new Socket(uri.getHost(), uri.getPort());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void send() {
		
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
