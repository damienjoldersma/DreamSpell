package org.joldersma.damien.DreamSpell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataHelper {
	
	public static final String TAG = "DreamSpell";
	
	private static final String DATABASE_NAME = "friends.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "kin";

	private Context context;
	private SQLiteDatabase db;

	private SQLiteStatement insertStmt;
	private static final String INSERT = "insert into " 
			+ TABLE_NAME + "(name,birthday,picture,facebookId) values (?,?,?,?)";

	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
		this.insertStmt = this.db.compileStatement(INSERT);
	}

	public long insert(Map<String, String> kin)
	{
		Log.d(TAG,String.format("Doing insert name=%s, facebookId=%s",kin.get("name"),kin.get("facebookId")));
		return insert(kin.get("name"),kin.get("birthday"),kin.get("picture"),kin.get("facebookId"));
	}
	
	public long insert(String name,String birthday, String picture, String facebookId) {
		this.insertStmt.bindString(1, name == null ? "null" : name );
		this.insertStmt.bindString(2, birthday == null ? "null" : birthday );
		this.insertStmt.bindString(3, picture == null ? "null" : picture );
		this.insertStmt.bindString(4, facebookId == null ? "null" : facebookId );
		return this.insertStmt.executeInsert();
	}

	public void deleteAll() {
		Log.d(TAG,String.format("Doing deleteAll"));
		this.db.delete(TABLE_NAME, null, null);
	 }

	//friendsData = new ArrayList<Map<String, String>>();
	//Map<String, String> friend;
	
	public List<Map<String,String>> selectAll()
	{
		Log.d(TAG,"Doing selectAll");
		List<Map<String,String>> list =  new ArrayList<Map<String, String>>();
		
		Cursor cursor = this.db.query(TABLE_NAME, new String[] { "name","birthday","picture","facebookId" }, 
				null, null, null, null, "name desc");
		if (cursor.moveToFirst()) 
		{
			do 
			{
				Map<String, String> kin = new HashMap<String, String>();
				kin.put("name", cursor.getString(0));
				kin.put("birthday", cursor.getString(1));
				kin.put("picture", cursor.getString(2));
				kin.put("facebookId", cursor.getString(3));
				list.add(kin); 
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				 cursor.close();
			}
		
		return list;
	}
	
	 public List<String> selectAllNames() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query(TABLE_NAME, new String[] { "name","birthday","picture","facebookId" }, 
			null, null, null, null, "name desc");
		if (cursor.moveToFirst()) {
			 do {
				list.add(cursor.getString(0)); 
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				 cursor.close();
			}
			return list;
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			 super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
	 
		public void onCreate(SQLiteDatabase db) {
			 db.execSQL("CREATE TABLE " + TABLE_NAME + 
		" (id INTEGER PRIMARY KEY, name TEXT, birthday TEXT, picture TEXT, facebookId TEXT)");
		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			 Log.w("Example", "Upgrading database, this will drop tables and recreate.");
			 db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			 onCreate(db);
		}
	 }

	 public class FriendsDbOpenHelper extends SQLiteOpenHelper	{

		private static final int DATABASE_VERSION = 2;
		private static final String FRIENDS_TABLE_NAME = "FRIENDS";
		private static final String KEY_WORD = "KEYWORD";
		private static final String KEY_DEFINITION = "KEYDEF";
		
		private static final String FRIENDS_TABLE_CREATE =
									"CREATE TABLE " + FRIENDS_TABLE_NAME + " (" +
									KEY_WORD + " TEXT, " +
									KEY_DEFINITION + " TEXT);";
		private static final String DATABASE_NAME = "DreamSpell";

			FriendsDbOpenHelper(Context context) {
					super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
					db.execSQL(FRIENDS_TABLE_CREATE);
			}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
			
	}

}




