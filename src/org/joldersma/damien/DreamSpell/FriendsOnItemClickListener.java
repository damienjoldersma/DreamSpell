package org.joldersma.damien.DreamSpell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FriendsOnItemClickListener implements OnItemClickListener {

	public static final String TAG = "DreamSpell";
	public static final String KEY_DATE = "date";
	private Activity friends;
	
	public FriendsOnItemClickListener(Activity f)
	{
		this.friends = f;
	}
	
	public void onItemClick(AdapterView<?> l, View v,
			int position, long id) {
		// TODO Auto-generated method stub
		
	//}    
	
	//@Override
	//protected void onTODOListItemClick(ListView l, View v, int position, long id) {
			//super.onItemClick(l, v, position,	id);
			Log.d(TAG,"onItemClick: click, id is " + id);
			
			Object o = l.getItemAtPosition(position);
			Log.d(TAG,"onItemClick: object o is " + o);
			
			HashMap<String, String> friend = (HashMap<String, String>) l.getItemAtPosition(position);
			Log.d(TAG,"onItemClick: friend is " + friend);
			
			String friendId = friend.get("facebookId");
			String name = friend.get("name");
			String bday = friend.get("birthday");
			String picture = friend.get("picture");
			
			Log.d(TAG,String.format("onItemClick: friend values are friendId=%s, name=%s, bday=%s, picture=%s",friendId,name,bday,picture));
			
//			friend = new HashMap<String, String>();
//	friend.put("facebookId", name);
//		friend.put("name", name);
//		friend.put("birthday", birthday);
//		friend.put("picture", picture);
			
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
			
			Log.d(TAG,"onItemClick: putting birthday into extra - " + birthday);
			
			Intent i = new Intent(friends, DreamSpell.class);
			i.putExtra(KEY_DATE, birthday);
			friends.startActivity(i);
	}
}
