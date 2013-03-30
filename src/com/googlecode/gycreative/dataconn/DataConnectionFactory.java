package com.googlecode.gycreative.dataconn;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.content.Context;


public class DataConnectionFactory {
	public static final String PROTOCOL_HTTP="http";
	public static final String PROTOCOL_HTTPS="https";
	public static final String PROTOCOL_SOCKET="socket";
 
	public static DataConnection openConnection(Context context,final String uri) {
		// TODO:
			
			URI _uri;
			try {
				_uri = new URI(uri);
			
			if(PROTOCOL_HTTP.equals(_uri.getScheme())||PROTOCOL_HTTPS.equals(_uri.getScheme())){
				return new HttpDataConnectionImpl(context,_uri);
				
			}else if (PROTOCOL_SOCKET.equals(_uri.getScheme())) {
				return new SocketDataConnectionImpl(context,_uri);
			}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

	}
}
