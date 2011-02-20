package org.joldersma.damien.DreamSpell;

import java.text.DateFormat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView.ScaleType;
import android.widget.TableRow.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Tzolkin extends Activity {

	public static final String TAG = "DreamSpell";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG,"** TZOLKIN ON CREATE **");
		setContentView(R.layout.tzolkin);
		
		/* Find Tablelayout defined in main.xml */
		TableLayout tl = (TableLayout)findViewById(R.id.tzolkin_table);
		int kin = 1;
		for (int seal = 0; seal < 20; seal++)
		{
			/* Create a new row to be added. */
			TableRow tr = new TableRow(this);
			
			
			
			tr.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.FILL_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT));
			
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(AndroidUtil.getGlyphResource(seal+1));
			imageView.setScaleType(ScaleType.FIT_XY);
		
			imageView.setLayoutParams(new LayoutParams(40,40));
//			imageView.setLayoutParams(new LayoutParams(
//								LayoutParams.FILL_PARENT,
//								LayoutParams.WRAP_CONTENT));
			
			imageView.setPadding(3, 1, 3, 1);
			/* Add TextView to row. */
			tr.addView(imageView);
			
			for (int column = 0; column < 13; column++) {
				Log.d(TAG, "Going to add kin text view: ");
				
				int kinIndex = (seal + ((column) * 20));
				int tone = kinIndex % 13 + 1;
				
				
				boolean today = DreamSpellUtil.getKin() == kinIndex + 1;
				
				/* Create a TextView to be the row-content. */
				TextView textView = new TextView(this);
				textView.setText(String.format("%s",tone));
				textView.setGravity(Gravity.CENTER);
				textView.setLayoutParams(new LayoutParams(21,40));
				textView.setTextColor(today ? Color.RED : Color.BLACK);
				//textView.setPadding(1, 1, 1, 1);
				/* Add TextView to row. */
				tr.addView(textView);
			
			}
		
		
			Log.d(TAG, "Going to add new row to table layout");
			/* Add row to TableLayout. */
			tl.addView(tr,new TableLayout.LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
		}
		
	}
}
