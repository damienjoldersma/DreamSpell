package org.joldersma.damien.DreamSpell;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class FriendListFacebookAdapter extends SimpleAdapter {

	private Context context;

    private int layout;
	
    private List<? extends Map<String, ?>> data;
    
	public FriendListFacebookAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layout = resource;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		
		final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

       
        String name = (String) data.get(position).get("name");

        /**
         * Next set the name of the entry.
         */
        TextView name_text = (TextView) v.findViewById(R.id.friendViewText);
        if (name_text != null) {
            name_text.setText(name);
        }

        String birthday = (String) data.get(position).get("birthday");

        /**
         * Next set the birthday of the entry.
         */
        TextView birthday_text = (TextView) v.findViewById(R.id.friendViewBirthDay);
        if (birthday_text != null) {
        	birthday_text.setText(birthday);
        }
        
        String picture = (String) data.get(position).get("picture");
        
        /**
         * Next set the photo of the entry.
         */
        ImageView photo_view = (ImageView) v.findViewById(R.id.friendViewImage);
        if (photo_view != null) {
        	
        	Drawable drawable = LoadImageFromWebOperations(picture);
        
        	photo_view.setImageDrawable(drawable);

        	
        }
        
        return v;
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

