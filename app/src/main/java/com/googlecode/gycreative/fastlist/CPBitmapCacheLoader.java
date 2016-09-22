package com.googlecode.gycreative.fastlist;

import android.R.drawable;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.Adapter;
import android.widget.ImageView;
import com.googlecode.gycreative.R;
import com.googlecode.gycreative.cpbitmapcache.AsyncBitmapCache;

public class CPBitmapCacheLoader {
	private AsyncBitmapCache myBitmapCache;
	private ImageView imageview;
	
	public CPBitmapCacheLoader(){
		myBitmapCache = new AsyncBitmapCache();
	}
	
	public void displayImage(Context context, String url,
			String fileString, int size, boolean widthFlag,
			ImageView imageview) {
		this.imageview = imageview;
		Drawable cacheImage_icon = myBitmapCache.loadDrawable(context,
				url, callback, fileString, size, false);
		if (cacheImage_icon == null) {
			// 载入中，先暂时将其设置为默认载入图片；
			imageview.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.default_item));
		} else {
			// 载入完成 ，将其设置为已载入图片；
			imageview.setImageDrawable(cacheImage_icon);
		}
	}

	AsyncBitmapCache.ImageCallback_LW callback = new AsyncBitmapCache.ImageCallback_LW() {
		public void imageLoaded(Drawable imageDrawable, String imageUrls) {
			// 载入完成 ，将其设置为已载入图片；
			imageview.setImageDrawable(imageDrawable);
		}
	};
}
