package org.joldersma.damien.DreamSpell;

import android.util.Log;

public class AndroidUtil {

	public static final String TAG = "AndroidUtil";
	
	public static int toneResources[] = new int[] {
		R.drawable.tone1,
		R.drawable.tone2,
		R.drawable.tone3,
		R.drawable.tone4,
		R.drawable.tone5,
		R.drawable.tone6,
		R.drawable.tone7,
		R.drawable.tone8,
		R.drawable.tone9,
		R.drawable.tone10,
		R.drawable.tone11,
		R.drawable.tone12,
		R.drawable.tone13 
	};
	
	public static int glyphResources[] = new int[] {
		R.drawable.glyph1,
		R.drawable.glyph2,
		R.drawable.glyph3,
		R.drawable.glyph4,
		R.drawable.glyph5,
		R.drawable.glyph6,
		R.drawable.glyph7,
		R.drawable.glyph8,
		R.drawable.glyph9,
		R.drawable.glyph10,
		R.drawable.glyph11,
		R.drawable.glyph12,
		R.drawable.glyph13,
		R.drawable.glyph14,
		R.drawable.glyph15,
		R.drawable.glyph16,
		R.drawable.glyph17,
		R.drawable.glyph18,
		R.drawable.glyph19,
		R.drawable.glyph20
	};
	
	public static int getToneResource(int tone)
	{
		Log.d(TAG,"Getting tone resource for tone: " + tone);
		return toneResources[tone-1];
	}
	
	public static int getGlyphResource(int glyph)
	{
		return glyphResources[glyph-1];
	}
	
}
