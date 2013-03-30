package com.googlecode.gycreative.cpbitmapcache.util;


import java.io.IOException;
import java.io.InputStream;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;

/**
 * @author zhouml
 * @version  createTime:20132013-3-30下午3:52:54
 * */
public class ImageCacheUtil {


		/**
		 * 获取合适的Bitmap（将会先测量并进行inSampleSize操作缩小内存占用）.
		 * @param path 路径.
		 * @param data byte[]数组.
		 * @param context 上下文
		 * @param uri uri
		 * @param target 以多大的长宽为显示目标（一般根据机型的分辨率定义）
		 * @param width 是否只以宽度为准
		 * @return
		 */
		public static Bitmap getResizedBitmap(String path, byte[] data,
				Context context,Uri uri, int target, boolean width) {
			Options options = null;

			if (target > 0) {

				Options info = new Options();
				//这里设置true的时候，将图片宽高读取放在Options里。

				info.inJustDecodeBounds = true;
				
				decode(path, data, context,uri, info);
				
				int dim = info.outWidth;
				if (!width)
					dim = Math.max(dim, info.outHeight);
				int ssize = sampleSize(dim, target);

				options = new Options();
				options.inSampleSize = ssize;

			}

			Bitmap bm = null;
			try {
				bm = decode(path, data, context,uri, options);
			} catch(Exception e){
				e.printStackTrace();
			}
			return bm;

		}
		
		
		
		public static Bitmap getResizedBitmap(String path, byte[] data,
				Context context,Uri uri, int target, boolean width,String path2) {
			Options options = null;

			if (target > 0) {

				Options info = new Options();
				//这里设置true的时候，将图片宽高读取放在Options里。
				info.inJustDecodeBounds = true;
				
				decode(path, data, context,uri, info);
				
				int dim = info.outWidth;
				if (!width)
					dim = Math.max(dim, info.outHeight);
				int ssize = sampleSize(dim, target);

				options = new Options();
				options.inSampleSize = ssize;

			}

			Bitmap bm = null;
			try {
				bm = decode(path, data, context,uri, options,path2);
			} catch(Exception e){
				e.printStackTrace();
			}
			return bm;

		}
		
		
		/**
		 * 解析Bitmap的方法1.（注意uri或者path2不为空的时候context也不能为空）
		 * @param path 地址
		 * @param data  二进制数据流
		 * @param context 上下文
		 * @param uri  uri地址
		 * @param options 图片options参数
		 * @return Bitmap
		 */
		public static Bitmap decode(String path, byte[] data, Context context,
				Uri uri, BitmapFactory.Options options) {

			Bitmap result = null;

			if (path != null) {

				result = BitmapFactory.decodeFile(path, options);

			} else if (data != null) {

				result = BitmapFactory.decodeByteArray(data, 0, data.length,
						options);

			} else if (uri != null) {
				//uri不为空的时候context也不能为空.
				ContentResolver cr = context.getContentResolver();
				InputStream inputStream = null;

				try {
					inputStream = cr.openInputStream(uri);
					result = BitmapFactory.decodeStream(inputStream, null, options);
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			return result;
		}
		
		/**
		 * 解析Bitmap的方法2.（注意uri或者path2不为空的时候context也不能为空）
		 * @param path 地址
		 * @param data  二进制数据流
		 * @param context 上下文
		 * @param uri  uri地址
		 * @param options 图片options参数
		 * @param path2 assets地址
		 * @return Bitmap
		 */
		public static Bitmap decode(String path, byte[] data, Context context,
				Uri uri, BitmapFactory.Options options,String path2) {

			Bitmap result = null;

			if (path != null) {

				result = BitmapFactory.decodeFile(path, options);

			} else if (data != null) {

				result = BitmapFactory.decodeByteArray(data, 0, data.length,
						options);

			} else if (uri != null) {
				//uri不为空的时候context也不要为空.
				ContentResolver cr = context.getContentResolver();
				InputStream inputStream = null;

				try {
					inputStream = cr.openInputStream(uri);
					result = BitmapFactory.decodeStream(inputStream, null, options);
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					//即使保存时发生异常，也要关闭流
		        	try {
						if (inputStream != null)
							{inputStream.close();
						}  
						} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}

			}else if(path2!=null){
				
				 AssetManager assetManager = context.getAssets();
				 InputStream inputStream2=null;
			        try {
			         inputStream2= assetManager.open(path2);
			         result = BitmapFactory.decodeStream(inputStream2, null, options);
			         inputStream2.close();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }finally {
						//即使保存时发生异常，也要关闭流
			        	try {
							if (inputStream2 != null)
								{inputStream2.close();
							
							}  
							} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
			        
			        
				
			}

			return result;
		}
		
		
		/**
		 * 获取合适的sampleSize.
		 * google推荐使用2的倍数，这边也进行了对比估计以形成合适的sampleSize值（2的倍数）
		 * @param width（原图的参量值--长或者是宽）
		 * @param target（比较的目标值）
		 * @return
		 */
		public static int sampleSize(int width, int target){	    	
		    	int result = 1;	    	
		    	for(int i = 0; i < 10; i++){	    		
		    		if(width < target * 2){
		    			break;
		    		}	    		
		    		width = width / 2;
		    		result = result * 2;	    		
		    	}	    	
		    	return result;
		    }


}
