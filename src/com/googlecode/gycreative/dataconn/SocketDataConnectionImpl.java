package com.googlecode.gycreative.dataconn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

import android.content.Context;

public class SocketDataConnectionImpl extends DataConnection {

	private URI uri;
	Socket socket;
	MsgWriter msgWriter;
	


	/**
	 * 阻塞构造，建立长连接,创建写线程，构造线程成为读线程。
	 * @param context
	 * @param uri
	 */
	public SocketDataConnectionImpl(URI uri) {
		super();
		
		this.uri = uri;
		
		//建立socket和写线程,心跳线程并初始化parser，并开始读数据
		init();
	}

	public void init() {
		try {
			socket = new Socket(uri.getHost(), uri.getPort());
			msgWriter = new MsgWriter(this);
			msgWriter.startup();
			InputStream inputStream = socket.getInputStream();
			parser.setInputStream(inputStream);
			parseData();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

	/* (non-Javadoc)
	 * @see com.googlecode.gycreative.dataconn.DataConnection#send(byte[])
	 */
	@Override
	public void send(byte[] bytes) {
	
			
			msgWriter.sendMsg(bytes);
			
		
		
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		msgWriter.shutdown();
		try {
		parser.getInputStream().close();
		
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
