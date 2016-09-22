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
import android.widget.Toast;

public class MainActivity extends Activity {
	private FastListView listView;
	ArrayList<Item> list;
	FastListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ConfigScreen.init(MainActivity.this);
		buildFileDir();
		
		listView = (FastListView) findViewById(R.id.listView);
		list = TestData.initalizeData(MainActivity.this);
		adapter = new FastListAdapter(list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		listView.setAdapter(null);
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/listCache");
		File[] files = file.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

	private void buildFileDir() {
		if (hasSdcard()) {
			File file = new File(Environment.getExternalStorageDirectory()+"/listCache");
			if (!file.exists()) {
				file.mkdir();
			}
		}
		else{
			Toast.makeText(MainActivity.this, "«Î≤Â»ÎSDø®", Toast.LENGTH_LONG).show();
		}
	}

	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
