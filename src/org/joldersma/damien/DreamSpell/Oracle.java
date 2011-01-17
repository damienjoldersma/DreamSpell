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
		
		ImageView waveSpellSeal = (ImageView) findViewById(R.id.wavespell_seal);
		waveSpellSeal.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getWavespell()));
		
		ImageView yearTone = (ImageView) findViewById(R.id.oracle_year_tone);
		yearTone.setImageResource(AndroidUtil.getToneResource(DreamSpellUtil.getYearTone()));
		
		ImageView yearSeal = (ImageView) findViewById(R.id.oracle_year_seal);
		yearSeal.setImageResource(AndroidUtil.getGlyphResource(DreamSpellUtil.getYearSeal()));
		
		TextView affirmationText = (TextView) findViewById(R.id.oracle_affirmation);
		affirmationText.setText(DreamSpellUtil.getAffirmation(getResources()));
		
		TextView sealText = (TextView) findViewById(R.id.oracle_seal_text);
		sealText.setText(DreamSpellUtil.getSealDefinition(DreamSpellUtil.getSeal()));
		
		TextView toneText = (TextView) findViewById(R.id.oracle_tone_text);
		toneText.setText(DreamSpellUtil.getToneDefinition(DreamSpellUtil.getTone()));
		
		TextView dailyMeditationText = (TextView) findViewById(R.id.oracle_daily_meditation);
		dailyMeditationText.setText(DreamSpellUtil.getDailyMediation(DreamSpellUtil.getTone()));
		
		TextView guideText = (TextView) findViewById(R.id.oracle_guide_text);
		guideText.setText(DreamSpellUtil.getSealDefinition(DreamSpellUtil.getGuide()));
		
		TextView antipodeText = (TextView) findViewById(R.id.oracle_antipode_text);
		antipodeText.setText(DreamSpellUtil.getSealDefinition(DreamSpellUtil.getAntipode()));
		
		TextView occultText = (TextView) findViewById(R.id.oracle_occult_text);
		occultText.setText(DreamSpellUtil.getSealDefinition(DreamSpellUtil.getOccult()));
		
		TextView analogText = (TextView) findViewById(R.id.oracle_analog_text);
		analogText.setText(DreamSpellUtil.getSealDefinition(DreamSpellUtil.getAnalog()));
		
		TextView wavespellToneText = (TextView) findViewById(R.id.oracle_wavespell_tone_text);
		//wavespellToneText.setText(DreamSpellUtil.GetWavespell(DreamSpellUtil.getWavespell()));
		wavespellToneText.setText(DreamSpellUtil.getToneDefinition(1));
		
		TextView wavespellSealText = (TextView) findViewById(R.id.oracle_wavespell_seal_text);
		wavespellSealText.setText(DreamSpellUtil.getSealDefinition(DreamSpellUtil.getWavespell()));
		
		TextView yearToneText = (TextView) findViewById(R.id.oracle_year_tone_text);
		yearToneText.setText(DreamSpellUtil.getToneDefinition(DreamSpellUtil.getYearTone()));
		
		TextView yearSealText = (TextView) findViewById(R.id.oracle_year_seal_text);
		yearSealText.setText(DreamSpellUtil.getSealDefinition(DreamSpellUtil.getYearSeal()));
		
		TextView sealDefText = (TextView) findViewById(R.id.oracle_seal_def);
		sealDefText.setText(DreamSpellUtil.getOracleDefinition(1));
		
		TextView yearDefText = (TextView) findViewById(R.id.oracle_year_def);
		yearDefText.setText(DreamSpellUtil.getOracleDefinition(2));
		
		TextView guideDefText = (TextView) findViewById(R.id.oracle_guide_def);
		guideDefText.setText(DreamSpellUtil.getOracleDefinition(3));
		
		TextView antipodeDefText = (TextView) findViewById(R.id.oracle_antipode_def);
		antipodeDefText.setText(DreamSpellUtil.getOracleDefinition(4));
		
		TextView occultDefText = (TextView) findViewById(R.id.oracle_occult_def);
		occultDefText.setText(DreamSpellUtil.getOracleDefinition(5));
		
		TextView analogDefText = (TextView) findViewById(R.id.oracle_analog_def);
		analogDefText.setText(DreamSpellUtil.getOracleDefinition(6));
		
		TextView wavespellDefText = (TextView) findViewById(R.id.oracle_wavespell_def);
		wavespellDefText.setText(DreamSpellUtil.getOracleDefinition(7));
		
		
		
		
//		<TextView android:id="@+id/oracle_seal_def"
//		<TextView android:id="@+id/oracle_guide_def"
//		<TextView android:id="@+id/oracle_antipode_def"
//		<TextView android:id="@+id/oracle_occult_def"
//		<TextView android:id="@+id/oracle_analog_def"
//		<TextView android:id="@+id/oracle_wavespell_def"
//		<TextView android:id="@+id/oracle_year_def"

	}
}
