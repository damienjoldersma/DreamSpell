package org.joldersma.damien.DreamSpell;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.res.Resources;
import android.util.Log;

public class DreamSpellUtil {

	public static final String TAG = "DreamSpell";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

//		System.out.println("Hello Dream World!");
		
		DreamSpellUtil.Calc();
		
//		System.out.println("Name: " + DreamSpellUtil.GetName(DreamSpellUtil.getSeal()));
		
//		System.out.println("Tone: " + DreamSpellUtil.getTone() + " " + 
//				DreamSpellUtil.getTones()[DreamSpellUtil.getTone()][0] + " " +
//				DreamSpellUtil.GetToneAction(DreamSpellUtil.getTone()) + " " +
//				DreamSpellUtil.GetToneEssence(DreamSpellUtil.getTone()) + " " +
//				DreamSpellUtil.GetTonePower(DreamSpellUtil.getTone())
//		);
//		
//		System.out.println("Seal: " + DreamSpellUtil.getSeal() + " " + 
//				DreamSpellUtil.getSeals()[DreamSpellUtil.getSeal()][0] + " " +
//				DreamSpellUtil.GetAction() + " " +
//				DreamSpellUtil.GetEssence() + " " +
//				DreamSpellUtil.GetPower()
//		);
		
//		System.out.println("Analog: " + DreamSpellUtil.getAnalog());
//		System.out.println("Occult: " + DreamSpellUtil.getOccult());
//		System.out.println("Antipode: " + DreamSpellUtil.getAntipode());
//		System.out.println("Guide: " + DreamSpellUtil.getGuide());
		
		//System.out.println("Year: " + DreamSpellUtil.get);
		
//		System.out.println("CurrentDate: " + DreamSpellUtil.getCurrentDate());
	}

	static double y,x,rem;
	static int cont,i,loopstart,daydist;
	static int byear = 0, bmonth = 0, bday = 0;
	static int yseal = 0, ytone = 0, magseal = 0, kin = 0;
	static int tone = 0, seal = 0, guide = 0, antipode = 0, occult = 0, analog = 0;
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

	static Date currentDate;
	
	static int[] gmonth = new int[] { 0,31,28,31,30,31,30,6,31,30,31,30,31 };
	
	//static String [] seals = new String[] { "Unknown","Red Dragon","White Wind","Blue Night","Yellow Seed","Red Serpent","White World-Bridger","Blue Hand","Yellow Star","Red Moon","White Dog","Blue Monkey","Yellow Human","Red Skywalker","White Wizard","Blue Eagle","Yellow Warrior","Red Earth","White Mirror","Blue Storm","Yellow Sun" }; 
		
	static String[][] tones = new String[][]
	{ 
		// Tone, Power, Action, Essence, Daily Meditation
		{"Unknown"," ", " "," "," "},
		{"Magnetic","Unify", "Purpose","Attraction","What is this wavespell's Goal?"},
		{"Lunar","Polarize", "Challenge","Stabilizing","What are the Obstacles for this wavespell's goal?"},
		{"Electric","Activate", "Service","Bonding","How can this wavespell's goal be Obtained?"},
		{"Self-Existing","Define", "Form","Measuring","What is the form of the action to obtain the wavespell goal?"},
		{"Overtone","Empower", "Radiance","Command","Gather Resources."},
		{"Rythmic","Organize", "Equality","Balance","Administer Challenge."},
		{"Resonant","Channel", "Attunement","Inspiring","Attune Service to action."},
		{"Galactic","Harmonize", "Integrity","Modeling","Action Attains form."},
		{"Solar","Pulse", "Intention","Realizing","Action set in Motion."},
		{"Planetary","Perfect", "Manifestation","Producing","Action and Challenge are meet."},
		{"Spectral","Dissolve", "Liberation","Release","Action dissolves service."},
		{"Crystal","Dedicate", "Cooperation","Universalize","Round Table meets."},
		{"Cosmic","Endure", "Presence","Transcend","Return to Magnetic (tone 1)."} 
	};

	static String[][] seals = new String[][] 
	{ 			
		// Seal, Name, Action, Essence, Power
		{"Unknown"," "," "," "," "},
		{"Red Dragon","IMIX","Nurtures","Being","Birth"}, 
		{"White Wind","IK","Communicates","Breath","Spirit"}, 
		{"Blue Night","AKBAL","Dreams","Intuition","Abundance"}, 
		{"Yellow Seed","KAN","Targets","Awareness","Flowering (ideas)"}, 
		{"Red Serpent","CHICCHAN","Survives","Awareness","Life-force (instinct)"}, 
		{"White World-Bridger","CIMI","Equalizes","Opportunity","Death (span dimensions)"},
		{"Blue Hand","MANIK","Knows","Healing","Accomplishment (heals)"}, 
		{"Yellow Star","LAMAT","Beautifies","Art","Elegance"}, 
		{"Red Moon","MULAC","Purifies","Flow","Universal Water"}, 
		{"White Dog","OC","Loves","Loyalty","Heart (truth)"}, 
		{"Blue Monkey","CHUEN","Plays","Illusion","Magic"},
		{"Yellow Human","EB","Influences","Wisdom","Free Will"}, 
		{"Red Skywalker","BEN","Explores","Wisdom","Space"}, 
		{"White Wizard","IX","Enchants","Receptivity","Timelessness"}, 
		{"Blue Eagle","MEN","Creates","Mind","Vision"}, 
		{"Yellow Warrior","CIB","Questions","Fearlessness","Intelligence"},
		{"Red Earth","CABAN","Evolves","Synchronicity","Navigation"}, 
		{"White Mirror","ETZNAB","Reflects","Order","Endlessness"}, 
		{"Blue Storm","CAUAC","Catalyzes","Energy","Self-Generation"}, 
		{"Yellow Sun","AHAU","Enlightens","Life","Universal Fire"}
	};		
		
	static String[] oracleTextDefinition = new String[] 
	{                                                                                                  
		"The seal of the day is the basis of life destiny, with power of the solar tribe.",
		"The seal of the year is the basis of life destiny, with the power of the solar tribe.",
		"The seal for the Guide of the day modifies the oracle reading in Outcome.",
		"The seal for the Anitpode of the day modifies the oracle reading with Challenging power (strengthening memory, reconstruction).", 
		"The seal for the Occult of the day modifies the oracle reading with Hidden power (the unexpected).",
		"The seal for the Analog of the day modifies the oracle reading with Like-Minded power (galactic-solar planetary power).",
		"The seal for the Wavespell sets the emphasis of the thirteen day, thirteen tone cycle."
	};
	
	
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
		 "I am guided by the power of %s\n",
		 affirmation_tones[getTone()-1][1],affirmation_glyphs[getSeal()-1][3],
		 affirmation_tones[getTone()-1][3],affirmation_glyphs[getSeal()-1][5],
		 affirmation_glyphs[getSeal()-1][6],affirmation_glyphs[getSeal()-1][4],
		 affirmation_tones[getTone()-1][0],affirmation_tones[getTone()-1][2],
		 guidePower);
	}
	// TONE,POWER,FUNCTION,ACTION
	// GLYPH,MAYAN NAME,COLOR,ACTION,POWER,FUNCTION
	
	public static String getSealDefinition(int s)
	{
		return String.format("%s (%s) %s and emphasizes %s",
				seals[s][0],seals[s][1],seals[s][2],seals[s][4]);
	}
	
	public static String getToneDefinition(int t)
	{
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
		//Date d = new Date(2008,7,26);
		//System.out.println("Day Out Of Time: " + d.ToString("d"));
		//System.out.println("Day Out Of Time day of year: " + d.DayOfYear);				
		
		setCurrentDate(date);
		
////		System.out.println("-- Year Seal: " + DreamSpellUtil.CalcYearSeal());
////		System.out.println("-- Year Tone: " + DreamSpellUtil.CalcYearTone());
////		System.out.println("-- Distance From Day Out Of Time: " + DreamSpellUtil.CalcDistanceFromDayOutOfTime());
////		System.out.println("-- Year Seal: " + DreamSpellUtil.CalcSeal());
////		System.out.println("-- Year Tone: " + DreamSpellUtil.CalcTone());
		
		DreamSpellUtil.CalcYearSeal();
		DreamSpellUtil.CalcYearTone();
		DreamSpellUtil.CalcDistanceFromDayOutOfTime();
		DreamSpellUtil.CalcSeal();
		DreamSpellUtil.CalcTone();
		
		/*DreamSpellUtil.CalcYearSeal();
		DreamSpellUtil.CalcYearTone();
		DreamSpellUtil.CalcDistanceFromDayOutOfTime();
		DreamSpellUtil.CalcSeal();
		DreamSpellUtil.CalcTone();
		*/
		
		DreamSpellUtil.CalcOracle();
//		System.out.println("-- Guide: " + DreamSpellUtil.getGuide());
//		System.out.println("-- Antiopde: " + DreamSpellUtil.getAntipode());
//		System.out.println("-- Analog: " + DreamSpellUtil.getAnalog());
//		System.out.println("-- Occult: " + DreamSpellUtil.getOccult());
//		System.out.println("-- Kin: " + DreamSpellUtil.getKin());			
	}
	
	public static int CalcYearSeal()
	{
//		System.out.println("DreamSpellUtil.CalcYearSeal: getCurrentDate is " + getCurrentDate());
//		System.out.println("DreamSpellUtil.CalcYearSeal: getCurrentDate.getYear is " + getCurrentDate());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentDate());
		
		byear = cal.get(GregorianCalendar.YEAR);
		bmonth = cal.get(GregorianCalendar.MONTH)+1;
		bday = cal.get(GregorianCalendar.DAY_OF_MONTH);
		
//		System.out.println("byear: " + byear);
//		System.out.println("bmonth: " + bmonth);
//		System.out.println("bday: " + bday);			
		
		//     calc year seal 
		if((bmonth<7)||((bmonth==7)&&(bday<26)))byear--;      
//		System.out.println("byear after --: " + byear);
		yseal=4+(15*(calcyseal(byear)-1));
//		System.out.println("yseal: " + yseal);
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
	
	/*
	public static int Tone {
		get {
			return tone;
		}
		set {
			tone = value;
		}
	}
	
	public static int Seal {
		get {
			return seal;
		}
		set {
			seal = value;
		}
	}
	
	public static int Occult {
		get {
			return occult;
		}
		set {
			occult = value;
		}
	}
	
	public static int Kin {
		get {
			return kin;
		}
		set {
			kin = value;
		}
	}
	
	public static int Guide {
		get {
			return guide;
		}
		set {
			guide = value;
		}
	}
	
	public static int Daydist {
		get {
			return daydist;
		}
		set {
			daydist = value;
		}
	}
	
	public static int Antipode {
		get {
			return antipode;
		}
		set {
			antipode = value;
		}
	}
	
	public static int Analog {
		get {
			return analog;
		}
		set {
			analog = value;
		}
	}

	public static DateTime CurrentDate {
		get {
			return currentDate;
		}
		set {
			currentDate = value;
		}
	}

	public static String[,] Seals {
		get {
			return seals;
		}
		set {
			seals = value;
		}
	}

	public static String[,] Tones {
		get {
			return tones;
		}
		set {
			tones = value;
		}
	}
	*/
}
