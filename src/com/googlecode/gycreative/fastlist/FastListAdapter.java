package com.googlecode.gycreative.fastlist;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FastListAdapter extends BaseAdapter {
	private List<Item>allDataObj;
	
	public FastListAdapter(){
		allDataObj = new ArrayList<Item>();
	}
	
	public FastListAdapter(List<Item>list){
		allDataObj = new ArrayList<Item>();
		allDataObj.addAll(list);
	}
	
	public void addItem(Item i){
		allDataObj.add(i);
	}
	
	public void addItems(List<Item>list){
		allDataObj.addAll( list );
	}
	
	@Override
	public int getCount() {
		return allDataObj.size();
	}

	@Override
	public Object getItem(int position) {
		return allDataObj.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return ItemType.values().length;
	}
	
	@Override
        public int getItemViewType(int position) {
            return allDataObj.get(position).getViewType();
        }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//firstly pre-load some image of the following items
		return allDataObj.get(position).getView(convertView);		
	}
}
