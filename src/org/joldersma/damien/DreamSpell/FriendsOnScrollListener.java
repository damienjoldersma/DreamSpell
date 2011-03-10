package org.joldersma.damien.DreamSpell;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.AbsListView.OnScrollListener;

public class FriendsOnScrollListener implements OnScrollListener {

	public static final String TAG = "DreamSpell";
	FriendListFacebookAdapter adapter;
	
	public FriendsOnScrollListener(FriendListFacebookAdapter a)
	{
		this.adapter = a;
	}

	public void onScroll(AbsListView view, int firstVisible,
			int visibleCount, int totalCount) {

        boolean loadMore = /* maybe add a padding */
            firstVisible + visibleCount >= totalCount;

        if(loadMore && adapter != null ) {
        	Log.d(TAG,"Increasing adapter count to 3");
        	adapter.count += 3; // visibleCount; // or any other amount
        	adapter.notifyDataSetChanged();
        }
		
	}
	
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

}