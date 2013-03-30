package com.googlecode.gycreative.fastlist;

import com.googlecode.gycreative.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.TextSize;
import android.widget.ImageView;
import android.widget.TextView;

public class WithImageItem implements Item{
	private final LayoutInflater inflater;
	String textString;
	//image information
	
	private static class ViewHolder {
		final ImageView imageView;
		final TextView textView;
		private ViewHolder(ImageView iv, TextView tv) {
			this.imageView = iv;
			this.textView = tv;
		}
	}
	
	public WithImageItem(String s,LayoutInflater inflater){
		textString = s;
		this.inflater = inflater;
	}
	
	public void setText(String s){
		textString = s;
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
	        //holder.imageView.setText(animal.getName());
	        return view;
	}

	@Override
	public int getViewType() {
		return ItemType.WITHIMAGE.ordinal();
	}

}
