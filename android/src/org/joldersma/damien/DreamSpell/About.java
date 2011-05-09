package org.joldersma.damien.DreamSpell;

import java.text.DateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity {

	public static final String TAG = "DreamSpell";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG,"** ON CREATE **");
		setContentView(R.layout.about);
		
		WebView mWebView = (WebView) findViewById(R.id.WebView01);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl("file:///android_asset/about.html");
	}
}
