package org.joldersma.damien.DreamSpell;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class Friends extends ListActivity {

	public static final String TAG = "Friends";
	
	private ListView mFriendList;
	FriendListCursorAdapter friendListCursorAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        // Obtain handles to UI objects
        //mFriendList = (ListView) findViewById(R.id.);
        
        // Populate the contact list
        populateContactList();
    }

	/**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void populateContactList() {
    	 Log.d(TAG,"populateContactList begin");
        // Build adapter with contact entries
    	// Cursor cursor = getContacts();
    	
    	/*
    	Cursor cursor = getAllContactData(0);
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME,
                Event.START_DATE,
                ContactsContract.Contacts.PHOTO_ID
        };
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.friend_view, cursor,
        //        fields, new int[] {R.id.friendViewText, R.id.friendViewBirthDay});
        friendListCursorAdapter = new FriendListCursorAdapter(this, R.layout.friend_view, cursor,
                fields, new int[] {R.id.friendViewText, R.id.friendViewBirthDay, R.id.friendViewImage});
        //mFriendList.setAdapter(adapter);
        setListAdapter(friendListCursorAdapter);
        */
  
	  	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
    	Map<String, String> group;
    	group = new HashMap<String, String>();
        group.put("name", "damien");
        group.put("birthday", "022277");
        group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
    	groupData.add(group);
    	
    	group = new HashMap<String, String>();
        group.put("name", "damien1");
        group.put("birthday", "022278");
        group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
        groupData.add(group);
    	 
    	group = new HashMap<String, String>();
        group.put("name", "damien2");
        group.put("birthday", "022279");
        group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
        groupData.add(group);
    	 
//    	SimpleAdapter simpleAdapter = new SimpleAdapter(this,groupData,R.layout.friend_view, new String[] { "name","birthday"},
//                new int[]{ R.id.friendViewText, R.id.friendViewBirthDay });
//    	setListAdapter(simpleAdapter);
    	
    	FriendListFacebookAdapter friendListCursorAdapter = new FriendListFacebookAdapter(this, groupData, R.layout.friend_view,
        	new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewBirthDay, R.id.friendViewImage});
    	setListAdapter(friendListCursorAdapter);
        
        //Cursor c = getAllContactData(0);
        //getColumnData(c);
        /*
       if (cursor.moveToFirst()) 
       {
    	   Log.d(TAG,"Yes second pass through cursor");
    	   do {
    		   long contactId = cursor.getLong(cursor.getColumnIndex(Data._ID));
    		   String name = cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
    		   //Log.d(TAG,"Getting contact data for " + contactId + " " + name);
    		   Cursor c = getAllContactData(contactId);
    		   getColumnData(c);
    	   } while (cursor.moveToNext());
       }
       else
       {
    	   Log.d(TAG,"No second pass through cursor");
       }
       */
    }

    private Cursor getAllContactData(long contactId)
    {
//    	Cursor c = getContentResolver().query(Data.CONTENT_URI,
//  	          new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
//  	          Data.CONTACT_ID + "=?" + " AND "
//  	                  + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
//  	          new String[] {String.valueOf(contactId)}, null);
//    	return c;
    	Cursor c = getContentResolver().query(Data.CONTENT_URI,
    	          new String[] {Data._ID, Event.START_DATE, Event.TYPE, Event.LABEL, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID},
    	          Data.MIMETYPE + "='" + Event.CONTENT_ITEM_TYPE + "'",
    	          null, null);
    	          //Data.CONTACT_ID + "=?" + " AND "
    	          //        + Data.MIMETYPE + "='" + Event.CONTENT_ITEM_TYPE + "'",
    	          //new String[] {String.valueOf(contactId)}, null);
      	return c;
    }
    
    private void getColumnData(Cursor cur){ 
        if (cur.moveToFirst()) {

            String birthDay; 
            String label; 
            int birthdayColumn = cur.getColumnIndex(Event.START_DATE); 
            int labelColumn = cur.getColumnIndex(Event.LABEL);
           
        
            do {
                // Get the field values
            	birthDay = cur.getString(birthdayColumn);
            	label = cur.getString(labelColumn);
               
                // Do something with the values. 
                Log.d(TAG, "birthday: " + birthDay + " label: " + label);
                
                
                
            } while (cur.moveToNext());

        }
    }
    
    public static final String KEY_DATE = "date";
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position,  id);
        Log.d(TAG,"onListItemClick: click, id is " + id);
        
        Date birthday = friendListCursorAdapter.getBirthdays().get(String.valueOf(id));
        Log.d(TAG,"onListItemClick: putting birthday into extra - " + birthday);
        
        Intent i = new Intent(this, DreamSpell.class);
        i.putExtra(KEY_DATE, birthday);
        startActivity(i);
    }
    
}
