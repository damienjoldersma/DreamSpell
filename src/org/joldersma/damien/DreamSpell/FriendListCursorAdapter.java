package org.joldersma.damien.DreamSpell;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.Contacts;
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
	
	public Hashtable<String, Date> birthdays;

	private Context context;

    private int layout;

    public FriendListCursorAdapter (Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.layout = layout;
        birthdays = new Hashtable<String, Date>();
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

    	//if (true) return;

    	int contactIdCol= c.getColumnIndex(Data._ID);
    	String contactId = c.getString(contactIdCol);
  	
        int nameCol = c.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
        String name = c.getString(nameCol);

        int bdayCol = c.getColumnIndex(Event.START_DATE);
        String bday = c.getString(bdayCol);

        int photoCol = c.getColumnIndex(ContactsContract.Contacts.PHOTO_ID);
        int photo = c.getInt(photoCol);

        Log.d(TAG,String.format("bindView record: contactId=%s, name=%s, bday=%s, photo=%d",contactId,name,bday,photo));

        //if () return;
    
        
        //1988-01-21
        try
    	{
	        SimpleDateFormat df1 = new SimpleDateFormat( "yyyy-MM-dd" );
	        Date birthday = df1.parse(bday);
	        birthdays.put(contactId, birthday);
	        
	        DreamSpellUtil.Calc(birthday);
    	}
    	catch (Exception e)
    	{
    		Log.e(TAG, "Error setting tone",e);
    		
    	}
        /**
         * Next set the Tone of the entry.
         */
        ImageView tone = (ImageView) v.findViewById(R.id.friendViewTone);
        if (tone != null) {
    		tone.setImageResource(AndroidUtil.getToneResource(DreamSpellUtil.getTone()));        	
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
        
        /**
         * Next set the birthday of the entry.
         */
        TextView bday_text = (TextView) v.findViewById(R.id.friendViewBirthDay);
        if (bday_text != null) {
        	try {
				SimpleDateFormat df1 = new SimpleDateFormat( "yyyy-MM-dd" );
				Date d = df1.parse(bday);
				SimpleDateFormat df2 = new SimpleDateFormat( "dd/MM/yyyy" );
				bday_text.setText( df2.format(d) );
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        
        /**
         * Next set the photo of the entry.
         */
        ImageView photo_view = (ImageView) v.findViewById(R.id.friendViewImage);
        if (photo_view != null) {
//        	ContentResolver contentResolver = null;
//        	Uri uri = null;
        	//Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, photo); 
        	//InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri) ;
        	
        	//ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE;	
        	
//        	Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
//                    .parseLong(contactId));
//            Uri uri = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//        	photo_view.setImageURI(uri);
//        	Log.d(TAG,String.format("Set photo_view image uri=%s",uri));
        	//photo_view.setImageResource(photo);
        	
        	//photo_view.setImageResource(R.drawable.glyph2);
        	
        	Uri u = getPhotoUri(Long.parseLong(contactId));
        	if (u != null) {
        		Log.d(TAG,"Set custom photo: " + u);
        		photo_view.setImageURI(u);
        	} else {
        		photo_view.setImageResource(R.drawable.glyph2);
        		Log.d(TAG,"Set default photo, uri was " + u);
        	}
        	
//        	int id = c.getColumnIndex(People._ID);
//		    Uri uri = ContentUris.withAppendedId(People.CONTENT_URI, c.getLong(id));
//		    Log.d(TAG,"getting photo with uri: " + uri);
//		    Bitmap bitmap = People.loadContactPhoto(context, uri, R.drawable.icon, null);
//		    photo_view.setImageBitmap(bitmap);
        	
//        	   Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
//    		   InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri);
//    		   Bitmap bitmap = new Bitmap(input);
//    		   
       // photo_view.setImageBitmap(loadContactPhoto(context.getContentResolver(), Long.parseLong(contactId)));
        		
//        	String id = "damien.joldersma";
//        	String url = String.format("http://graph.facebook.com/%s/picture",id);
//        	Uri uri = 
//        	photo_view.setImage;
        	
//        	int id = c.getColumnIndex(People._ID);
//            Uri uri = ContentUris.withAppendedId(People.CONTENT_URI, c.getLong(id));
//         
//            Bitmap bitmap = People.loadContactPhoto(context, uri, R.drawable.icon, null);
//         
//            photo_view.setImageBitmap(bitmap);

            
//            Uri peopleURI = ContentUris.withAppendedId(People.CONTENT_URI, new Long(contactId));
//            Bitmap contactPicture = People.loadContactPhoto(context,peopleURI, R.drawable.icon, null); 
//            photo_view.setImageBitmap(contactPicture);

//        	if ( name.equals("Damien Joldersma") || name.equals("Andy Largent") )
//        	{
//        		Log.d(TAG,"**SETTING DAMIEN**");
//        		photo_view.setImageBitmap(getPhoto(context.getContentResolver(), Long.parseLong(contactId)));
//        	}
//        	else
//        	{
//        		photo_view.setImageResource(R.drawable.icon);
//        	}
        	//photo_view.setImageBitmap(getPhoto(context.getContentResolver(), Long.parseLong(contactId)));
        	
        }
        else
        {
        	
        	Log.d(TAG,"Did not set photo: " + photo);
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
    
    public Hashtable<String, Date> getBirthdays() {
		return birthdays;
	}
    
    /**
     * @return the photo URI
     */
    public Uri getPhotoUri(long id) {
        try {
            Cursor cur = this.context.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
                            + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                	Log.d(TAG,"getPhotoUri: no photo");
                    return null; // no photo
                }
            } else {
            	Log.d(TAG,"getPhotoUri: error in cursor process");
                return null; // error in cursor process
            }
        } catch (Exception e) {
        	Log.e(TAG,"getPhotoUri: error " + e.toString(),e);
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        Uri uri = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Log.d(TAG,"getPhotoUri: returning " + uri);
        return uri;
    }
    
    public static Bitmap loadContactPhoto(ContentResolver cr, long id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        //if (input == null) {
        //     return getBitmapFromURL("http://thinkandroid.wordpress.com");
        // }
        
        if ( input == null )
        {
        	Log.d(TAG,"photo input is null");
        }
        else
        {
        	Log.d(TAG,"photo input is not null");
        }
        return BitmapFactory.decodeStream(input);
    }
    
    public Bitmap getPhoto(ContentResolver contentResolver, Long contactId) {
        Uri contactPhotoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);

        // contactPhotoUri --> content://com.android.contacts/contacts/1557
        Log.d(TAG, String.format("getPhoto: contactPhotoUri=%s, contactId=%s",contactPhotoUri,contactId));
        InputStream photoDataStream = Contacts.openContactPhotoInputStream(contentResolver,contactPhotoUri); // <-- always null
        Log.d(TAG, String.format("getPhoto: photoDataStream=%s", (photoDataStream == null ? "null":"*not* null")));
        Bitmap photo = BitmapFactory.decodeStream(photoDataStream);
        return photo;
    }

}
