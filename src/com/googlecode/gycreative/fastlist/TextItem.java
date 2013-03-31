package com.googlecode.gycreative.fastlist;

import com.googlecode.gycreative.R;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextItem implements Item{
	private String textString;
	private final LayoutInflater inflater;
	
	private static class ViewHolder {
		TextView textView;
		private ViewHolder(TextView tv) {
			this.textView = tv; 
		}
	}
	
	public TextItem(String s, LayoutInflater inflater){
		textString = s;
		this.inflater = inflater;
	}
	
	public void  setText(String s){
		textString = s;
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.text_item;
	}

	@Override
	public View getView(View convertView) {
		ViewHolder holder;
		View view;
		
		//create a new one
		if (convertView == null) {
			ViewGroup viewGroup = (ViewGroup) inflater.inflate( R.layout.text_item, null );
			holder = new ViewHolder((TextView) viewGroup.findViewById(R.id.textView));
			viewGroup.setTag(holder);
			view = viewGroup;
		} else {
			holder = (ViewHolder) convertView.getTag();
			view = convertView;
		}
		holder.textView.setText( textString+"" );
		return view;
	}

	@Override
	public int getViewType() {
		return 0;
		//return ItemType.TEXT.ordinal();
	}
	
}
