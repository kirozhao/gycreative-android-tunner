package com.googlecode.gycreative.dataconn;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

public class SocketDataConnectionImpl extends DataConnection {

	private URI uri;
	Socket socket;
	MsgWriter msgWriter;
	MsgReader msgReader;
	


	/**
	 * �������죬����������,����д�̣߳����̡߳�
	 * @param context
	 * @param uri
	 */
	public SocketDataConnectionImpl(URI uri) {
		super();
		
		this.uri = uri;
		
		//����socket��д�̺߳Ͷ��߳�,�����̲߳���ʼ��parser������ʼ������
		init();
	}

	public void init() {
		try {
			socket = new Socket(uri.getHost(), uri.getPort());
			msgWriter = new MsgWriter(this);
			msgWriter.startup();
			InputStream inputStream = socket.getInputStream();
			parser.setInputStream(inputStream);
			msgReader = new MsgReader(this, parser);
			msgReader.startup();
			//parseData();
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
	
			if(connected)
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
		 connected = false;
		try {
		parser.getInputStream().close();
		
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
