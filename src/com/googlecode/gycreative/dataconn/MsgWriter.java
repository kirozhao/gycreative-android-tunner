package com.googlecode.gycreative.dataconn;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;




public class MsgWriter {
		//心跳频率 30秒
		private static int keepAliveInterval = 30000;
	    private Thread writerThread;
	    private Thread keepAliveThread;
	    OutputStream writer;
	    private SocketDataConnectionImpl connection;
	    private final BlockingQueue<byte[]> queue;
	    private boolean done;
	    
	    /**
	     * 记录上次发消息时间，来判断是否需要发心跳包
	     */
	    private long lastActive = System.currentTimeMillis();
	    
	    protected MsgWriter(SocketDataConnectionImpl connection) {
	        this.queue = new ArrayBlockingQueue<byte[]>(500, true);
	        this.connection = connection;
	        init();
	    }
	    
	    
	    protected void init() {
	    	 done = false;
	        try {
	        	writer = connection.socket.getOutputStream();
				  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	        writerThread = new Thread() {
	            public void run() {
	                writeMsgs(this);
	            }
	        };
	        
	        writerThread.setDaemon(true);
	 
	       
	    }
	    
	    public void startup() {
	        writerThread.start();
	        startKeepAliveThread();
	    }
	    
	    private byte[] nextMsg() {
	    	byte[] msg= null;
	        while (!done && (msg = queue.poll()) == null) {
	            try {
	                synchronized (queue) {
	                    queue.wait();
	                }
	            }
	            catch (InterruptedException ie) {
	              
	            }
	        }
	        return msg;
	    }
	    
	    
	    private void writeMsgs(Thread thisThread) {
	    
	           
	            try {
					
				
	            while (!done && (writerThread == thisThread)) {
	               byte[] msg= nextMsg();
	                if (msg != null) {
	                    synchronized (writer) {
	                        writer.write(msg);
	                        writer.flush();
	                        lastActive = System.currentTimeMillis();
	                    	}
	                }
	            
	            }
	            //如果客户端主动shutdown，必须将queue中剩余消息发出
	            try {
	                synchronized (writer) {
	                   while (!queue.isEmpty()) {
	                     byte[] msg = queue.remove();
	                        writer.write(msg);
	                    }
	                    writer.flush();
	                }
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
	            
	            
	            } catch (Exception e) {
	            	//发生写错误，服务器可能已经close();
	            	if (!done) {
	                    done = true;
	            	}
					e.printStackTrace();
				}
	    }
	    
	    

	    public void sendMsg(byte[] msg) {
	        if (!done) {
	         

	            try {
	                queue.put(msg);
	            }
	            catch (InterruptedException ie) {
	                ie.printStackTrace();
	                return;
	            }
	            synchronized (queue) {
	                queue.notifyAll();
	            }

	        }
	    }
	    public void shutdown() {
	        done = true;
	        synchronized (queue) {
	            queue.notifyAll();//唤醒阻塞队列
	        }
	    }
	    
	    /**
	     * 开启心跳线程
	     */
	    void startKeepAliveThread() {
	        
	        if (keepAliveInterval > 0) {
	            KeepAliveTask task = new KeepAliveTask(keepAliveInterval);
	            keepAliveThread = new Thread(task);
	            keepAliveThread.setDaemon(true);
	            keepAliveThread.start();
	        }
	    }
	    
	    /**
	     * 
	     * 如果一段时间没有发送消息，则发送" "来保持心跳
	     *
	     */
	    private class KeepAliveTask implements Runnable {

	        private int delay;

	        public KeepAliveTask(int delay) {
	            this.delay = delay;
	        }
	        
	        public void run() {
	            try {

	                Thread.sleep(15000);
	            }
	            catch (InterruptedException ie) {
	            	
	            }
	            while (!done ) {
	                synchronized (writer) {
	                  
	                    if (System.currentTimeMillis() - lastActive >= keepAliveInterval) {
	                        try {
	                            writer.write(" ".getBytes());
	                            writer.flush();
	                        }
	                        catch (Exception e) {
	                            // Do nothing
	                        }
	                    }
	                }
	                try {
	                   
	                    Thread.sleep(delay);
	                }
	                catch (InterruptedException ie) {
	                    // Do nothing
	                }
	            }
	        }
	    }
	    
	    
	    
	    
}
	            
	          

	       

