package com.googlecode.gycreative.faststorage.protoprocessor.test;

import com.googlecode.gycreative.R;
import com.googlecode.gycreative.faststorage.FastImageStorage;
import com.googlecode.gycreative.faststorage.FastStorage;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storage_test);
		this.textView = (TextView) findViewById(R.id.text);
		this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		try {
			storage = new FastImageStorage(this, FastStorage.CachePolicy.MEM_FILE_CACHE);
		} catch (ErrorCachePolicy e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		storage.setStorageCallback(new FastStorage.StorageCallback<ImageProtoProcessor>() {

			@Override
			public void loadError(String key, Exception e) {
				// TODO Auto-generated method stub
				textView.setText("load error, " + e.getMessage());
			}

			@Override
			public void afterLoad(String key, ImageProtoProcessor data) {
				// TODO Auto-generated method stub
				textView.setText("finished load");
				resultBitmap = data.toBitmap();
				textView.append(resultBitmap.getWidth() + ", " + resultBitmap.getHeight());
			}
			
		});
		
		storage.put("image", new ImageProtoProcessor("image", bitmap));
		storage.loadData("image");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.storage_test, menu);
		return true;
	}

}
