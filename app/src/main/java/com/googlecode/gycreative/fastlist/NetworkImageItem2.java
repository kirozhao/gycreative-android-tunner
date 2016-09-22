package com.googlecode.gycreative.fastlist;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import com.googlecode.gycreative.R;
import com.googlecode.gycreative.cpbitmapcache.AsyncBitmapCache;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class NetworkImageItem2 implements Item{
	private final LayoutInflater inflater;
	private final Context context;
	private FastStorageBitmapLoader fastStorageBitmapLoader;
	String  imageString;
	AsyncBitmapCache myBitmapCache;
	public static Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ImageView imageView;
	
	
	public NetworkImageItem2(String image,LayoutInflater inflater, Context context){
		imageString = image;
		this.inflater = inflater;
		this.context = context;
		fastStorageBitmapLoader = new FastStorageBitmapLoader();
		myBitmapCache= new AsyncBitmapCache();
	}
	
	public void setImage(String s){
		imageString = s;
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.image_item;
	}

	@Override
	public View getView(View convertView) {
		View view=convertView;
	        if(convertView==null)
	            view = inflater.inflate(R.layout.image_item, null);
	        ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
	        imageViews.put(imageView, imageString);
	        fastStorageBitmapLoader.showImage(imageView,imageString,context);
	        return view;
	}

	@Override
	public int getViewType() {
		return ItemType.WITHIMAGE.ordinal();
	}

	 static boolean imageViewReused(String key,ImageView imageView){
		        String tag=imageViews.get(imageView);
		        if(tag==null || !tag.equals(key))
		            return true;
		        return false;
	 }
}
