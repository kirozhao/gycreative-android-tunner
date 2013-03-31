package com.googlecode.gycreative;

import java.util.ArrayList;
import com.googlecode.gycreative.fastlist.FastListAdapter;
import com.googlecode.gycreative.fastlist.FastListView;
import com.googlecode.gycreative.fastlist.Item;
import com.googlecode.gycreative.fastlist.TextItem;
import com.googlecode.gycreative.fastlist.WithImageItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FastListView listView = (FastListView)findViewById(R.id.listView);
		
		ArrayList<Item> list = new ArrayList<Item>();
		TextItem item1 = new TextItem("123", LayoutInflater.from(MainActivity.this));
		list.add(item1);
		TextItem item2 = new TextItem("234", LayoutInflater.from(MainActivity.this));
		list.add(item2);
		WithImageItem item3 = new WithImageItem("234", LayoutInflater.from(MainActivity.this));
		list.add(item3);
		FastListAdapter adapter = new FastListAdapter(list);
		listView.setAdapter(adapter);
	}

}
