package org.joldersma.damien.DreamSpell;

import java.text.DateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class Oracle extends Activity {

	public static final String TAG = "DreamSpell";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG,"** ON CREATE **");
		setContentView(R.layout.oracle);
		
		TextView mDateTitle = (TextView) findViewById(R.id.date_title);
		mDateTitle.setText(DateFormat.getDateInstance().format(DreamSpellUtil.getCurrentDate()));

		TextView mDateName = (TextView) findViewById(R.id.date_name);
		mDateName.setText(DreamSpellUtil.GetName());
		
		ImageView tone = (ImageView) findViewById(R.id.tone);
		tone.setImageResource(AndroidUtil.getToneResource(DreamSpellUtil.getTone()));
		
		ImageView seal = (ImageView) findViewById(R.id.seal);
		seal.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getSeal()));
		
		ImageView analog = (ImageView) findViewById(R.id.analog);
		analog.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getAnalog()));
		
		ImageView occult = (ImageView) findViewById(R.id.occult);
		occult.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getOccult()));
		
		ImageView antipode = (ImageView) findViewById(R.id.antipode);
		antipode.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getAntipode()));
		
		ImageView guide = (ImageView) findViewById(R.id.guide);
		guide.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getGuide()));
		
		TextView sealText = (TextView) findViewById(R.id.oracle_seal_text);
		sealText.setText(DreamSpellUtil.GetSealName(DreamSpellUtil.getSeal()));
		
		TextView toneText = (TextView) findViewById(R.id.oracle_tone_text);
		toneText.setText(DreamSpellUtil.GetSealName(DreamSpellUtil.getTone()));
		
		TextView guideText = (TextView) findViewById(R.id.oracle_guide_text);
		guideText.setText(DreamSpellUtil.GetSealName(DreamSpellUtil.getGuide()));
		
		TextView antipodeText = (TextView) findViewById(R.id.oracle_antipode_text);
		antipodeText.setText(DreamSpellUtil.GetSealName(DreamSpellUtil.getAntipode()));
		
		TextView occultText = (TextView) findViewById(R.id.oracle_occult_text);
		occultText.setText(DreamSpellUtil.GetSealName(DreamSpellUtil.getOccult()));
		
		TextView analogText = (TextView) findViewById(R.id.oracle_analog_text);
		analogText.setText(DreamSpellUtil.GetSealName(DreamSpellUtil.getAnalog()));
		
		TextView wavespellToneText = (TextView) findViewById(R.id.oracle_wavespell_tone_text);
		//wavespellToneText.setText(DreamSpellUtil.GetWavespell(DreamSpellUtil.getWavespell()));
		wavespellToneText.setText("Tone 1 Magnetic");
		
		TextView wavespellSealText = (TextView) findViewById(R.id.oracle_wavespell_seal_text);
		wavespellSealText.setText(DreamSpellUtil.GetSealName(DreamSpellUtil.getWavespell()));
		
		TextView yearToneText = (TextView) findViewById(R.id.oracle_year_tone_text);
		yearToneText.setText(DreamSpellUtil.GetYearTone());
		
		TextView yearSealText = (TextView) findViewById(R.id.oracle_year_seal_text);
		yearSealText.setText(DreamSpellUtil.GetSealName(DreamSpellUtil.getYearSeal()));
	}
}
