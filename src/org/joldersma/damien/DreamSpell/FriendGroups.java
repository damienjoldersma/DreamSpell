package org.joldersma.damien.DreamSpell;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class FriendGroups extends Activity {
	
	public static final String TAG = "DreamSpell";
	private DataHelper dh;
	private FriendListFacebookAdapter adapter = null;
	private List<Map<String, String>> friendsData;	
	private TextView textView2, textView3, textView4, textView5, textView6, textView7;
	private ListView listView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.friend_groups);
		
		this.dh = new DataHelper(this);
		friendsData = this.dh.selectAll();
		
		TabHost myTabHost = (TabHost)this.findViewById(R.id.tabhost);
		myTabHost.setup();
		
		//textView1 = (TextView)this.findViewById(R.id.friend_groups_text_view);
		//textView2 = (TextView)this.findViewById(R.id.friend_groups_text_view);
		listView1 = new ListView(FriendGroups.this);
		textView2 = new TextView(FriendGroups.this);
		textView3 = new TextView(FriendGroups.this);
		textView4 = new TextView(FriendGroups.this);
		textView5 = new TextView(FriendGroups.this);
		textView6 = new TextView(FriendGroups.this);
		textView7 = new TextView(FriendGroups.this);
		
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
				textView2.setText("party party 2");
				return textView2;
			}
		});
		myTabHost.addTab(ts1);
		
		TabSpec ts2 = myTabHost.newTabSpec("TAB_TAG_3");
		ts2.setIndicator("Tone");
		ts2.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				textView3.setText("abra cadabra 3");
				return textView3;
			}
		});
		myTabHost.addTab(ts2);
		
		TabSpec ts3 = myTabHost.newTabSpec("TAB_TAG_3");
		ts3.setIndicator("Analog");
		ts3.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				textView4.setText("analog fun");
				return textView4;
			}
		});
		myTabHost.addTab(ts3);
		
		TabSpec ts4 = myTabHost.newTabSpec("TAB_TAG_4");
		ts4.setIndicator("Occult");
		ts4.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				textView5.setText("occult time");
				return textView5;
			}
		});
		myTabHost.addTab(ts4);
		
		TabSpec ts5 = myTabHost.newTabSpec("TAB_TAG_5");
		ts5.setIndicator("Focus");
		ts5.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				textView6.setText("focus");
				return textView6;
			}
		});
		myTabHost.addTab(ts5);
		
		TabSpec ts6 = myTabHost.newTabSpec("TAB_TAG_6");
		ts6.setIndicator("Guide");
		ts6.setContent(new TabHost.TabContentFactory(){
			public View createTabContent(String tag)
			{
				textView7.setText("focus");
				return textView7;
			}
		});
		myTabHost.addTab(ts6);
		
	}
}
