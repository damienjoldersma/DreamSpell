package org.joldersma.damien.DreamSpell;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FriendListCursorAdapter extends SimpleCursorAdapter implements Filterable {

	public static final String TAG = "FriendListCursorAdapter";
	
    private Context context;

    private int layout;

    public FriendListCursorAdapter (Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        Cursor c = getCursor();

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

        int nameCol = c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);

        String name = c.getString(nameCol);

        /**
         * Next set the name of the entry.
         */
        TextView name_text = (TextView) v.findViewById(R.id.friendViewText);
        if (name_text != null) {
            name_text.setText(name);
        }

        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

        int nameCol = c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);

        String name = c.getString(nameCol);

        int bdayCol = c.getColumnIndex(Event.START_DATE);

        String bday = c.getString(bdayCol);

        Log.d(TAG,"Got bday: " + bday);
        
        /**
         * Next set the Tone of the entry.
         */
        ImageView tone = (ImageView) v.findViewById(R.id.friendViewTone);
        if (tone != null) {
        	try
        	{
        		//1988-01-21
        		SimpleDateFormat df1 = new SimpleDateFormat( "yyyy-MM-dd" );
        		DreamSpellUtil.Calc(df1.parse(bday));
        		tone.setImageResource(AndroidUtil.getToneResource(DreamSpellUtil.getTone()));
        	}
        	catch (Exception e)
        	{
        		Log.e(TAG, "Error setting tone",e);
        		tone.setImageResource(R.drawable.tone1);
        	}
        }
        
        /**
         * Next set the Glyph of the entry.
         */
        ImageView glpyh = (ImageView) v.findViewById(R.id.friendViewGlyph);
        if (glpyh != null) {
        	try
        	{
        		//1988-01-21
        		SimpleDateFormat df1 = new SimpleDateFormat( "yyyy-MM-dd" );
        		DreamSpellUtil.Calc(df1.parse(bday));
        		glpyh.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getSeal()));
        	}
        	catch (Exception e)
        	{
        		Log.e(TAG, "Error setting glyph",e);
        		glpyh.setImageResource(R.drawable.glyph1);
        	}
        }
        
        /**
         * Next set the name of the entry.
         */
        TextView name_text = (TextView) v.findViewById(R.id.friendViewText);
        if (name_text != null) {
            name_text.setText(name);
        }
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider() != null) { return getFilterQueryProvider().runQuery(constraint); }

        StringBuilder buffer = null;
        String[] args = null;
        if (constraint != null) {
            buffer = new StringBuilder();
            buffer.append("UPPER(");
            buffer.append(ContactsContract.Data.DISPLAY_NAME);
            buffer.append(") GLOB ?");
            args = new String[] { constraint.toString().toUpperCase() + "*" };
        }

        return context.getContentResolver().query(Data.CONTENT_URI, null,
                buffer == null ? null : buffer.toString(), args, ContactsContract.Data.DISPLAY_NAME + " ASC");
    }
}
