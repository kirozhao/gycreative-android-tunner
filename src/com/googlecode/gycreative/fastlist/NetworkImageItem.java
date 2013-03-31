package com.googlecode.gycreative.fastlist;

import com.googlecode.gycreative.R;
import com.googlecode.gycreative.cpbitmapcache.AsyncBitmapCache;
import com.googlecode.gycreative.cpbitmapcache.util.ConfigScreen;
import com.googlecode.gycreative.dataconn.DataConnection;
import com.googlecode.gycreative.dataconn.DataConnectionFactory;
import com.googlecode.gycreative.dataconn.DataConnection.LoadImageCallBack;
import com.googlecode.gycreative.faststorage.FastImageStorage;
import com.googlecode.gycreative.faststorage.FastStorage;
import com.googlecode.gycreative.faststorage.FastStorage.StorageCallback;
import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;

import android.R.raw;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NetworkImageItem implements Item{
	private final LayoutInflater inflater;
	private final Context context;
	private CPBitmapCacheLoader cpBitmapCacheLoader;
	String  imageString;
	AsyncBitmapCache myBitmapCache;
	
	private static class ViewHolder {
		final ImageView imageView;
		private ViewHolder(ImageView iv) {
			this.imageView = iv;
		}
	}
	
	public NetworkImageItem(String image,LayoutInflater inflater, Context context){
		imageString = image;
		this.inflater = inflater;
		this.context = context;
		cpBitmapCacheLoader = new CPBitmapCacheLoader();
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
		final ViewHolder holder;
	        View view;
	        if (convertView == null) {
	        	ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.image_item, null);
	        	holder = new ViewHolder((ImageView)viewGroup.findViewById(R.id.imageView));
	        	viewGroup.setTag(holder);
	        	view = viewGroup;
	        } else {
	        	view = convertView;
	        	holder = (ViewHolder)convertView.getTag();
	        }
	        cpBitmapCacheLoader.displayImage( context, imageString,
				"listCache", ConfigScreen.int_target , true, holder.imageView);
	        return view;
	}

	@Override
	public int getViewType() {
		return ItemType.WITHIMAGE.ordinal();
	}
}
