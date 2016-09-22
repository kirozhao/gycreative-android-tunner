package com.googlecode.gycreative.cpbitmapcache;






import com.googlecode.gycreative.R;
import com.googlecode.gycreative.cpbitmapcache.CPBitmap.Stub;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @author zhouml
 * @version  createTime:20132013-3-31下午9:29:12
 * */
public class CPBitmapCache extends Service {

	private CPBinder MycpBinder;

	private Context mcontext;
	public class CPBinder extends Stub
	{

		@Override
		public Bitmap getCPBitmap() throws RemoteException {
			// TODO Auto-generated method stub
//			int myint=0;
			Drawable cachedImage_icon = AsyncBitmapCache.getBitmapCache( "http://a3.twimg.com/profile_images/670625317/aam-logo-v3-twitter.png");


					if (cachedImage_icon == null) {
					//载入中，先暂时将其设置为默认载入图片；
						cachedImage_icon=mcontext.getResources().getDrawable(R.drawable.ic_launcher);
					}
			
					
			return drawableToBitmap(cachedImage_icon);
		}


	}
	@Override
	public void onCreate()
	{
		super.onCreate();

		mcontext=this;
		MycpBinder=new CPBinder();

	}
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return MycpBinder;
	}
	@Override
	public void onDestroy()
	{
	}	
	
	
	public Bitmap drawableToBitmap(Drawable drawable) {   
        
        Bitmap bitmap = Bitmap   
                        .createBitmap(   
                                        drawable.getIntrinsicWidth(),   
                                        drawable.getIntrinsicHeight(),   
                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888   
                                                        : Bitmap.Config.RGB_565);   
        Canvas canvas = new Canvas(bitmap);   
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());   
        drawable.draw(canvas);   
        return bitmap;   
}  
	
	
}
