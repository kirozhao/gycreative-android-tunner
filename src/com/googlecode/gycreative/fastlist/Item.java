package com.googlecode.gycreative.fastlist;

import android.view.View;

public interface Item {
	public View getView(View convertView);
	public int getViewType();
	public int getLayoutId();
	public void loadDataByFastStorage();
	public void loadBitmapByCPBitmapCache();
	public void loadDataByDataConnection();
}
