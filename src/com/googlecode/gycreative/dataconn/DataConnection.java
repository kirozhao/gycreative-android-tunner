package com.googlecode.gycreative.dataconn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.googlecode.gycreative.dataconn.Header.headerinfo;

public abstract class DataConnection {
	public static final String HTTPPOST_KEY = "HTTP_POST";
	WechatDataParser parser;
	LoadImageCallBack loadImageCallBack;
	LoadTextCallBack loadTextCallBack;
	//是否还连接
	protected boolean connected = false;
	// 区分接受数据流量类型根据协议头部字段
	int recvtype = 0;
	// 区分发送数据流量类型根据协议头部字段
	int sendtype = 0;

	/**
	 * 初始化协议数据解析器
	 */
	void init() {

		parser = new WechatDataParser();
		 connected = true;
	}

	/**
	 * socket下非阻塞
	 * http下阻塞方式
	 * 
	 * @param bytes
	 *            socket通信时表示要发送协议的字节数组(默认长连接，服务器write后close()则连接关闭)，
	 *            http通信时表示要post的协议的字节数组
	 */
	public abstract void send(byte[] bytes);

	/**
	 * 关闭连接
	 */
	public abstract void shutdown();

	/**
	 * 这个包是前一次send的onRecv,接受完数据调用callback
	 */
	void onRecv() {

		if (loadImageCallBack != null) {
			Bitmap bitmap = BitmapFactory.decodeStream(parser.getInputStream());
			if (bitmap != null)
				loadImageCallBack.imageLoaded(bitmap);

		}

		if (loadTextCallBack != null) {
			String textString = parser.getBody().toString();
			loadTextCallBack.textLoaded(textString);
		}
		loadImageCallBack = null;
		loadTextCallBack = null;

		onRecvStatus();
	}

	/**
	 * 为其他推送的消息，根据不同类型进行分发，可设置listeners
	 * 
	 */
	void onNotify() {
		byte[] bytes = parser.getBody();
		onRecvStatus();

	}

	/**
	 * 发送或接受协议数据的时候，头部字段中有内容类型和长度的字段， 在此可以实现针对不同类型的流量记录（如语音，图片，文字，视频)
	 * 由于uri里设置类型只能统计发送类型不能获得收到的类型，所以需要协议头部的支持
	 */
	synchronized void onRecvStatus() {
		switch (recvtype) {
		case WechatTrafficStat.TYPE_IMAGE:
			WechatTrafficStat.addRecvImagebytes(parser.getContent_length());

			break;
		case WechatTrafficStat.TYPE_TEXT:
			WechatTrafficStat.addRecvTextbytes(parser.getContent_length());
			break;
		case WechatTrafficStat.TYPE_VOICE:
			WechatTrafficStat.addRecvVoicebytes(parser.getContent_length());

			break;
		case WechatTrafficStat.TYPE_VIDEO:
			WechatTrafficStat.addRecvVideobytes(parser.getContent_length());

			break;

		default:
			break;
		}

	}

	synchronized void onSendStatus(byte[] msg) {

		Header.headerinfo header = WechatDataParser.getHeaderFromBytes(msg);
		sendtype = getTypeFromMsgType(header.getTYPE());
		switch (sendtype) {
		case WechatTrafficStat.TYPE_IMAGE:
			WechatTrafficStat.addSendImagebytes(parser.getContent_length());

			break;
		case WechatTrafficStat.TYPE_TEXT:
			WechatTrafficStat.addRecvTextbytes(parser.getContent_length());
			break;
		case WechatTrafficStat.TYPE_VOICE:
			WechatTrafficStat.addRecvVoicebytes(parser.getContent_length());

			break;
		case WechatTrafficStat.TYPE_VIDEO:
			WechatTrafficStat.addRecvVideobytes(parser.getContent_length());

			break;

		default:
			break;
		}

	}

	/**
	 * 接受数据并解析头部进行状态分发回调
	 */
	protected void parseData() {
		do {
			headerinfo header = parser.getHeader();
			// 服务器已经关闭连接，或者数据错误
			if (header == null) {

				shutdown();

				break;

			}

			int MsgType = header.getTYPE();
			recvtype = getTypeFromMsgType(MsgType);
			switch (MsgType) {
			case WechatDataParser.TYPE_NOTYFY_IMAGE:
			case WechatDataParser.TYPE_NOTYFY_VOICE:
			case WechatDataParser.TYPE_NOTYFY_TEXT:

				onNotify();

				break;
			case WechatDataParser.TYPE_IMAGE:

			case WechatDataParser.TYPE_TEXT:

				onRecv();
				break;
			default:
				break;
			}
		} while (true);

	}

	/**
	 * 转换消息头类型为流量类型
	 * @param MsgType
	 * @return
	 */
	public int getTypeFromMsgType(int MsgType) {

		int type = 0;
		switch (MsgType) {
		case WechatDataParser.TYPE_NOTYFY_IMAGE:

			type = WechatTrafficStat.TYPE_IMAGE;

		case WechatDataParser.TYPE_NOTYFY_VOICE:
			type = WechatTrafficStat.TYPE_VOICE;

		case WechatDataParser.TYPE_NOTYFY_TEXT:
			type = WechatTrafficStat.TYPE_TEXT;

		case WechatDataParser.TYPE_IMAGE:
			type = WechatDataParser.TYPE_IMAGE;

		case WechatDataParser.TYPE_TEXT:
			type = WechatDataParser.TYPE_TEXT;
		case WechatDataParser.TYPE_VOICE:
			type = WechatDataParser.TYPE_VOICE;

		default:
			break;
		}
		return type;

	}

	public interface LoadImageCallBack {
		public void imageLoaded(Bitmap bitmap);
	}

	public interface LoadTextCallBack {
		public void textLoaded(String text);
	}

	public LoadImageCallBack getLoadImageCallBack() {
		return loadImageCallBack;
	}

	public void setLoadImageCallBack(LoadImageCallBack loadImageCallBack) {
		this.loadImageCallBack = loadImageCallBack;
	}

	public LoadTextCallBack getLoadTextCallBack() {
		return loadTextCallBack;
	}

	public void setLoadTextCallBack(LoadTextCallBack loadTextCallBack) {
		this.loadTextCallBack = loadTextCallBack;
	}

}
