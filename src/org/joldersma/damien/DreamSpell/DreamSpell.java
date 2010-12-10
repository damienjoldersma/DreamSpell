package org.joldersma.damien.DreamSpell;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;


public class DreamSpell extends Activity
 {
	
	public static final String TAG = "DreamSpell";

    private static final int SWIPE_MIN_DISTANCE = 120;
    //private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    
    private DatePickerDialog datePickerDialog;
    
    final static int MENUMODE_SEARCH_KEY = 0;
    final static int MENUMODE_MENU_ITEM = 1;
    final static int MENUMODE_TYPE_TO_SEARCH = 2;
    final static int MENUMODE_DISABLED = 3;
    
    private int mYear;
    private int mMonth;
    private int mDay;

	private int SWIPE_DAYS = 1;
    
    static final int DATE_DIALOG_ID = 0;
    
    public String friendsData = null;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"** ON CREATE **");
		setContentView(R.layout.main);

		// Check for bundle extra 
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		if ( extra != null )
		{
			Log.d(TAG,"Extra is not null");
			Date date = (Date)extra.getSerializable(Friends.KEY_DATE);
			if (date != null)
			{
				Log.d(TAG,"date is not null, calcing goodness");
				calc(date);
			}
			else
			{
				Log.d(TAG,"date is null");
				calc();
			}
				
		}
		else
		{
			Log.d(TAG,"Extra is null");
			calc();
		}
		  // Gesture detection
        gestureDetector = new GestureDetector(new MyGestureDetector());
         gestureListener = new View.OnTouchListener() {
             public boolean onTouch(View v, MotionEvent event) {
                 if (gestureDetector.onTouchEvent(event)) {
                     return true;
                 }
                 return false;
             }
         };

         setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);		
         
         registerForContextMenu(findViewById(R.id.date_title));
	}
	
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(TAG,"onRestart");
		calc();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		Log.d(TAG,"onRestoreInstanceState");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG,"onResume");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG,"onStart");
	}

	private static final int TODAY_ID = Menu.FIRST;
	private static final int DAY_ID = Menu.FIRST+1;
	private static final int FRIENDS_ID = Menu.FIRST+2;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, TODAY_ID,0, R.string.goto_today);
		menu.add(0, DAY_ID,0, R.string.goto_day);
		menu.add(0, FRIENDS_ID,0, R.string.goto_friends);
		return true;
	}

    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
    	Log.d(TAG,"Menu item selected" + item.getItemId());
        switch(item.getItemId()) {
        case TODAY_ID:
            calc();
            return true;
        case DAY_ID:
        	showDialog(DATE_DIALOG_ID);
        	return true;
        case FRIENDS_ID:
        	Intent myIntent = new Intent();
        	myIntent.setClassName("org.joldersma.damien.DreamSpell", "org.joldersma.damien.DreamSpell.Friends");
        	//myIntent.putExtra("com.android.samples.SpecialValue", "Hello, Joe!"); // key/value pair, where key needs current package prefix.
        	myIntent.putExtra("friendsDataResponse","fooboo");
        	startActivity(myIntent); 
        	return true;
        }
        
        return super.onMenuItemSelected(featureId, item);
	}
    
    



	private static final int CHANGE_DAY_1 = Menu.FIRST;
	private static final int CHANGE_DAY_4 = Menu.FIRST+1;
	private static final int CHANGE_DAY_7 = Menu.FIRST+2;
	private static final int CHANGE_DAY_13 = Menu.FIRST+3;
	private static final int CHANGE_DAY_14 = Menu.FIRST+4;
	private static final int CHANGE_DAY_20 = Menu.FIRST+5;
	private static final int CHANGE_DAY_28 = Menu.FIRST+6;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		
		Log.d(TAG,"onCreateContextMenu going!");
		
		//super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Swipe Time Travel");
		menu.add(0, CHANGE_DAY_1,0, R.string.CHANGE_DAY_1);
		menu.add(0, CHANGE_DAY_4,0, R.string.CHANGE_DAY_4);
		menu.add(0, CHANGE_DAY_7,0, R.string.CHANGE_DAY_7);
		menu.add(0, CHANGE_DAY_13,0, R.string.CHANGE_DAY_13);
		menu.add(0, CHANGE_DAY_14,0, R.string.CHANGE_DAY_14);
		menu.add(0, CHANGE_DAY_20,0, R.string.CHANGE_DAY_20);
		menu.add(0, CHANGE_DAY_28,0, R.string.CHANGE_DAY_28);
		
	}

	@Override
	public void onContextMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		
		Log.d(TAG,"onContextMenuClosed going!");
		super.onContextMenuClosed(menu);
		
		
	}

	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onContextItemSelected going! "  + item.getItemId());
		//return super.onContextItemSelected(item);
		
		switch(item.getItemId()) 
		{
		case CHANGE_DAY_1:
			SWIPE_DAYS = 1;
			return true;
		case CHANGE_DAY_4:
			SWIPE_DAYS = 4;
			return true;
		case CHANGE_DAY_7:
			SWIPE_DAYS = 7;
			return true;
		case CHANGE_DAY_13:
			SWIPE_DAYS = 13;
			return true;
		case CHANGE_DAY_14:
			SWIPE_DAYS = 14;
			return true;
		case CHANGE_DAY_20:
			SWIPE_DAYS = 20;
			return true;
		case CHANGE_DAY_28:
			SWIPE_DAYS = 28;
			return true;
		}

		return super.onContextItemSelected(item);
	}



	@Override
    public boolean onSearchRequested() {
		Log.d(TAG, "onSearchRequested!");
		
		showDialog(DATE_DIALOG_ID);
		
		return true;
	}
		
	private void calc() {
		calc(new Date(System.currentTimeMillis()));
	}

	private void calc(Date d) {
		// get the current date
        final Calendar c = Calendar.getInstance();
        c.setTime(d);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        if ( datePickerDialog != null )
        {
        	datePickerDialog.updateDate(mYear, mMonth, mDay);
        }
		
		DreamSpellUtil.Calc(d);

		TextView mDateTitle = (TextView) findViewById(R.id.date_title);
		mDateTitle.setText(DateFormat.getDateInstance().format(
				DreamSpellUtil.getCurrentDate()));

		TextView mDateName = (TextView) findViewById(R.id.date_name);
		mDateName.setText(DreamSpellUtil.GetName());
			
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

	public void removeDay() {
		removeDay(1);
	}

	public void addDay() {
		addDay(1);
	}
	
	private void addDay(int i) {
		Log.d(TAG, "addDay " + i);
		Calendar c = Calendar.getInstance();
		Date d = DreamSpellUtil.currentDate;
		c.setTime(d);
		c.add(Calendar.DAY_OF_YEAR,i);
		calc(c.getTime());
	}

	private void removeDay(int i) {
		Log.d(TAG, "removeDay " + i);
		Calendar c = Calendar.getInstance();
		Date d = DreamSpellUtil.currentDate;
		c.setTime(d);
		c.add(Calendar.DAY_OF_YEAR,-i);
		calc(c.getTime());
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	    	Log.d(TAG, "MyGestureDetector onFling!");
	        try {
	            //if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	            //{
	            //	Log.d(TAG, "MyGestureDetector we got a swipe max off path");
	            //    return false;
	            //}
	        	
	            // right to left swipe
	            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
	            {
	                //Toast.makeText(SelectFilterActivity.this, "Left Swipe", Toast.LENGTH_SHORT`enter code here`).show();
	            	Log.d(TAG, "MyGestureDetector Left Swipe");
	            	removeDay(SWIPE_DAYS);
	            }  
	            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
	            {
	                //Toast.makeText(SelectFilterActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
	            	Log.d(TAG, "MyGestureDetector Right Swipe");
	            	addDay(SWIPE_DAYS);
	            }
	            else if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) 
	            {
	                //Toast.makeText(SelectFilterActivity.this, "Left Swipe", Toast.LENGTH_SHORT`enter code here`).show();
	            	Log.d(TAG, "MyGestureDetector Up Swipe");
	            	removeDay(SWIPE_DAYS);
	            }  
	            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) 
	            {
	                //Toast.makeText(SelectFilterActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
	            	Log.d(TAG, "MyGestureDetector Down Swipe");
	            	addDay(SWIPE_DAYS);
	            }
	        } catch (Exception e) {
	            // nothing
	        }
	        return false;
	    }
	}
	
	public boolean onTouch(View v, MotionEvent event) {
			Log.d(TAG, "onTouch");
		return false;
	}
	
	
	@Override
		public boolean onTouchEvent(MotionEvent event)
	 	{
			//Log.d(TAG,"onTouchEvent! ");
			
			//GestureDetector gestureDetector = new GestureDetector(gestureListener);
			if ( gestureDetector.onTouchEvent(event) )
			{
				//Log.d(TAG,"onTouchEvent case1");
				return true;
			}
			else
			{
				//Log.d(TAG,"onTouchEvent case2");
				return false;
			}
	 	}
		
	// the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    
                    Log.d(TAG,String.format("onDateSet: mYear %s, mMonth %s, mDay %s",mYear,mMonth,mDay));
                    
                    calc(new GregorianCalendar(mYear,mMonth,mDay).getTime());
                }
            };
            
            @Override
            protected Dialog onCreateDialog(int id) {
            	
            	Log.d(TAG,String.format("onCreateDialog: mYear %s, mMonth %s, mDay %s",mYear,mMonth,mDay));
            	
            	 final Calendar c = Calendar.getInstance();
                 c.setTime(DreamSpellUtil.getCurrentDate());
                 mYear = c.get(Calendar.YEAR);
                 mMonth = c.get(Calendar.MONTH);
                 mDay = c.get(Calendar.DAY_OF_MONTH);
            	
                switch (id) {
                case DATE_DIALOG_ID:
                    datePickerDialog = new DatePickerDialog(this,
                                mDateSetListener,
                                mYear, mMonth, mDay);
                    
                    return datePickerDialog;
                }
                return null;
            }
            
   
}

