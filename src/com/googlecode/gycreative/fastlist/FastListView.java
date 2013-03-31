package com.googlecode.gycreative.fastlist;

import java.util.ArrayList;

import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Process;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.widget.ListView;


public class FastListView extends ListView{
	private List<Long>timeList = new ArrayList<Long>();
	private long currentTime;
	private boolean refreshFPSflag;
	private boolean removeFlag;
	private Paint paint;
	
	public FastListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		currentTime=0;
		refreshFPSflag = removeFlag =false;
		paint = new Paint();
		activateStrictMode();
		startCalculateFps();
	}
	
	private void activateStrictMode() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
						.detectDiskReads()
						.detectDiskWrites()
						.detectAll() 
						.penaltyLog() // logcat
						.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // SQLite
				.penaltyLog() // logcat
				.penaltyDeath().build());
	}

	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(!refreshFPSflag)
			timeList.add( System.currentTimeMillis() );
		else 
			refreshFPSflag=false;
		canvas.drawText("fps:"+ timeList.size(), 300, 10, paint);
	}
	
	private void startCalculateFps() {
		Thread thread = new Thread(){
			@Override
			public void run(){
				try {
					while(true){
						Thread.sleep(200);
						refreshFPSflag=false;
						removeFlag=false;
						currentTime = System.currentTimeMillis();
						for(int i=0;i<timeList.size();i++){
							if(timeList.get(i) < currentTime-1000){
								timeList.remove(i);
								i--;
								removeFlag=true;
							}
							else break;
						}
						if(removeFlag){
							refreshFPSflag=true;
							postInvalidate();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thread.setPriority( Process.THREAD_PRIORITY_BACKGROUND );
		thread.start();
	}

}
