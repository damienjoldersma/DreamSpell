package org.joldersma.damien.DreamSpell;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DreamSpellUtil {

	public static final String TAG = "DreamSpell";
	
	static String[][] tones,seals;
	static String[] oracleTextDefinition;
	
	static double y,x,rem;
	static int cont,i,loopstart,daydist;
	static int byear = 0, bmonth = 0, bday = 0;
	static int yseal = 0, ytone = 0, magseal = 0, kin = 0;
	static int tone = 0, seal = 0, guide = 0, antipode = 0, occult = 0, analog = 0;

	static Date currentDate;
	static int[] gmonth = new int[] { 0,31,28,31,30,31,30,6,31,30,31,30,31 };

	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		DreamSpellUtil.Calc();
		System.out.println("CurrentDate: " + DreamSpellUtil.getCurrentDate());
	}
	
	public static void init(Resources res)
	{
		tones = getToneResources(res);
		seals = getSealResources(res);
		oracleTextDefinition = getOracleTextDefinitionResources(res);
	}

	public static void buildKinLookup(SQLiteDatabase db) {
		Date rowDate = new Date(Date.parse("12/25/2010")); // Happens to be Kin 1, any Kin 1 date would work 
		
		Calendar c1 = Calendar.getInstance();
		c1.set(2010,11,24); // Start it a day before, Kin 260, allow it to roll over
		
		rowDate = c1.getTime();
		
		for (int kinRow = 1; kinRow <= 260; kinRow++)
		{
			c1.add(Calendar.DATE,1);
			rowDate = c1.getTime();
			DreamSpellUtil.Calc(rowDate);
			
			Date date = DreamSpellUtil.getCurrentDate();
			int kin = DreamSpellUtil.getKin();
			int seal = DreamSpellUtil.getSeal();
			int tone = DreamSpellUtil.getTone();
			int analog = DreamSpellUtil.getAnalog();
			int occult = DreamSpellUtil.getOccult();
			int antipode = DreamSpellUtil.getAntipode();
			int guide = DreamSpellUtil.getGuide();

			db.execSQL(String.format("INSERT INTO KIN_LOOKUP (seal,tone,analog,occult,antipode,guide) VALUES (%s,%s,%s,%s,%s,%s);",
					seal,tone,analog,occult,antipode,guide));
		}
	}
	
	public static int getDaydist() {
		return daydist;
	}

	public static void setDaydist(int daydist) {
		DreamSpellUtil.daydist = daydist;
	}

	public static int getKin() {
		return kin;
	}

	public static void setKin(int kin) {
		DreamSpellUtil.kin = kin;
	}

	public static int getTone() {
		return tone;
	}

	public static void setTone(int tone) {
		DreamSpellUtil.tone = tone;
	}

	public static int getSeal() {
		return seal;
	}

	public static void setSeal(int seal) {
		DreamSpellUtil.seal = seal;
	}

	public static int getGuide() {
		return guide;
	}

	public static void setGuide(int guide) {
		DreamSpellUtil.guide = guide;
	}

	public static int getAntipode() {
		return antipode;
	}

	public static void setAntipode(int antipode) {
		DreamSpellUtil.antipode = antipode;
	}

	public static int getOccult() {
		return occult;
	}

	public static void setOccult(int occult) {
		DreamSpellUtil.occult = occult;
	}

	public static int getAnalog() {
		return analog;
	}

	public static void setAnalog(int analog) {
		DreamSpellUtil.analog = analog;
	}
	
	public static int getYearTone() {
		return ytone;
	}

	public static void setYearTone(int t) {
		DreamSpellUtil.ytone = t;
	}
	
	public static int getYearSeal() {
		return yseal;
	}

	public static void setYearSeal(int s) {
		DreamSpellUtil.yseal = s;
	}
	
	
	public static int getWavespell() {
		return magseal;
	}

	public static Date getCurrentDate() {
		return currentDate;
	}

	public static void setCurrentDate(Date currentDate) {
		DreamSpellUtil.currentDate = currentDate;
	}

	public static String[][] getTones() {
		return tones;
	}

	public static void setTones(String[][] tones) {
		DreamSpellUtil.tones = tones;
	}

	public static String[][] getSeals() {
		return seals;
	}

	public static void setSeals(String[][] seals) {
		DreamSpellUtil.seals = seals;
	}

	public static String[][] getToneResources(Resources res)
	{
		// TONE NAME,CREATIVE POWER,FUNCTION,ACTION
		String[] tones_raw = res.getStringArray(R.array.tones);
		
		String[][] tones = new String[14][5];
		for (int i = 0; i < tones_raw.length; i++) {
			String t = tones_raw[i];
			String[] values = t.split(",");
			tones[i] = values;
		}
		
		return tones;
	}

	public static String[][] getSealResources(Resources res)
	{
		// GLYPH,MAYAN NAME,COLOR,ACTION,CREATIVE POWER,FUNCTION
		String[] seals_raw = res.getStringArray(R.array.seals);
		
		String[][] seals = new String[21][7];
		for (int i = 0; i < seals_raw.length; i++) {
			String g = seals_raw[i];
			String[] values = g.split(",");
			seals[i] = values;
		}
		
		return seals;
	}

	public static String[] getOracleTextDefinitionResources(Resources res)
	{
		return res.getStringArray(R.array.oracle_text_definition);
	}
	
	public static String getAffirmation(Resources res)
	{
		// TONE NAME,CREATIVE POWER,FUNCTION,ACTION
		String[] affirmation_tones_raw = res.getStringArray(R.array.affirmation_tones);
		
		// GLYPH,MAYAN NAME,COLOR,ACTION,CREATIVE POWER,FUNCTION
		String[] affirmation_glyphs_raw = res.getStringArray(R.array.affirmation_glyphs);
		
		String[][] affirmation_tones = new String[13][5];
		for (int i = 0; i < affirmation_tones_raw.length; i++) {
			String t = affirmation_tones_raw[i];
			String[] values = t.split(",");
			affirmation_tones[i] = values;
		}
		
		String[][] affirmation_glyphs = new String[20][7];
		for (int i = 0; i < affirmation_glyphs_raw.length; i++) {
			String g = affirmation_glyphs_raw[i];
			String[] values = g.split(",");
			affirmation_glyphs[i] = values;
		}
		
		String guidePower = affirmation_glyphs[getGuide()-1][4];
		
		if ( getTone() == 1 || getTone() == 6 || getTone() == 11)
			guidePower = "MY OWN POWER DOUBLED!";
		
		Log.d(TAG,"getSeal is " + getSeal());
		Log.d(TAG,"guidePower is " + guidePower);
		
		/*
		 * I 'Tone Power' in order to 'Tribe Action'
		 * 'Tone Action' 'Tribe Essence'
		 * I seal the 'Timecell' of 'Tribe Power'
		 * With the 'Tone Name' tone of 'Tone Essence'
		 * I am guided by the power of 'Guide Power'
		 */
		
		return String.format(
		 "I %s in order to %s\n" +
		 "%s %s\n" +
		 "I seal the %s of %s\n" +
		 "With the %s tone of %s\n" +
		 "I am guided by the power of %s",
		 affirmation_tones[getTone()-1][1],affirmation_glyphs[getSeal()-1][3],
		 affirmation_tones[getTone()-1][3],affirmation_glyphs[getSeal()-1][5],
		 affirmation_glyphs[getSeal()-1][6],affirmation_glyphs[getSeal()-1][4],
		 affirmation_tones[getTone()-1][0],affirmation_tones[getTone()-1][2],
		 guidePower);
	}
	
	public static String getSealDefinition(int s)
	{
		// GLYPH,MAYAN NAME,COLOR,ACTION,POWER,FUNCTION
		return String.format("%s (%s) %s and emphasizes %s",
				seals[s][0],seals[s][1],seals[s][2],seals[s][4]);
	}
	
	public static String getToneDefinition(int t)
	{
		// TONE,POWER,FUNCTION,ACTION
		return String.format("Tone %s %s, creative power to %s %s, action of %s",
				t,tones[t][0],tones[t][1],tones[t][2],tones[t][3]);
	}
	
	public static String getOracleDefinition(int i)
	{
		return oracleTextDefinition[i-1];
	}
	
	public static String getDailyMediation()
	{
		return getDailyMediation(tone);
	}
	
	public static String getDailyMediation(int t)
	{
		return "Daily meditation - " + tones[t][4];
	}
	
	public DreamSpellUtil()
	{
	}

	public static String GetName()
	{
		return GetName(getSeal());
	}
	
	public static String GetName(int s)
	{
		return GetName(s,tone);
	}
	
	public static String GetName(int s, int t)
	{
		String seal = seals[s][0];
		seal = seal.replace(" "," " + tones[t][0] + " ");
		return seal;
	}
	
	public static String GetSealName(int s)
	{
		return seals[s][0];
	}

	public static String GetToneAction()
	{
		return GetToneAction(getTone());
	}	
	
	public static String GetTribeAction()
	{
		return GetGlyphAction(getSeal());
	}
	
	public static String GetAction(Tone t)
	{
		return GetToneAction(t.getNumber());
	}
	
	public static String GetAction(Glyph g)
	{
		return GetGlyphAction(g.getNumber());
	}
	
	public static String GetToneName(int t)
	{
		String action = tones[t][0];
		return action;
	}
	
	public static String GetToneAction(int t)
	{
		String action = tones[t][2];
		return action;
	}
	
	public static String GetGlyphAction(int g)
	{
		String action = seals[g][2];			
		return action;
	}
	
	public static String GetEssence()
	{
		return GetToneEssence(getTone());
	}
	
	public static String GetEssence(Tone t)
	{
		return GetToneEssence(t.getNumber());
	}
	
	public static String GetEssence(Glyph g)
	{
		return GetGlyphEssence(g.getNumber());
	}
	
	public static String GetToneEssence(int t)
	{
		String esssence = tones[t][1];
		return esssence;
	}
	
	public static String GetGlyphEssence(int g)
	{
		String esssence = seals[g][3];
		return esssence;
	}
	
	public static String GetTonePower()
	{
		return GetTonePower(getTone());
	}
	
	public static String GetPower(Tone t)
	{
		return GetTonePower(t.getNumber());
	}
	
	public static String GetPower(Glyph g)
	{
		return GetGlyphPower(g.getNumber());
	}
	
	public static String GetTonePower(int t)
	{
		String power = tones[t][3];
		return power;
	}				 

	public static String GetGlyphPower(int g)
	{
		String power = seals[g][4];
		return power;
	}		
	
	public static String GetWavespell()
	{
		return GetName(getWavespell());
	}
	
	public static String GetWavespell(int magseal)
	{
		return GetName(magseal);
	}
	
	public static String GetYearTone()
	{
		return GetYearTone(ytone);
	}
	
	public static String GetYearTone(int t)
	{
		return GetToneName(t);
	}
	
	public static String GetYearSeal()
	{
		return GetYearSeal(yseal);
	}
	
	public static String GetYearSeal(int s)
	{
		return GetName(s);
	}
	
	public static void Calc()
	{
		Calc(new Date(System.currentTimeMillis()));
	}
	
	public static void Calc(Date date)
	{			
		setCurrentDate(date);
		DreamSpellUtil.CalcYearSeal();
		DreamSpellUtil.CalcYearTone();
		DreamSpellUtil.CalcDistanceFromDayOutOfTime();
		DreamSpellUtil.CalcSeal();
		DreamSpellUtil.CalcTone();
		DreamSpellUtil.CalcOracle();		
	}
	
	public static int CalcYearSeal()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentDate());
		
		byear = cal.get(GregorianCalendar.YEAR);
		bmonth = cal.get(GregorianCalendar.MONTH)+1;
		bday = cal.get(GregorianCalendar.DAY_OF_MONTH);
		
		//     calc year seal 
		if((bmonth<7)||((bmonth==7)&&(bday<26)))byear--;      
		yseal=4+(15*(calcyseal(byear)-1));
		if(yseal==34)yseal=14;
		if(yseal==49)yseal=9;
		
		return yseal;
	}
	
	public static int calcyseal(int bthyear)
	{
		
		int abs = (Math.abs(1965-bthyear));
		y=abs/4.00;
		y=y-Math.floor(y);
		if ( y > 0 )
		{
			cont = calcyseal(bthyear+1);
			return(cont+1);
		}
		else 
			return(1);
	}
	
	public static int CalcYearTone()
	{
		// calc year tone
		y=Math.abs(byear-1967);
		x=y/13; 
		rem=x-Math.floor(x);
		//z ytone=Math.round((rem*13)+1); 
		rem=Math.floor((rem*13)+.5); 
		ytone= (int)Math.floor(rem+1);
		
		if(byear<1967)
		{
			ytone=(int)Math.floor(Math.floor(.99999+(rem/13.00))*15-(rem+1));
			if(ytone<0){ytone=(0-1)*ytone;}
		}
		
		return ytone;
		
	}
	
	public static int CalcDistanceFromDayOutOfTime()
	{
		//     calc distance from day out of time  
		loopstart=1;daydist=159;
		if(bmonth>7){loopstart=7;daydist=0;}
		else{ 
			if((bday>=26)&&(bmonth==7)){
				loopstart=7;daydist=-25;
			}
		}
		for(i=loopstart;i<bmonth;i++) daydist+=gmonth[i]; 
		daydist+=bday-1; 	
		
		return daydist;
	}
	
	public static int CalcSeal()
	{
		// calc seal 
		seal=yseal;
		for(i=1;i<=daydist;i++)
		{
			seal++;
			if(seal>20)seal=1;
		}
		return seal;
	}
	
	public static int CalcTone()
	{
		// calc tone and seal 
		tone=ytone;
		for(i=1;i<=daydist;i++)
		{
			tone++;
			if(getTone()>13)setTone(1);
		}
		return tone;
	}
	
	public static void CalcOracle()
	{
		// calc oracle
		if(seal>=tone) magseal=seal-tone+1;
		else magseal=20+seal-tone+1;
		guide=getguide(magseal,tone);
		antipode=seal+10;
		if(antipode>20) antipode=seal-10;
		antipode=antipode;
		analog=19-seal;	if(analog<=0)analog=analog+20;
		occult=21-seal;
		kin=getkin(magseal,tone);			
	}
	
	public static int getguide(int sealone, int mtone)
	{
		double one23,next;
		one23 =1;
		if(sealone<=7)one23++;
		if(sealone<=14)one23++;
		for(i=1;i<mtone;i++){
			next=(20*(Math.floor(one23*1/3))-7);
			one23++;
			if(one23>3)one23=1;
			sealone+=(int)next;
			if(sealone==14) one23=2;
		}
		return(sealone);
	}
	
	public static int getkin(int wvseal, int ktone)
	{
		double one23,next,sealone;
		one23 =3;
		sealone=1;
		for(i=0;sealone!=wvseal && (i<100);i++){
			next=(20*(Math.floor(one23*1/3))-7);
			one23++;
			if(one23>3)one23=1;
			sealone+=next;
			if(sealone==14) one23=2;
		}
		return(i*13+ktone);		
	}

	public static String getKinNumber(String birthday) {
		try {
			SimpleDateFormat df1 = new SimpleDateFormat( "MM/dd/yyyy" );
			Date bdate;
			bdate = df1.parse(birthday);
			DreamSpellUtil.Calc(bdate);
			return String.valueOf(DreamSpellUtil.getKin());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static boolean isPortal(int kin)
	{
		return (kin == 1 || kin == 20 || 
				kin == 22 || kin == 39 ||
				kin == 43 || kin == 58 ||
				kin == 64 || kin == 77 ||
				kin == 85 || kin == 96 ||
				(kin >= 106 && kin <= 115) ||
				(kin == 88 || kin == 69 || kin == 50 || kin == 51 || kin == 72 || kin == 93 ) ||
				(kin == 168 || kin == 189 || kin == 210 || kin == 211 || kin == 192 || kin == 173 ) ||
				(kin >= 146 && kin <= 155) ||
				kin == 165 || kin == 176 ||
				kin == 184 || kin == 197 ||
				kin == 203 || kin == 218 ||
				kin == 222 || kin == 239 ||
				kin == 241 || kin == 260 );
	}
}

