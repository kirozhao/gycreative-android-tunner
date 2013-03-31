package com.googlecode.gycreative.dataconn;

/**
 * �������͵�����ͳ��
 * @author desmo
 *
 */
public class WechatTrafficStat {
	
	public static final int  TYPE_IMAGE = 0;
	public static final int  TYPE_TEXT = 1;
	public static final int  TYPE_VOICE = 2;
	public static final int  TYPE_VIDEO = 3;
	
	private static int textBytes = 0;
	private static int voicebytes = 0;
	private static int imageBytes = 0;
	private static int videoBytes = 0;
	
	private static int sendtextBytes = 0;
	private static int sendvoicebytes = 0;
	private static int sendimageBytes = 0;
	private static int sendvideoBytes = 0;
	
	public static  synchronized void addSendTextbytes(int add){
		sendtextBytes += add;
	}
	
	public static synchronized void addSendVoicebytes(int add){
		sendvoicebytes += add;
	}
	
	public static synchronized void addSendImagebytes(int add){
		sendimageBytes += add;
	}
	
	public static synchronized  void addSendVideobytes(int add){
		sendvideoBytes += add;
	}
	
	
	public static  synchronized void addRecvTextbytes(int add){
		textBytes += add;
	}
	
	public static synchronized void addRecvVoicebytes(int add){
		voicebytes += add;
	}
	
	public static synchronized void addRecvImagebytes(int add){
		imageBytes += add;
	}
	
	public static synchronized  void addRecvVideobytes(int add){
		videoBytes += add;
	}
	
	/**
	 * �־û�����ͳ�ƣ��ɿ������ݿ���ļ�����������ͳ�ƿ��Ƶ�����)
	 * �������Ҫ�����������д
	 */
	public synchronized static void dump(){
		//�־û���������Ϊ0
		 textBytes = 0;
		 voicebytes = 0;
		 imageBytes = 0;
		 videoBytes = 0;
		
		 sendtextBytes = 0;
		 sendvoicebytes = 0;
		 sendimageBytes = 0;
		 sendvideoBytes = 0;
	}
	
	
	
}
