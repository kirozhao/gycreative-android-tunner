package com.googlecode.gycreative;

import java.util.ArrayList;
import com.googlecode.gycreative.fastlist.FastListAdapter;
import com.googlecode.gycreative.fastlist.FastListView;
import com.googlecode.gycreative.fastlist.Item;
import com.googlecode.gycreative.fastlist.TextItem;
import com.googlecode.gycreative.fastlist.NetworkImageItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FastListView listView = (FastListView)findViewById(R.id.listView);
		FastListAdapter adapter = new FastListAdapter();
		
		ArrayList<Item> list = new ArrayList<Item>();
		TextItem item1 = new TextItem("123", LayoutInflater.from(MainActivity.this));
		list.add(item1);
		TextItem item2 = new TextItem("234", LayoutInflater.from(MainActivity.this));
		list.add(item2);
		NetworkImageItem item3 = new NetworkImageItem(
				"234",
				"http://blog.ce.cn/sp1/blog_attachments/2009/08/355332_200908251016235zoac.jpg",
				LayoutInflater.from(MainActivity.this), this);
		item3.setAdapter(adapter);
		list.add(item3);
		
		adapter.addItems(list);
		listView.setAdapter(adapter);
	}

}
