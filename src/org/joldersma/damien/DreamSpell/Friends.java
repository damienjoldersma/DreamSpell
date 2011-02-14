package org.joldersma.damien.DreamSpell;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joldersma.damien.DreamSpell.SessionEvents.AuthListener;
import org.joldersma.damien.DreamSpell.SessionEvents.LogoutListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;

public class Friends extends Activity implements OnScrollListener, OnItemClickListener {

	public static final String TAG = "DreamSpell";
	
	public static final String APP_ID = "32395165793";
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
	boolean loggedIn = false;
	
	private DataHelper dh;

	FriendListCursorAdapter friendListCursorAdapter;
	private ListView listView1, listView2, listView3, listView4, listView5, listView6, listView7;
	
	//private LoginButton mLoginButton;
	
	private List<Map<String, String>> friendsData;
	public String friendsDataResponse;
	
	//Aleph0 adapter = new Aleph0();
	FriendListFacebookAdapter adapter = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
			Log.v(TAG, "Activity State: onCreate()");
			super.onCreate(savedInstanceState);
			setContentView(R.layout.friends);
			
			
			mFacebook = new Facebook(APP_ID);
		 	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		 	
		 	try
		 	{
		 		Log.d(TAG,"Going to restore facebook session");
		 		SessionStore.restore(mFacebook, this);
		 		Log.d(TAG,"Restored facebook session");
		 		
		 	}
		 	catch (Exception e)
		 	{
		 		Log.e(TAG,"Error trying to restore session",e);
		 	}
		 	
		 	if ( mFacebook.isSessionValid() )
		 	{
		 		loggedIn = true;
		 		Log.d(TAG,"Valid facebook session");
		 	}
		 	else
		 		Log.d(TAG,"No valid facebook session");
		 	
			SessionEvents.addAuthListener(new SampleAuthListener());
			SessionEvents.addLogoutListener(new SampleLogoutListener());
			
//				mLoginButton = (LoginButton) findViewById(R.id.login);
//				mLoginButton.init(this, mFacebook);
			
			mActivity = this;
	        mFb = mFacebook;
	        mPermissions = new String[] {};
	        mHandler = new Handler();
	        SessionEvents.addAuthListener(mSessionListener);
	        SessionEvents.addLogoutListener(mSessionListener);
			
			Log.d(TAG,"Goign to check SessionStore.restore for friendsDataResponse");
			//friendsDataResponse = SessionStore.restore("friendsDataResponse", this);
			if ( friendsDataResponse != null )
			{
				Log.d(TAG,"*** Got friendsDataResponse, going to processResponseString!");
				processResponseString();
			}
			else
				Log.d(TAG,"friendsDataResponse is null");
				
			
			
			// Obtain handles to UI objects
			//mFriendList = (ListView) findViewById(R.id.fr);
			
			
//				mLoginButton.setVisibility(mFacebook.isSessionValid() ?
//								View.INVISIBLE :
//								View.VISIBLE);
			
			//friendsData = getFriendsData(savedInstanceState);
			
			// Get the instance of the object that was stored
			// if one exists
//			if (getLastNonConfigurationInstance() != null)
//			{
//				Log.d(TAG,"WOW GOING TO GET SAVED FRIENDSDATA!");
//				friendsData = (List<Map<String, String>>)getLastNonConfigurationInstance();
//			}
//			else
//				Log.d(TAG,"No LastNonConfigurationInstance to check for friendsData");
//			
//			if ( savedInstanceState != null )
//			{
//				Log.d(TAG,"Ok have a savedInstanceState");
//				for (String key : savedInstanceState.keySet()) {
//					Log.d(TAG,"key=%s");
//				}
//			}
//		 	else
//		 		Log.d(TAG,"No savedInstanceState to check for friendsData");
			
			//setListAdapter(adapter); 
		    //getListView().setOnScrollListener(this); // TODO setOnScrollListners on listviews
			
			this.dh = new DataHelper(this);
			
			friendsData = this.dh.selectAll();
			
			//if ( mFacebook.isSessionValid() )
			//{
				// Populate the contact list
				Log.d(TAG,"onCreate going to populate");
				//populateContactList();
			//}
			setupTabs();
					 
	}

	public void setupTabs()
	{
		TabHost myTabHost = (TabHost)this.findViewById(R.id.tabhost);
		myTabHost.setup();
		
		listView2 = new ListView(Friends.this);
		listView3 = new ListView(Friends.this);
		listView4 = new ListView(Friends.this);
		listView5 = new ListView(Friends.this);
		listView6 = new ListView(Friends.this);
		listView7 = new ListView(Friends.this);
				
		TabSpec ts1 = myTabHost.newTabSpec("TAB_TAG_2");
		ts1.setIndicator("Seal");
		ts1.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				DreamSpellUtil.Calc(DreamSpell.getCurrentDate());
				List<Map<String,String>> data = dh.select("seal",DreamSpellUtil.getSeal());
				FriendListFacebookAdapter a = new FriendListFacebookAdapter(Friends.this,Friends.this, data, R.layout.friend_view,
						new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
				listView2.setAdapter(a);
				listView2.setItemsCanFocus(true);
				listView2.setOnItemClickListener(new FriendsOnItemClickListener(Friends.this));
				listView2.setOnScrollListener(new FriendsOnScrollListener(a)); 
				
				return listView2;
			}
		});
		myTabHost.addTab(ts1);
		
		TabSpec ts2 = myTabHost.newTabSpec("TAB_TAG_3");
		ts2.setIndicator("Tone");
		ts2.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				DreamSpellUtil.Calc(DreamSpell.getCurrentDate());
				List<Map<String,String>> data = dh.select("tone",DreamSpellUtil.getTone());
				FriendListFacebookAdapter a = new FriendListFacebookAdapter(Friends.this,Friends.this, data, R.layout.friend_view,
						new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
				listView3.setAdapter(a);
				listView3.setItemsCanFocus(true);
				listView3.setOnItemClickListener(new FriendsOnItemClickListener(Friends.this));
				listView3.setOnScrollListener(new FriendsOnScrollListener(a)); 
				return listView3;
			}
		});
		myTabHost.addTab(ts2);
		
		TabSpec ts3 = myTabHost.newTabSpec("TAB_TAG_3");
		ts3.setIndicator("Analog");
		ts3.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				DreamSpellUtil.Calc(DreamSpell.getCurrentDate());
				List<Map<String,String>> data = dh.select("seal",DreamSpellUtil.getAnalog());
				FriendListFacebookAdapter a = new FriendListFacebookAdapter(Friends.this,Friends.this, data, R.layout.friend_view,
						new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
				listView4.setAdapter(a);
				listView4.setItemsCanFocus(true);
				listView4.setOnItemClickListener(new FriendsOnItemClickListener(Friends.this));
				listView4.setOnScrollListener(new FriendsOnScrollListener(a)); 
				return listView4;
			}
		});
		myTabHost.addTab(ts3);
		
		TabSpec ts4 = myTabHost.newTabSpec("TAB_TAG_4");
		ts4.setIndicator("Occult");
		ts4.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				DreamSpellUtil.Calc(DreamSpell.getCurrentDate());
				List<Map<String,String>> data = dh.select("seal",DreamSpellUtil.getOccult());
				FriendListFacebookAdapter a = new FriendListFacebookAdapter(Friends.this,Friends.this, data, R.layout.friend_view,
						new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
				listView5.setAdapter(a);
				listView5.setItemsCanFocus(true);
				listView5.setOnItemClickListener(new FriendsOnItemClickListener(Friends.this));
				listView5.setOnScrollListener(new FriendsOnScrollListener(a)); 
				return listView5;
			}
		});
		myTabHost.addTab(ts4);
		
		TabSpec ts5 = myTabHost.newTabSpec("TAB_TAG_5");
		ts5.setIndicator("Antipode");
		ts5.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				DreamSpellUtil.Calc(DreamSpell.getCurrentDate());
				List<Map<String,String>> data = dh.select("seal",DreamSpellUtil.getAntipode());
				FriendListFacebookAdapter a = new FriendListFacebookAdapter(Friends.this,Friends.this, data, R.layout.friend_view,
						new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
				listView6.setAdapter(a);
				listView6.setItemsCanFocus(true);
				listView6.setOnItemClickListener(new FriendsOnItemClickListener(Friends.this));
				listView6.setOnScrollListener(new FriendsOnScrollListener(a)); 
				return listView6;
			}
		});
		myTabHost.addTab(ts5);
		
		TabSpec ts6 = myTabHost.newTabSpec("TAB_TAG_6");
		ts6.setIndicator("Guide");
		
		ts6.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				DreamSpellUtil.Calc(DreamSpell.getCurrentDate());
				List<Map<String,String>> data = dh.select("seal",DreamSpellUtil.getGuide());
				FriendListFacebookAdapter a = new FriendListFacebookAdapter(Friends.this,Friends.this, data, R.layout.friend_view,
						new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
				listView7.setAdapter(a);
				listView2.setItemsCanFocus(true);
				listView7.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						AlertDialog.Builder adb=new AlertDialog.Builder(Friends.this);
						adb.setTitle("LVSelectedItemExample");
						adb.setMessage("Selected Item is = "+a.getItemAtPosition(position));
						adb.setPositiveButton("Ok", null);
						adb.show();
					}
				 });
				listView7.setOnScrollListener(new FriendsOnScrollListener(a)); 
				return listView7;
			}
		});
		myTabHost.addTab(ts6);
		
		myTabHost.getTabWidget().getChildAt(0).getLayoutParams().width = 60;
		myTabHost.getTabWidget().getChildAt(1).getLayoutParams().width = 60;
		myTabHost.getTabWidget().getChildAt(2).getLayoutParams().width = 60;
		myTabHost.getTabWidget().getChildAt(3).getLayoutParams().width = 60;
		myTabHost.getTabWidget().getChildAt(4).getLayoutParams().width = 80;
		myTabHost.getTabWidget().getChildAt(5).getLayoutParams().width = 60;
		
		myTabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 30;
		myTabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 30;
		myTabHost.getTabWidget().getChildAt(2).getLayoutParams().height = 30;
		myTabHost.getTabWidget().getChildAt(3).getLayoutParams().height = 30;
		myTabHost.getTabWidget().getChildAt(4).getLayoutParams().height = 30;
		myTabHost.getTabWidget().getChildAt(5).getLayoutParams().height = 30;
	}
	
	private static final int LOGIN_ID = Menu.FIRST;
	private static final int SYNC_ID = Menu.FIRST+1;
	private static final int ALL_FRIENDS_ID = Menu.FIRST+2;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
//		menu.add(0, LOGIN_ID,0, loggedIn ? R.string.logout : R.string.login);
//		menu.add(0, SYNC_ID,0, R.string.sync);
//		menu.add(0, ALL_FRIENDS_ID,0, R.string.all_friends);
		updateMenu(menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		Log.d(TAG,"OnPrepareOptionsMenu fired!");
		menu.clear();
		
		updateMenu(menu);
		
		return super.onPrepareOptionsMenu(menu);
	}

	public void updateMenu(Menu menu)
	{
		if ( loggedIn )
		{
			menu.add(0, LOGIN_ID,0, R.string.logout);
			menu.add(0, SYNC_ID,0, R.string.sync);
		}
		else
		{
			menu.add(0, LOGIN_ID,0, R.string.login);
		}
		
		menu.add(0, ALL_FRIENDS_ID,0, R.string.all_friends);
	}
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
    	Log.d(TAG,"Menu item selected" + item.getItemId());
        switch(item.getItemId()) {
        case LOGIN_ID:
            doClick();
            return true;
        case SYNC_ID:
        	doSync();
        	return true;
	    case ALL_FRIENDS_ID:
	    	showAllFriends();
	    	return true;
	    }
        return super.onMenuItemSelected(featureId, item);
	}
    
    public void doClick() {
		if (mFb.isSessionValid()) {
			Log.d(TAG,"buttonOnClick listener - session is valid going to logout");
		    SessionEvents.onLogoutBegin();
		    AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
		    asyncRunner.logout(getContext(), new LogoutRequestListener());
		    
		    // Update menu
		    
		} else {
			Log.d(TAG,"buttonOnClick listener - session is not valid going to login,");
		    mFb.authorize(mActivity, mPermissions, new LoginDialogListener());
		}
	}

    public void showAllFriends()
    {
    	setContentView(R.layout.all_friends);
    	
    	ListView allFriendsList = (ListView)this.findViewById(R.id.all_friends_list);
    	
    	FriendListFacebookAdapter a = new FriendListFacebookAdapter(Friends.this,Friends.this, friendsData, R.layout.friend_view,
				new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
    	allFriendsList.setAdapter(a);

    	allFriendsList.setOnScrollListener(new FriendsOnScrollListener(a)); 
    	
    }

	private Facebook mFb;
	private Handler mHandler;
	private SessionListener mSessionListener = new SessionListener();
	private String[] mPermissions;
	private Activity mActivity;
	
private final class LoginDialogListener implements DialogListener {
    	
    	public LoginDialogListener()
    	{
    		Log.d(TAG,"LoginDialogListener constructor ");
    		
    	}
    	
        public void onComplete(Bundle values) {
        	Log.d(TAG,"LoginDialogListener onComplete");
            SessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) {
        	Log.d(TAG,"LoginDialogListener onFacebookError=" + error.toString());
        	error.printStackTrace();
            SessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
        	Log.d(TAG,"LoginDialogListener onError=" + error.toString());
        	error.printStackTrace();
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
        	Log.d(TAG,"LoginDialogListener onCancel");
            SessionEvents.onLoginError("Action Canceled");
        }
    }
    
    private class LogoutRequestListener extends BaseRequestListener {
        public void onComplete(String response) {
            // callback should be run in the original thread, 
            // not the background thread
            mHandler.post(new Runnable() {
                public void run() {
                	Log.d(TAG,"OnLogoutFinish");
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }
    
    private class SessionListener implements AuthListener, LogoutListener {
        
		public void onAuthSucceed() {
        	Log.d(TAG, "SessionListener onAuthSucceed!");
            //setImageResource(R.drawable.logout_button);
           SessionStore.save(mFb, getContext()	);
           loggedIn = true;
        }

        public void onAuthFail(String error) {
        	Log.d(TAG, "SessionListener onAuthFail=" + error);
        	loggedIn = false;
        }
        
        public void onLogoutBegin() {
        }
        
        public void onLogoutFinish() {
        	Log.d(TAG,"My SessionListener onLogoutFinish");
            SessionStore.clear(getContext());
            //setImageResource(R.drawable.login_button);
            AlertDialog.Builder adb=new AlertDialog.Builder(Friends.this);
			adb.setTitle("Facebook Logout");
			adb.setMessage("You've been logged out!");
			adb.setPositiveButton("Ok", null);
			adb.show();
            
        	loggedIn = false;
        }
    }
	
	// Store the instance of an object
	@Override
	public Object onRetainNonConfigurationInstance() 
	{
		Log.d(TAG,"WOW, onRetainNonConfigurationInstance maybe going to save friendsData");
		if (friendsData != null) // Check that the object exists
				return(friendsData);
		Log.d(TAG,"If you are reading this, it's because friends data is null, friendsData=" + friendsData);
		return super.onRetainNonConfigurationInstance();
	}
	
	public Context getContext() {
		return this.getBaseContext();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		Log.d(TAG,"WOW, onSaveInstanceState maybe going to save friendsData");
		
		///SessionStore.save("friendsDataResponse", friendsDataResponse, this);

//		savedInstanceState.putBoolean("MyBoolean", true);
//		savedInstanceState.putDouble("myDouble", 1.9);
//		savedInstanceState.putInt("MyInt", 1);
//		savedInstanceState.putString("MyString", "Welcome back to Android");
		
//		if ( friendsData != null) {
//			for (Map<String,String> friend : friendsData) {
//				Log.d(TAG,"onSaveInstanceState adding friend=" + friend);
//			ArrayList<String> friendValues = new ArrayList<String>();
//			friendValues.add(friend.get("facebookId"));
//			friendValues.add(friend.get("name"));
//			friendValues.add(friend.get("birthday"));
//			friendValues.add(friend.get("picture"));
//			savedInstanceState.putStringArrayList(friend.get("facebookId"), friendValues);
//			}
//		}
		
		//savedInstanceState.putString("friendsDataResponse",friendsDataResponse);
		
		//super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		Log.d(TAG,"OnRestore!");
		
		//friendsDataResponse = savedInstanceState.getString("friendsDataResponse");
		
		Log.d(TAG,String.format("OnRestore got friendsDataResponse=%s",friendsDataResponse));
	}
	
	/**
		 * Populate the contact list based on account currently selected in the account spinner.
	 * @param friendsData 
		 */
		private void populateContactList() {
			 Log.d(TAG,"populateContactList begin");
			 
			 if ( friendsData == null )
			 {
				 Log.d(TAG,"Going to skip populate, friendsData is null");
				 return;
			 }
			 
			// mLoginButton.setVisibility(View.INVISIBLE);
			 
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
				//				fields, new int[] {R.id.friendViewText, R.id.friendViewBirthDay});
				friendListCursorAdapter = new FriendListCursorAdapter(this, R.layout.friend_view, cursor,
								fields, new int[] {R.id.friendViewText, R.id.friendViewBirthDay, R.id.friendViewImage});
				//mFriendList.setAdapter(adapter);
				setListAdapter(friendListCursorAdapter);
				*/
	
//			List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
//			Map<String, String> group;
//			group = new HashMap<String, String>();
//				group.put("name", "damien");
//				group.put("birthday", "022277");
//				group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
//			groupData.add(group);
//			
//			group = new HashMap<String, String>();
//				group.put("name", "damien1");
//				group.put("birthday", "022278");
//				group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
//				groupData.add(group);
//			 
//			group = new HashMap<String, String>();
//				group.put("name", "damien2");
//				group.put("birthday", "022279");
//				group.put("picture", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs338.snc4/41773_775949236_4548_q.jpg");
//				groupData.add(group);
			 
//			SimpleAdapter simpleAdapter = new SimpleAdapter(this,groupData,R.layout.friend_view, new String[] { "name","birthday"},
//								new int[]{ R.id.friendViewText, R.id.friendViewBirthDay });
//			setListAdapter(simpleAdapter);
			
			adapter = new FriendListFacebookAdapter(this,this, friendsData, R.layout.friend_view,
					new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
			//setListAdapter(adapter);
			
			
				
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
//			Cursor c = getContentResolver().query(Data.CONTENT_URI,
//							new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
//							Data.CONTACT_ID + "=?" + " AND "
//											+ Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
//							new String[] {String.valueOf(contactId)}, null);
//			return c;
			Cursor c = getContentResolver().query(Data.CONTENT_URI,
								new String[] {Data._ID, Event.START_DATE, Event.TYPE, Event.LABEL, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID},
								Data.MIMETYPE + "='" + Event.CONTENT_ITEM_TYPE + "'",
								null, null);
								//Data.CONTACT_ID + "=?" + " AND "
								//				+ Data.MIMETYPE + "='" + Event.CONTENT_ITEM_TYPE + "'",
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
		
		public void onItemClick(AdapterView<?> l, View v,
				int position, long id) {
			// TODO Auto-generated method stub
			
		//}    
		
		//@Override
		//protected void onTODOListItemClick(ListView l, View v, int position, long id) {
				//super.onListItemClick(l, v, position,	id);
				Log.d(TAG,"onListItemClick: click, id is " + id);
				
				Object o = l.getItemAtPosition(position);
				Log.d(TAG,"onListItemClick: object o is " + o);
				
				HashMap<String, String> friend = (HashMap<String, String>) l.getItemAtPosition(position);
				Log.d(TAG,"onListItemClick: friend is " + friend);
				
				String friendId = friend.get("facebookId");
				String name = friend.get("name");
				String bday = friend.get("birthday");
				String picture = friend.get("picture");
				
				Log.d(TAG,String.format("onListItemClick: friend values are friendId=%s, name=%s, bday=%s, picture=%s",friendId,name,bday,picture));
				
//				friend = new HashMap<String, String>();
//		friend.put("facebookId", name);
//			friend.put("name", name);
//			friend.put("birthday", birthday);
//			friend.put("picture", picture);
				
				Date birthday = new Date();//friendListCursorAdapter.getBirthdays().get(String.valueOf(id));
				
				try 
				{
					SimpleDateFormat df1 = new SimpleDateFormat( "MM/dd/yyyy" );
					birthday = df1.parse(bday);
				}
				catch (Exception e)
				{
					Log.w(TAG,"Couldn't parse birthday, " + bday);
					
				}
				
				Log.d(TAG,"onListItemClick: putting birthday into extra - " + birthday);
				
				Intent i = new Intent(this, DreamSpell.class);
				i.putExtra(KEY_DATE, birthday);
				startActivity(i);
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.d(TAG,String.format("onActivityResult begin requestCode=%s, resultCode=%s",requestCode,resultCode));
				mFacebook.authorizeCallback(requestCode, resultCode, data);
				Log.d(TAG,String.format("onActivityResult end requestCode=%s, resultCode=%s",requestCode,resultCode));
		}

		private void processResponseString() {
		try 
		{
			Log.d(TAG, "processResponseString going to save friendsDataResponse");
			SessionStore.save("friendsDataResponse",friendsDataResponse, this);
			
			String response = friendsDataResponse;
			
			if ( response == null )
			{
				Log.d(TAG, "friendsDataResponse is null, not processing " + friendsDataResponse);
				return;
			}
			// process the response here: executed in background thread
			Log.d(TAG, "Response: " + response.toString());
		
			
			Log.d(TAG, "***************  Begin DataBase Setup ***************");
			DataHelper dh = new DataHelper(this);
			dh.deleteAll();
//			this.dh.deleteAll();
//			this.dh.insert("Porky Pig");
//			this.dh.insert("Foghorn Leghorn");
//			this.dh.insert("Yosemite Sam");       

//			List<String> names = this.dh.selectAll();
//			StringBuilder sb = new StringBuilder();
//			
//			sb.append("Names in database:\n");
//			
//			   for (String name : names) {
//			
//			       sb.append(name + "\n");
//			
//			}
//						         
//			
//			Log.d("EXAMPLE", "names size - " + names.size());
			Log.d(TAG, "*************** Finish DataBase Setup **********");
			
			Log.d(TAG, "*************** BEGIN PARSE *******");
			friendsData = new ArrayList<Map<String, String>>();
			Map<String, String> friend;

			JSONObject json = Util.parseJson(response);

			JSONArray data = json.getJSONArray("data");
			for (int i = 0; i < data.length(); i++) 
			{
				Log.d(TAG,String.format("data %s",i));

				JSONObject d = data.getJSONObject(i);
				String name = d.getString("name");
				String birthday = null;
				try { birthday = d.getString("birthday"); } catch (Exception e) {} 
				String id = d.getString("id");
				String picture = d.getString("picture");
				
				friend = new HashMap<String, String>();
				friend.put("facebookId", id);
				friend.put("name", name);
				friend.put("birthday", birthday);
				friend.put("picture", picture);
				
				if ( birthday != null && birthday.length() == 10)
				{
					// Now find kin number
					friend.put("kin", DreamSpellUtil.getKinNumber(birthday));
					
					friendsData.add(friend); // TODO Remove here?
					Log.d(TAG,"## Going to insert friend");
					dh.insert(friend);
				}
				
				Log.d(TAG,String.format("data %s, name=%s, id=%s, birthday=%s, pic=%s",i,name,id,birthday,picture));
			}
						
			Collections.sort(friendsData, new MyComparator());
			
			Log.d(TAG, "*************** END PARSE *******");
			

			
			// then post the processed result back to the UI thread
			// if we do not do this, an runtime exception will be generated
			// e.g. "CalledFromWrongThreadException: Only the original
			// thread that created a view hierarchy can touch its views."
			Friends.this.runOnUiThread(new Runnable() {
					public void run() {
						//populateContactList();
						//setupTabs();
					}
			});
			
			//final String name = "FOO";// json.getString("data[0]name");

			// then post the processed result back to the UI thread
			// if we do not do this, an runtime exception will be generated
			// e.g. "CalledFromWrongThreadException: Only the original
			// thread that created a view hierarchy can touch its views."
//						Example.this.runOnUiThread(new Runnable() {
//								public void run() {
//										mText.setText("Hello there, " + name + "!");
//								}
//						});
			Log.d(TAG,"Going to dismiss dialog1");
			dialog.dismiss();
			
			} catch (JSONException e) {
					Log.w(TAG, "JSON Error in response: " + e.toString());
					Log.w(TAG,e);
					e.printStackTrace();
					
			} catch (FacebookError e) {
					Log.w(TAG, "Facebook Error: " + e.getMessage());
					e.printStackTrace();
			}
		}
		ProgressDialog dialog = null;
		private void doSync() {
			dialog = ProgressDialog.show(Friends.this, "", 
                    "Loading. Please wait...", true);
			
			Log.d(TAG,"DreamSpell now with new and improved me/friends?fields=id,name,picture,birthday!");
			Bundle params = new Bundle();
//						//String[] fields = { "facebookId","name","picture" };
			params.putString("fields", "id,name,picture,birthday");
//	mAsyncRunner.request("me/friends", params, new SampleFriendsListener());
			
			new Thread() {
				@Override public void run() {
					Log.d(TAG,"doing cyclone mAyncRunner request thread");
							Bundle params = new Bundle();
							params.putString("fields", "id,name,picture,birthday");
					mAsyncRunner.request("me/friends", params, new SampleFriendsListener());
					
				}
			}.start();
		}

		public class MyComparator implements Comparator 
		{
			 public int compare(Object o1, Object o2) {
						// Get the value of the properties
				Log.d(TAG,String.format("compare time o1=%s, o2=%s",o1,o2));
				
				Map<String,String> friend1 = (Map<String,String>)o1;
				Map<String,String> friend2 = (Map<String,String>)o2;
				return friend1.get("name").compareTo(friend2.get("name"));
				 
//				 	String friend1 = (String)o1;
//				 	String friend2 = (String)o2;
//				 	return friend1.compareTo(friend2);
		 	
		 	//HashMap<String, String> friend1 = (HashMap<String, String>)o1;
		 	//HashMap<String, String> friend2 = (HashMap<String, String>)o2;
			 // return compare(friend1.get("name"),friend2.get("name"));
			}
		}
		
		public class SampleFriendsListener extends BaseRequestListener {

			public void onComplete(final String response) {
				Log.d(TAG,"SampleFriendsListener onComplete, got and going to process response");
				friendsDataResponse = response;
				processResponseString();
			}

			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				Log.e(TAG,"FacebookError=" + e.toString());
				e.printStackTrace();
			}
		}
		
		public class SampleAuthListener implements AuthListener {

				public void onAuthSucceed() {
						Log.d(TAG,"You have logged in! ");
						
						//mLoginButton.setVisibility(View.INVISIBLE);
						
						doSync();
		
					// Populate the contact list
					Log.d(TAG,"onAuthSucceed valid, going to populate");
					//populateContactList();
					//setupTabs();

				}

				public void onAuthFail(String error) {
					Log.d(TAG,"Login Failed: " + error);
				}
		}

		public class SampleLogoutListener implements LogoutListener {
				public void onLogoutBegin() {
					Log.d(TAG,"Logging out...");
				}

				public void onLogoutFinish() {
					Log.d(TAG,"You have logged out! ");
				}
		}
		
		public class SampleDialogListener extends BaseDialogListener {

				public void onComplete(Bundle values) {
						final String postId = values.getString("post_id");
						if (postId != null) {
								Log.d(TAG, "Dialog Success! post_id=" + postId);
								
						} else {
								Log.d(TAG, "No wall post made");
						}
				}
		}
		
		 public void onScroll(AbsListView view,
			        int firstVisible, int visibleCount, int totalCount) {

			        boolean loadMore = /* maybe add a padding */
			            firstVisible + visibleCount >= totalCount;

			        if(loadMore && adapter != null ) {
			        	Log.d(TAG,"Increasing adapter count to 3");
			            adapter.count += 3; // visibleCount; // or any other amount
			            adapter.notifyDataSetChanged();
			        }
			    }

			    public void onScrollStateChanged(AbsListView v, int s) { }

				

//			    class Aleph0 extends BaseAdapter {
//			        int count =10; /* starting amount */
//			        public int getCount() { return count; }
//			        public Object getItem(int pos) { return pos; }
//			        public long getItemId(int pos) { return pos; }
//
//			        public View getView(int pos, View v, ViewGroup p) {
//			                TextView view = new TextView(Test.this);
//			                view.setText("entry " + pos);
//			                return view;
//			        }
//			        
//					public View getView(int position, View convertView,
//							ViewGroup parent) {
//						// TODO Auto-generated method stub
//						return null;
//					}
//			    }
}
