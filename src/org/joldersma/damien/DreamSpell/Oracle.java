package org.joldersma.damien.DreamSpell;

import java.text.DateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Oracle extends Activity {

	public static final String TAG = "DreamSpell";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG,"** ON CREATE **");
		setContentView(R.layout.oracle);
		
		TextView mDateTitle = (TextView) findViewById(R.id.date_title);
		mDateTitle.setText(DateFormat.getDateInstance().format(DreamSpellUtil.getCurrentDate()));

		TextView mDateName = (TextView) findViewById(R.id.date_name);
		mDateName.setText(DreamSpellUtil.GetName());
	}
}
