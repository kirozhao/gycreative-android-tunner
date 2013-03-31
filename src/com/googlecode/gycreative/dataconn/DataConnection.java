package com.googlecode.gycreative.dataconn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.googlecode.gycreative.dataconn.Header.headerinfo;

public abstract class DataConnection {
	public static final String HTTPPOST_KEY = "HTTP_POST";
	WechatDataParser parser;
	LoadImageCallBack loadImageCallBack;
	LoadTextCallBack loadTextCallBack;
	//�Ƿ�����
	protected boolean connected = false;
	// ���ֽ��������������͸���Э��ͷ���ֶ�
	int recvtype = 0;
	// ���ַ��������������͸���Э��ͷ���ֶ�
	int sendtype = 0;

	/**
	 * ��ʼ��Э�����ݽ�����
	 */
	void init() {

		parser = new WechatDataParser();
		 connected = true;
	}

	/**
	 * socket�·�����
	 * http��������ʽ
	 * 
	 * @param bytes
	 *            socketͨ��ʱ��ʾҪ����Э����ֽ�����(Ĭ�ϳ����ӣ�������write��close()�����ӹر�)��
	 *            httpͨ��ʱ��ʾҪpost��Э����ֽ�����
	 */
	public abstract void send(byte[] bytes);

	/**
	 * �ر�����
	 */
	public abstract void shutdown();

	/**
	 * �������ǰһ��send��onRecv,���������ݵ���callback
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
	 * Ϊ�������͵���Ϣ�����ݲ�ͬ���ͽ��зַ���������listeners
	 * 
	 */
	void onNotify() {
		byte[] bytes = parser.getBody();
		onRecvStatus();

	}

	/**
	 * ���ͻ����Э�����ݵ�ʱ��ͷ���ֶ������������ͺͳ��ȵ��ֶΣ� �ڴ˿���ʵ����Բ�ͬ���͵�������¼����������ͼƬ�����֣���Ƶ)
	 * ����uri����������ֻ��ͳ�Ʒ������Ͳ��ܻ���յ������ͣ�������ҪЭ��ͷ����֧��
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
	 * �������ݲ�����ͷ������״̬�ַ��ص�
	 */
	protected void parseData() {
		do {
			headerinfo header = parser.getHeader();
			// �������Ѿ��ر����ӣ��������ݴ���
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
	 * ת����Ϣͷ����Ϊ��������
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
