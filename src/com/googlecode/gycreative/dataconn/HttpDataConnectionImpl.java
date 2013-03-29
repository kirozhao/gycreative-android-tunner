package com.googlecode.gycreative.dataconn;


import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

public class HttpDataConnectionImpl implements DataConnection{
	
	public static final String CTWAP = "ctwap";
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	public static final int NETWORK_DISABLED = 0;
	// 移动联通wap 10.0.0.172 代理
	public static final int PROXY_CM_CU_WAP172 = 1;
	//电信wap 10.0.0.200 
	public static final int PROXY_CT_WAP200 = 2;
	//电信,移动,联通,wifi 等net网络 
	public static final int 	NP_NET = 3;
	public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	
	private static final int TIMEOUT = 10000;
	private static final int TIMEOUT_SOCKET = 15000;
	
	
	private Context context;

	private URI uri;
	
	
	public HttpDataConnectionImpl(Context context, URI uri) {
		super();
		this.context = context;
		this.uri = uri;
	}



	public static HttpClient getNewInstance(Context mContext) {
		HttpClient newInstance;

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		ConnManagerParams.setTimeout(params, 5000);

		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);

		HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);

		newInstance = new DefaultHttpClient(conMgr, params);

		switch (checkNetworkType(mContext)) {
		case PROXY_CT_WAP200: {
			HttpHost proxy = new HttpHost("10.0.0.200", 80, "http");
			newInstance.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		
		}
			break;
		case PROXY_CM_CU_WAP172: {
			
			HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
			newInstance.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		
		}
			break;
		}
		return newInstance;
	}
	
	

	/***
	 * 判断Network具体类型（联通移动wap，电信wap，其他net）
	 * 
	 * */
	public static int checkNetworkType(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isAvailable()) {

				return NETWORK_DISABLED;
			} else {
				int netType = networkInfo.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
				
					return PROXY_CM_CU_WAP172;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				
					final Cursor c = mContext.getContentResolver().query(
							PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c
								.getColumnIndex("user"));
						if (!TextUtils.isEmpty(user)) {
							
							if (user.startsWith(CTWAP)) {
							
								return PROXY_CT_WAP200;
							}
						}
					}
					c.close();

					
					String netMode = networkInfo.getExtraInfo();
				
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase();
						if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
								|| netMode.equals(UNIWAP)) {
							return PROXY_CT_WAP200 ;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return NETWORK_DISABLED;
		}
		return NETWORK_DISABLED;
	}



	@Override
	public void send() {
		// TODO Auto-generated method stub
		HttpClient httpClient = getNewInstance(context);
		HttpGet httpGet = new HttpGet(uri);
		
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
