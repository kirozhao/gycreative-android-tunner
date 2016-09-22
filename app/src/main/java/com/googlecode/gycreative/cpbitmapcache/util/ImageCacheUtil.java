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
 * @version  createTime:20132013-3-30����3:52:54
 * */
public class ImageCacheUtil {


		/**
		 * ��ȡ���ʵ�Bitmap�������Ȳ���������inSampleSize������С�ڴ�ռ�ã�.
		 * @param path ·��.
		 * @param data byte[]����.
		 * @param context ������
		 * @param uri uri
		 * @param target �Զ��ĳ���Ϊ��ʾĿ�꣨һ����ݻ��͵ķֱ��ʶ��壩
		 * @param width �Ƿ�ֻ�Կ��Ϊ׼
		 * @return
		 */
		public static Bitmap getResizedBitmap(String path, byte[] data,
				Context context,Uri uri, int target, boolean width) {
			Options options = null;

			if (target > 0) {

				Options info = new Options();
				//��������true��ʱ�򣬽�ͼƬ��߶�ȡ����Options�

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
				//��������true��ʱ�򣬽�ͼƬ��߶�ȡ����Options�
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
		 * ����Bitmap�ķ���1.��ע��uri����path2��Ϊ�յ�ʱ��contextҲ����Ϊ�գ�
		 * @param path ��ַ
		 * @param data  ������������
		 * @param context ������
		 * @param uri  uri��ַ
		 * @param options ͼƬoptions����
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
				//uri��Ϊ�յ�ʱ��contextҲ����Ϊ��.
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
		 * ����Bitmap�ķ���2.��ע��uri����path2��Ϊ�յ�ʱ��contextҲ����Ϊ�գ�
		 * @param path ��ַ
		 * @param data  ������������
		 * @param context ������
		 * @param uri  uri��ַ
		 * @param options ͼƬoptions����
		 * @param path2 assets��ַ
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
				//uri��Ϊ�յ�ʱ��contextҲ��ҪΪ��.
				ContentResolver cr = context.getContentResolver();
				InputStream inputStream = null;

				try {
					inputStream = cr.openInputStream(uri);
					result = BitmapFactory.decodeStream(inputStream, null, options);
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					//��ʹ����ʱ�����쳣��ҲҪ�ر���
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
						//��ʹ����ʱ�����쳣��ҲҪ�ر���
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
		 * ��ȡ���ʵ�sampleSize.
		 * google�Ƽ�ʹ��2�ı��������Ҳ�����˶Աȹ������γɺ��ʵ�sampleSizeֵ��2�ı�����
		 * @param width��ԭͼ�Ĳ���ֵ--�������ǿ�
		 * @param target���Ƚϵ�Ŀ��ֵ��
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
