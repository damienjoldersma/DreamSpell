package org.joldersma.damien.DreamSpell;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.ListView;
import android.widget.TabHost.TabSpec;

public class FriendGroups extends Activity {
	
	public static final String TAG = "DreamSpell";
	private DataHelper dh;
	private FriendListFacebookAdapter adapter = null;
	private List<Map<String, String>> friendsData;	
	private ListView listView1, listView2, listView3, listView4, listView5, listView6, listView7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.friend_groups);
		
		this.dh = new DataHelper(this);
		friendsData = this.dh.selectAll();
		
		TabHost myTabHost = (TabHost)this.findViewById(R.id.tabhost);
		myTabHost.setup();
		
		listView1 = new ListView(FriendGroups.this);
		listView2 = new ListView(FriendGroups.this);
		listView3 = new ListView(FriendGroups.this);
		listView4 = new ListView(FriendGroups.this);
		listView5 = new ListView(FriendGroups.this);
		listView6 = new ListView(FriendGroups.this);
		listView7 = new ListView(FriendGroups.this);
		
		TabSpec ts = myTabHost.newTabSpec("TAB_TAG_1");
		ts.setIndicator("All");
		ts.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				adapter = new FriendListFacebookAdapter(FriendGroups.this,FriendGroups.this, friendsData, R.layout.friend_view,
						new String[] { "name","birthday","picture"}, new int[] {R.id.friendViewText, R.id.friendViewImage});
				listView1.setAdapter(adapter);
				return listView1;
			}
		});
		myTabHost.addTab(ts);
		
		TabSpec ts1 = myTabHost.newTabSpec("TAB_TAG_2");
		ts1.setIndicator("Seal");
		ts1.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				
				return listView2;
			}
		});
		myTabHost.addTab(ts1);
		
		TabSpec ts2 = myTabHost.newTabSpec("TAB_TAG_3");
		ts2.setIndicator("Tone");
		ts2.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				
				return listView3;
			}
		});
		myTabHost.addTab(ts2);
		
		TabSpec ts3 = myTabHost.newTabSpec("TAB_TAG_3");
		ts3.setIndicator("Analog");
		ts3.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				
				return listView4;
			}
		});
		myTabHost.addTab(ts3);
		
		TabSpec ts4 = myTabHost.newTabSpec("TAB_TAG_4");
		ts4.setIndicator("Occult");
		ts4.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				
				return listView5;
			}
		});
		myTabHost.addTab(ts4);
		
		TabSpec ts5 = myTabHost.newTabSpec("TAB_TAG_5");
		ts5.setIndicator("Focus");
		ts5.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				
				return listView6;
			}
		});
		myTabHost.addTab(ts5);
		
		TabSpec ts6 = myTabHost.newTabSpec("TAB_TAG_6");
		ts6.setIndicator("Guide");
		ts6.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				
				return listView7;
			}
		});
		myTabHost.addTab(ts6);
		
	}
}
