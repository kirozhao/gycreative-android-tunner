package com.googlecode.gycreative.cpbitmapcache.util;

import android.app.Activity;
import android.util.DisplayMetrics;


/**
 * @author zhouml
 * @version  createTime:20132013-3-31下午12:14:16
 * */
public class ConfigScreen {

//	屏幕参数
	public static int screenHeight=0;
	public static int screenWidth=0;
	public static float screenDensity=0;
	
//	用于配置AsyncBitmapCache 参数 int target 
	public static int int_target=400;
	
	public static void init(Activity context) {
		if(screenDensity==0||screenWidth==0||screenHeight==0){
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			ConfigScreen.screenDensity = dm.density;
			ConfigScreen.screenHeight = dm.heightPixels;
			ConfigScreen.screenWidth = dm.widthPixels;
		}

		int_target=Math.max(ConfigScreen.screenHeight, ConfigScreen.screenWidth)/2;

	}
	
	
	
	
	
}
