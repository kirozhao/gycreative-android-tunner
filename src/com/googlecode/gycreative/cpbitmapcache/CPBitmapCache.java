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
 * @version  createTime:20132013-3-31����9:29:12
 * */
public class CPBitmapCache extends Service {

	private CPBinder MycpBinder;
	private AsyncBitmapCache myBitmapCache ;
	private Context mcontext;
	public class CPBinder extends Stub
	{

		@Override
		public Bitmap getCPBitmap() throws RemoteException {
			// TODO Auto-generated method stub
//			int myint=0;
			Drawable cachedImage_icon = myBitmapCache.loadDrawable(mcontext,
					"http://image.sjrjy.com/201011/291354164ea84578066309.jpg", new AsyncBitmapCache.ImageCallback_LW() {
					public void imageLoaded(Drawable imageDrawable,String imageUrls) {
					
						
					}
					},"listCache",400,false);
			

					if (cachedImage_icon == null) {
					//�����У�����ʱ��������ΪĬ������ͼƬ��
						cachedImage_icon=mcontext.getResources().getDrawable(R.drawable.ic_launcher);

					} else {
					//������� ����������Ϊ������ͼƬ��
						return drawableToBitmap(cachedImage_icon);
					}
			
			
			return drawableToBitmap(cachedImage_icon);
		}


	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		myBitmapCache = new AsyncBitmapCache();
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
