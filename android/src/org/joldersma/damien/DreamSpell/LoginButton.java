/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
package org.joldersma.damien.DreamSpell;

import org.joldersma.damien.DreamSpell.SessionEvents.AuthListener;
import org.joldersma.damien.DreamSpell.SessionEvents.LogoutListener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class LoginButton extends ImageButton {
    
	private static final String TAG = "DreamSpell";
	
    private Facebook mFb;
    private Handler mHandler;
    private SessionListener mSessionListener = new SessionListener();
    private String[] mPermissions;
    private Activity mActivity;
    
    public LoginButton(Context context) {
        super(context);
    }
    
    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void init(final Activity activity, final Facebook fb) {
    	init(activity, fb, new String[] {});
    }
    
    public void init(final Activity activity, final Facebook fb,
                     final String[] permissions) {
        mActivity = activity;
        mFb = fb;
        mPermissions = permissions;
        mHandler = new Handler();
        
        setBackgroundColor(Color.TRANSPARENT);
        setAdjustViewBounds(true);
        setImageResource(fb.isSessionValid() ?
                         R.drawable.logout_button : 
                         R.drawable.login_button);
        drawableStateChanged();
        
        SessionEvents.addAuthListener(mSessionListener);
        SessionEvents.addLogoutListener(mSessionListener);
        setOnClickListener(new ButtonOnClickListener());
    }
    
    public void doClick() {
		if (mFb.isSessionValid()) {
			Log.d(TAG,"buttonOnClick listener - session is valid going to logout");
		    SessionEvents.onLogoutBegin();
		    AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
		    asyncRunner.logout(getContext(), new LogoutRequestListener());
		} else {
			Log.d(TAG,"buttonOnClick listener - session is not valid going to login,");
		    mFb.authorize(mActivity, mPermissions, new LoginDialogListener());
		}
	}

	private final class ButtonOnClickListener implements OnClickListener {
        
        public void onClick(View arg0) {
        	Log.d(TAG,"buttonOnClick listener");
            doClick();
        }
    }

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
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }
    
    private class SessionListener implements AuthListener, LogoutListener {
        
		public void onAuthSucceed() {
        	Log.d(TAG, "SessionListener onAuthSucceed!");
            setImageResource(R.drawable.logout_button);
            SessionStore.save(mFb, getContext()	);
        }

        public void onAuthFail(String error) {
        	Log.d(TAG, "SessionListener onAuthFail=" + error);
        }
        
        public void onLogoutBegin() {           
        }
        
        public void onLogoutFinish() {
            SessionStore.clear(getContext());
            setImageResource(R.drawable.login_button);
        }
    }
    
}
*/
