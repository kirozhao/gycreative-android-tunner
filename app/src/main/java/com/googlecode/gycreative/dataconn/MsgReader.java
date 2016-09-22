package com.googlecode.gycreative.dataconn;


public class MsgReader {
    private Thread readerThread;

   
    private SocketDataConnectionImpl connection;
    WechatDataParser parser;
  
	public MsgReader(SocketDataConnectionImpl connection,
			WechatDataParser parser) {
		super();
		
		this.connection = connection;
		this.parser = parser;
		init();
	}
    
    
	   protected void init() {
	    	
	      
	    	 readerThread= new Thread() {
	            public void run() {
	                connection.parseData();
	            }
	        };
	        
	        readerThread.setDaemon(true);
	       
	 
	       
	    }
	   
	    public void startup(){
	    	 readerThread.start();
	    }
    
}
