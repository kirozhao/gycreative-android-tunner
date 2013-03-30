package com.googlecode.gycreative.fastlist;

import java.util.ArrayList;

import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ListView;


public class FastListView extends ListView{
	private List<Long>timeList = new ArrayList<Long>();
	private long currentTime;
	private boolean refreshFPSflag;
	private Paint paint;
	
	public FastListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		currentTime=0;
		refreshFPSflag=false;
		paint = new Paint();
		startCalculateFps();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(!refreshFPSflag)
			timeList.add( System.currentTimeMillis() );
		canvas.drawText("fps:"+ timeList.size(), 300, 10, paint);
	}
	
	private void startCalculateFps() {
		// TODO Auto-generated method stub
		new Thread(){
			@Override
			public void run(){
				try {
					while(true){
						Thread.sleep(50);
						refreshFPSflag=false;
						currentTime = System.currentTimeMillis();
						for(int i=0;i<timeList.size();i++){
							if(timeList.get(i) < currentTime-1000){
								timeList.remove(i);
								i--;
								refreshFPSflag=true;
							}
							else break;
						}
						if(refreshFPSflag)
							postInvalidate();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

}
