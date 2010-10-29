package org.joldersma.damien.DreamSpell;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DreamSpell extends Activity implements OnTouchListener {
	
	public static final String TAG = "DreamSpell";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"** ON CREATE **");
		setContentView(R.layout.main);

		calc();
		 
//		Button nextDayButton = (Button) findViewById(R.id.nextDayButton);
//		nextDayButton.setOnClickListener(new View.OnClickListener() {
//	        public void onClick(View v) {
//	            Log.d(TAG,"nextDayButton clicked");
//	            
//	            addDay();
//	        }
//		});
//		
//		Button prevDayButton = (Button) findViewById(R.id.prevDayButton);
//		prevDayButton.setOnClickListener(new View.OnClickListener() {
//	        public void onClick(View v) {
//	            Log.d(TAG,"prevDayButton clicked");
//	            
//	            removeDay();
//	        }
//		});
//		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		Log.d(TAG,"onTouchEvent! ");
		
		GestureDetector gestureDetector = new GestureDetector(gestureListener);
		if ( gestureDetector.onTouchEvent(event) )
		{
			Log.d(TAG,"onTouchEvent case1");
			return true;
		}
		else
		{
			Log.d(TAG,"onTouchEvent case2");
			return false;
		}
	}
	
	

	SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		private int SWIPE_MIN_DISTANCE = 120;
		private int SWIPE_MAX_OFF_PATH = 250;
		private int SWIPE_THRESHOLD_VELOCITY = 200;
		
		@Override 
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) 
		{
			Log.d(TAG,"Checking onFling: " + e1.getX() + " " + e2.getX() + " " + velocityX);
			if ( e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY ) 
			{
				// do something
				Log.d(TAG,"OnFling case1");
				return true;
			} 
			else if ( e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY )
			{
				// do something else
				Log.d(TAG,"OnFling case2");
				return true;
			}
			
			return false;
		}
	};
	
	private void calc() {
		calc(new Date(System.currentTimeMillis()));
	}

	private void calc(Date d) {
		DreamSpellUtil.Calc(d);

		TextView mDateTitle = (TextView) findViewById(R.id.date_title);
		mDateTitle.setText(DateFormat.getDateInstance().format(
				DreamSpellUtil.getCurrentDate()));

		TextView mDateName = (TextView) findViewById(R.id.date_name);
		mDateName.setText(DreamSpellUtil.GetName());
			
		int t = DreamSpellUtil.getTone();
		ImageView tone = (ImageView) findViewById(R.id.tone);
		tone.setImageResource(AndroidUtil.getToneResource(DreamSpellUtil.getTone()));
		
		ImageView seal = (ImageView) findViewById(R.id.seal);
		seal.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getSeal()));
		
		ImageView analog = (ImageView) findViewById(R.id.analog);
		analog.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getAnalog()));
		
		ImageView occult = (ImageView) findViewById(R.id.occult);
		occult.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getOccult()));
		
		ImageView antipode = (ImageView) findViewById(R.id.antipode);
		antipode.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getAntipode()));
		
		ImageView guide = (ImageView) findViewById(R.id.guide);
		guide.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getGuide()));
	}

	private void addDay() {
		Calendar c = Calendar.getInstance();
		Date d = DreamSpellUtil.currentDate;
		c.setTime(d);
		c.add(Calendar.DAY_OF_YEAR,1);
		calc(c.getTime());
	}

	private void removeDay() {
		Calendar c = Calendar.getInstance();
		Date d = DreamSpellUtil.currentDate;
		c.setTime(d);
		c.add(Calendar.DAY_OF_YEAR,-1);
		calc(c.getTime());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d(TAG, "onTouch");
		return false;
	}
}