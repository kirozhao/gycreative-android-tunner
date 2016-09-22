package com.googlecode.gycreative.cpbitmapcache;

import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.googlecode.gycreative.cpbitmapcache.util.ImageCacheUtil;
import com.googlecode.gycreative.cpbitmapcache.util.ImageOperation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;



/**
 * @author zhouml
 * @version  createTime:20132013-3-30����7:55:26
 * */
public class AsyncBitmapCache {
	private static HashMap<String, SoftReference<Drawable>> imageCache=new HashMap<String, SoftReference<Drawable>>();;

	public AsyncBitmapCache() {

	}
	
	
	
	public static Drawable  getBitmapCache(final String imageUrl) {
		Drawable mydrawable = null;
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			mydrawable= softReference.get();
	
		}

			return mydrawable;

	}
	
	/**
	 * ��ȡ�������� ��Drawable���󣨽������в�������������inSampleSize����������С�ڴ�ռ�ã�.
	 * @param Context context ������
	 * @param String imageUrl ͼƬ��ַ(һ�������ַΨһ�����ɵ�����ͼƬ�ı�ʾ�룬���ø�UrlͼƬ��ʹ���������ʾ)
	 * @param ImageCallback_LW  imageCallback �첽�ص��ӿ�
	 * @param String savefile �����ַsd����Ŀ¼��ĵ�ַ
	 * @param int target ��ʾ�ڳ�����ٵ�Ŀ���ϣ�һ����ݻ��͵ķֱ��ʶ��壬ȡ����֮��һ�����ʵ���ֵ���ɣ�
	 * @param boolean bywidth �Ƿ�ֻ������ʾͼ�Ŀ��Ϊ����inSampleSize�ı�׼
	 * @return Drawable
	 */
	public Drawable loadDrawable(final Context context,final String imageUrl, final ImageCallback_LW imageCallback,
			final String savefile,final int target,final boolean bywidth) {
		
		
		if(imageUrl==null|| imageUrl.equals("")) return null;
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				return drawable;
			}
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap bitmap = null;
				File cacheDir;
				 if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			        cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),savefile);
			    }else{
			        cacheDir=context.getCacheDir();
			    }
				if (!cacheDir.exists()) {
					cacheDir.mkdirs();
				}
				File file = new File(cacheDir+ "/" +imageUrl.hashCode());
				boolean isLoad = false;
				if (!file.isFile()) {
					isLoad = ImageOperation.loadImageFromUrl(context, imageUrl,savefile);
				} else {
					FileInputStream fis;
					int size = 0;
					try {
						fis = new FileInputStream(file);
						size = (fis.available() / 1000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("�쳣-��ͼƬ�ļ���С�ж�");
					}
					if (size <= 0) {
						file.delete();
						isLoad =ImageOperation.loadImageFromUrl(context, imageUrl,savefile);
					}
				}
				if (isLoad){
					bitmap = null;
					
				}
				else{
					BitmapFactory.Options options = null;

					if ( target > 0) {

						Options info = new Options();
						//��������true��ʱ��decodeʱ��ֻ�ὫBitmap��߶�ȡ����Options��.
						info.inJustDecodeBounds = true;
						BitmapFactory.decodeFile(cacheDir+ "/" +imageUrl.hashCode(), info);

						int dim = info.outWidth;
						if (!bywidth)
							dim = Math.max(dim, info.outHeight);
						int ssize = ImageCacheUtil.sampleSize(dim,  target);

						options = new Options();
						options.inSampleSize = ssize;

					}

					bitmap = BitmapFactory.decodeFile(cacheDir+ "/" +imageUrl.hashCode(), options);

				}
				
				Drawable drawable = new BitmapDrawable(bitmap);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	
	
	
	
	public interface ImageCallback_LW{
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
	

}
