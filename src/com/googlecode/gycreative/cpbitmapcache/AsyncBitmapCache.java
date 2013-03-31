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
 * @version  createTime:20132013-3-30上午7:55:26
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
	 * 获取软引用下 的Drawable对象（将会自行测量并进行设置inSampleSize操作参数缩小内存占用）.
	 * @param Context context 上下文
	 * @param String imageUrl 图片地址(一般这个地址唯一，即可当做该图片的标示码，引用该Url图片都使用这个做标示)
	 * @param ImageCallback_LW  imageCallback 异步回调接口
	 * @param String savefile 缓存地址sd卡根目录后的地址
	 * @param int target 显示在长宽多少的目标上（一般根据机型的分辨率定义，取长宽之间一个合适的数值即可）
	 * @param boolean bywidth 是否只以需显示图的宽度为产生inSampleSize的标准
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
						System.out.println("异常-》图片文件大小判断");
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
						//这里设置true的时候，decode时候只会将Bitmap宽高读取放在Options里.
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
