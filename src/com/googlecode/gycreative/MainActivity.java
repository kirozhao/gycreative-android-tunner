package com.googlecode.gycreative;

import java.io.File;
import java.util.ArrayList;
import com.googlecode.gycreative.cpbitmapcache.util.ConfigScreen;
import com.googlecode.gycreative.fastlist.FastListAdapter;
import com.googlecode.gycreative.fastlist.FastListView;
import com.googlecode.gycreative.fastlist.Item;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends Activity {
	private FastListView listView;
	ArrayList<Item>list;
	FastListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ConfigScreen.init(MainActivity.this);
		listView = (FastListView)findViewById(R.id.listView);
		list = TestData.initalizeData(MainActivity.this);
		adapter = new FastListAdapter(list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		listView.setAdapter(null);
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/listCache");
		File[] files = file.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
		super.onDestroy();
	}
}
