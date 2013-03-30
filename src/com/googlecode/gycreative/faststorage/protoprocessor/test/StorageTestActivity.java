package com.googlecode.gycreative.faststorage.protoprocessor.test;

import java.util.HashMap;

import com.googlecode.gycreative.R;
import com.googlecode.gycreative.faststorage.FastImageStorage;
import com.googlecode.gycreative.faststorage.FastStorage;
import com.googlecode.gycreative.faststorage.FastStorage.CachePolicy;
import com.googlecode.gycreative.faststorage.benchmark.ImageStorageBenchmark;
import com.googlecode.gycreative.faststorage.benchmark.StorageBenchmark;
import com.googlecode.gycreative.faststorage.exception.ErrorCachePolicy;
import com.googlecode.gycreative.faststorage.protoprocessor.ImageProtoProcessor;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.TextView;

public class StorageTestActivity extends Activity {

	TextView textView;
	FastImageStorage storage;
	Bitmap bitmap;
	Bitmap resultBitmap;
	ImageStorageBenchmark imageStorageBenchmark;
	int testPicNum = 100;
	boolean simulateSlow = false;
	boolean ifAsync = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage_test);
		this.textView = (TextView) findViewById(R.id.text);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		// prepare the test data
		textView.setText("Benchmark, simulateSlow: " + simulateSlow + "\n ifAsync: " + ifAsync + " \n\n");
		HashMap<String, ImageProtoProcessor> data = new HashMap<String, ImageProtoProcessor>();
		for (int i = 0; i < testPicNum; i++) {
			data.put(i + "", new ImageProtoProcessor(i + "", bitmap));
		}
		
		
		// mem cache
		imageStorageBenchmark = new ImageStorageBenchmark(this, CachePolicy.MEM_CACHE);
		textView.append("Now is memCache\n");
		textView.append("put " + testPicNum + " pictures: use " + imageStorageBenchmark.testPutData(data, simulateSlow) + "ms" + "\n");
		try {
			if (ifAsync == false)
				textView.append("get " + testPicNum + " pictures: use " + imageStorageBenchmark.testGetAllData(simulateSlow) + "ms" + "\n");
			else {
				imageStorageBenchmark.testAsynGetAllData(simulateSlow, new StorageBenchmark.AsyncLoadFinishCallback() {

					@Override
					public void onFinish(long time) {
						// TODO Auto-generated method stub
						textView.append("get " + testPicNum + " pictures: use " + time + "ms" + "\n");
					}
					
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textView.append(e.getLocalizedMessage() + "\n");
		}
		
		/*
		// mem and db cache
		textView.append("\n");
		imageStorageBenchmark = new ImageStorageBenchmark(this, CachePolicy.MEM_DB_CACHE);
		textView.append("Now is memDBCache\n");
		textView.append("put " + testPicNum + " pictures: use " + imageStorageBenchmark.testPutData(data, simulateSlow) + "ms" + "\n");
		try {
			if (ifAsync == false)
				textView.append("get " + testPicNum + " pictures: use " + imageStorageBenchmark.testGetAllData(simulateSlow) + "ms" + "\n");
			else {
				imageStorageBenchmark.testAsynGetAllData(simulateSlow, new StorageBenchmark.AsyncLoadFinishCallback() {

					@Override
					public void onFinish(long time) {
						// TODO Auto-generated method stub
						textView.append("get " + testPicNum + " pictures: use " + time + "ms" + "\n");
					}
					
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textView.append(e.getLocalizedMessage() + "\n");
		}*/
	
		
		// mem and file cache
		textView.append("\n");
		imageStorageBenchmark = new ImageStorageBenchmark(this, CachePolicy.MEM_FILE_CACHE);
		textView.append("Now is memFileCache\n");
		textView.append("put " + testPicNum + " pictures: use " + imageStorageBenchmark.testPutData(data, simulateSlow) + "ms" + "\n");
		try {
			if (ifAsync == false)
				textView.append("get " + testPicNum + " pictures: use " + imageStorageBenchmark.testGetAllData(simulateSlow) + "ms" + "\n");
			else {
				imageStorageBenchmark.testAsynGetAllData(simulateSlow, new StorageBenchmark.AsyncLoadFinishCallback() {

					@Override
					public void onFinish(long time) {
						// TODO Auto-generated method stub
						textView.append("get " + testPicNum + " pictures: use " + time + "ms" + "\n");
					}
					
				});
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textView.append(e.getLocalizedMessage() + "\n");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.storage_test, menu);
		return true;
	}

}
