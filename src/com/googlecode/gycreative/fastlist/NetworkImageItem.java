package com.googlecode.gycreative.fastlist;

import com.googlecode.gycreative.R;
import com.googlecode.gycreative.dataconn.DataConnection;
import com.googlecode.gycreative.dataconn.DataConnectionFactory;
import com.googlecode.gycreative.dataconn.DataConnection.LoadImageCallBack;
import com.googlecode.gycreative.faststorage.FastImageStorage;
import com.googlecode.gycreative.faststorage.FastStorage;
import com.googlecode.gycreative.faststorage.FastStorage.StorageCallback;
import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NetworkImageItem implements Item{
	private final LayoutInflater inflater;
	private final Context context;
	private CPBitmapCacheLoader cpBitmapCacheLoader;
	String textString;
	String  imageString;
	ImageView imageView;
	
	private static class ViewHolder {
		final ImageView imageView;
		final TextView textView;
		private ViewHolder(ImageView iv, TextView tv) {
			this.imageView = iv;
			this.textView = tv;
		}
	}
	
	public NetworkImageItem(String text,String image,LayoutInflater inflater, Context context){
		textString = text;
		imageString = image;
		this.inflater = inflater;
		this.context = context;
		cpBitmapCacheLoader = new CPBitmapCacheLoader();
	}
	
	public void setText(String s){
		textString = s;
	}
	
	public void setImage(String s){
		imageString = s;
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.with_image_item;
	}

	@Override
	public View getView(View convertView) {
		ViewHolder holder;
	        View view;
	        if (convertView == null) {
	        	ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.with_image_item, null);
	        	holder = new ViewHolder((ImageView)viewGroup.findViewById(R.id.imageView),
	        			(TextView)viewGroup.findViewById(R.id.textView));
	        	viewGroup.setTag(holder);
	        	view = viewGroup;
	        } else {
	        	view = convertView;
	        	holder = (ViewHolder)convertView.getTag();
	        }
	        holder.textView.setText(textString);
	        imageView = holder.imageView;
	        
	        //holder.imageView.setText(animal.getName());
	        return view;
	}

	@Override
	public int getViewType() {
		return ItemType.WITHIMAGE.ordinal();
	}

}
