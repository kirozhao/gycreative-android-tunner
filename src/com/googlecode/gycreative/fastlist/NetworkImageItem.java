package com.googlecode.gycreative.fastlist;

import com.googlecode.gycreative.R;
import com.googlecode.gycreative.cpbitmapcache.AsyncBitmapCache;
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
	private static FastListAdapter adapter;
	String textString;
	String  imageString;
	AsyncBitmapCache myBitmapCache;
	
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
		myBitmapCache= new AsyncBitmapCache();
	}
	
	public void setAdapter(FastListAdapter adapter){
		this.adapter = adapter;
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
		final ViewHolder holder;
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
	        //cpBitmapCacheLoader.displayImage( context, imageString,
				//"listCache", 40, true, holder.imageView, adapter);
	        
	        
	        
		
		Drawable cacheImage_icon = myBitmapCache.loadDrawable(context,
				imageString, new AsyncBitmapCache.ImageCallback_LW() {
					public void imageLoaded(
							Drawable imageDrawable,
							String imageUrls) {
						// 载入完成 ，将其设置为已载入图片；
						holder.imageView.setImageDrawable(imageDrawable);
						adapter.notifyDataSetChanged();
					}
				}, "listCache", 800, false);

		if (cacheImage_icon == null) {
			// 载入中，先暂时将其设置为默认载入图片；
			holder.imageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.ic_launcher));

		} else {
			// 载入完成 ，将其设置为已载入图片；
			holder.imageView.setImageDrawable(cacheImage_icon);
		}
	        
	   
	        return view;
	}

	@Override
	public int getViewType() {
		return ItemType.WITHIMAGE.ordinal();
	}

}
