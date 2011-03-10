package org.joldersma.damien.DreamSpell;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class FriendListFacebookAdapter extends SimpleAdapter {

	public static final String TAG = "DreamSpell";
	
	private Context context;

	private Friends friends;

	public static final String KEY_DATE = "date";
	
	private int layout;
	
    private List<? extends Map<String, ?>> data;
    public Hashtable<String, Date> birthdays;
    private final Map<String, Drawable> drawableMap;
    
    private Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    int count = 6; /* starting amount */
//    public int getCount() { return count; }
//    public Object getItem(int pos) { return pos; }
//    public long getItemId(int pos) { return pos; }
    
	public FriendListFacebookAdapter(Activity a, Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		drawableMap = new HashMap<String, Drawable>();
		this.context = context;
		this.friends = (Friends)context;
		this.layout = resource;
		this.data = data;
		birthdays = new Hashtable<String, Date>();
		
		activity = a;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity.getApplicationContext());
	}
	
	public Hashtable<String, Date> getBirthdays() {
		return birthdays;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int dataSize = data.size();
		if ( count < dataSize)
			return count;
		else
			return dataSize;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getDropDownView(position, convertView, parent);
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return super.getFilter();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		
		final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);        
        
        //v.getLayoutParams().width = LayoutParams.FILL_PARENT;
        //v.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		 //View v = View.inflate(context, R.layout.friend_view, null);
        
	      v.setClickable(true);
	      v.setFocusable(true);
	      //view.setBackgroundResource(android.R.drawable.menuitem_background);
	      v.setOnClickListener(new OnClickListener(){

	        public void onClick(View v) {
	            //new AlertDialog.Builder(context).setTitle("touched").show();
	        	Log.d(TAG,"onItemClick: click, position is " + position);
				
				Object o = getItem(position);
				Log.d(TAG,"onItemClick: object o is " + o);
				
				HashMap<String, String> friend = (HashMap<String, String>) getItem(position);
				Log.d(TAG,"onItemClick: friend is " + friend);
				
				String friendId = friend.get("facebookId");
				String name = friend.get("name");
				String bday = friend.get("birthday");
				String picture = friend.get("picture");
				
				Log.d(TAG,String.format("onItemClick: friend values are friendId=%s, name=%s, bday=%s, picture=%s",friendId,name,bday,picture));
				
				Date birthday = new Date();
				
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

	      });
		
		
		String id = (String) data.get(position).get("facebookId");
		String name = (String) data.get(position).get("name");
		String birthday = (String) data.get(position).get("birthday");
		String picture = (String) data.get(position).get("picture");
         
		 
		    

        /**
         * Next set the name of the entry.
         */
        TextView name_text = (TextView) v.findViewById(R.id.friendViewText);
        if (name_text != null) {
        	name_text.setSingleLine(false);
        	name_text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            name_text.setText(name);
            name_text.setFocusable(false);
            name_text.setHorizontallyScrolling(false);
            name_text.getLayoutParams().width = LayoutParams.FILL_PARENT;
            name_text.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
            //name_text.getLayoutParams().weight = 1;
            //name_text.setWidth(0);
            name_text.setEms(10);
            name_text.setMarqueeRepeatLimit(-1);
            //name_text.setHeight(0);
        }

        /**
         * Next set the birthday of the entry.
         */
//        TextView birthday_text = (TextView) v.findViewById(R.id.friendViewBirthDay);
//        if (birthday_text != null) {
//        	birthday_text.setText(birthday);
//        }
       
        /**
         * Next set the photo of the entry.
         */
        ImageView photo_view = (ImageView) v.findViewById(R.id.friendViewImage);
        if (photo_view != null) {        	
//        	Drawable drawable = LoadImageFromWebOperations(picture);        
//        	photo_view.setImageDrawable(drawable);        
       
        	//fetchDrawableOnThread(picture,photo_view);
        	
        	imageLoader.DisplayImage(picture, activity, photo_view);
        
        	photo_view.setFocusable(false);
//	        Thread thread = new Thread() {
//	    		@Override
//	    		public void run() {
//	    			//TODO : set imageView to a "pending" image
//	    			Drawable drawable = LoadImageFromWebOperations(picture);        
//	            	photo_view.setImageDrawable(drawable);
//	    			
//	    			//Drawable drawable = fetchDrawable(urlString);
//	    			Message message = handler.obtainMessage(1, drawable);
//	    			handler.sendMessage(message);
//	    		}
//	    	};
//	    	thread.start();
	        
        }
        
        
        
        /**
         * Now do the daycalc and show tone and glyph
         */
        try
    	{
	        SimpleDateFormat df1 = new SimpleDateFormat( "MM/dd/yyyy" );
	        Date bdate = df1.parse(birthday);
	        birthdays.put(id, bdate);	        
	        DreamSpellUtil.Calc(bdate);
    	
	        /**
	         * Next set the Tone of the entry.
	         */
	        ImageView tone = (ImageView) v.findViewById(R.id.friendViewTone);
	        if (tone != null) {
	    		tone.setImageResource(AndroidUtil.getToneResource(DreamSpellUtil.getTone()));        	
	    		tone.setFocusable(false);
	        }
	        
	        /**
	         * Next set the Glyph of the entry.
	         */
	        ImageView glpyh = (ImageView) v.findViewById(R.id.friendViewGlyph);
	        if (glpyh != null) {
	        	glpyh.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getSeal()));
	        	glpyh.setFocusable(false);
	        }
        
    	}
    	catch (Exception e)
    	{
    		Log.e(TAG, String.format("Error doing day calc, date=%s",birthday),e);
    		
    	}
    	
    	v.setFocusable(false);
    	//v.setOnClickListener();
    	
        return v;
	}

	 public Drawable fetchDrawable(String urlString) {
	    	if (drawableMap.containsKey(urlString)) {
	    		return drawableMap.get(urlString);
	    	}

	    	Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
	    	try {
	    		InputStream is = fetch(urlString);
	    		Drawable drawable = Drawable.createFromStream(is, "src");
	    		drawableMap.put(urlString, drawable);
	    		Log.d(this.getClass().getSimpleName(), "got a thumbnail drawable: " + drawable.getBounds() + ", "
	    				+ drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
	    				+ drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
	    		return drawable;
	    	} catch (MalformedURLException e) {
	    		Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
	    		return null;
	    	} catch (IOException e) {
	    		Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
	    		return null;
	    	}
	    }
	
	public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
    	if (drawableMap.containsKey(urlString)) {
    		imageView.setImageDrawable(drawableMap.get(urlString));
    	}

    	final Handler handler = new Handler() {
    		@Override
    		public void handleMessage(Message message) {
    			imageView.setImageDrawable((Drawable) message.obj);
    		}
    	};

    	Thread thread = new Thread() {
    		@Override
    		public void run() {
    			//TODO : set imageView to a "pending" image
    			Drawable drawable = fetchDrawable(urlString);
    			Message message = handler.obtainMessage(1, drawable);
    			handler.sendMessage(message);
    		}
    	};
    	thread.start();
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpGet request = new HttpGet(urlString);
    	HttpResponse response = httpClient.execute(request);
    	return response.getEntity().getContent();
    }
	
	@Override
	public ViewBinder getViewBinder() {
		// TODO Auto-generated method stub
		return super.getViewBinder();
	}

	@Override
	public void setDropDownViewResource(int resource) {
		// TODO Auto-generated method stub
		super.setDropDownViewResource(resource);
	}

	@Override
	public void setViewBinder(ViewBinder viewBinder) {
		// TODO Auto-generated method stub
		super.setViewBinder(viewBinder);
	}

	@Override
	public void setViewImage(ImageView v, int value) {
		// TODO Auto-generated method stub
		super.setViewImage(v, value);
	}

	@Override
	public void setViewImage(ImageView v, String value) {
		// TODO Auto-generated method stub
		super.setViewImage(v, value);
	}

	@Override
	public void setViewText(TextView v, String text) {
		// TODO Auto-generated method stub
		super.setViewText(v, text);
	}

	private Drawable LoadImage(String url)
	{
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
	}
	
	private Drawable LoadImageFromWebOperations(String url)
	{
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
	}
}

