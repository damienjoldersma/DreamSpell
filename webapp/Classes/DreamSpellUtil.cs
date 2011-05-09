// DreamSpellUtil.cs created with MonoDevelop
// User: damien at 10:58 AM 10/22/2008
//
// Copyright Skull Squadron, All Rights Reserved.

using DreamSpell.Model;
using EmergeTk;
using System;

namespace DreamSpell
{
	
	public class DreamSpellUtil
	{
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(DreamSpellUtil));
			
		static double y,x,rem;
		static int cont,i,loopstart,daydist;
		static int byear = 0, bmonth = 0, bday = 0;
		static int yseal = 0, ytone = 0, magseal = 0, kin = 0;
		static int tone = 0, seal = 0, guide = 0, antipode = 0, occult = 0, analog = 0;
		static DateTime currentDate;
		
		static int[] gmonth = new int[] { 0,31,28,31,30,31,30,6,31,30,31,30,31 };
		
		//static string [] seals = new string[] { "Unknown","Red Dragon","White Wind","Blue Night","Yellow Seed","Red Serpent","White World-Bridger","Blue Hand","Yellow Star","Red Moon","White Dog","Blue Monkey","Yellow Human","Red Skywalker","White Wizard","Blue Eagle","Yellow Warrior","Red Earth","White Mirror","Blue Storm","Yellow Sun" }; 
		
		static string[,] tones = new string[,] 
		{ 
			// Tone, Power, Action, Essence
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

		static string[,] seals = new string[,] 
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
		
		public DreamSpellUtil()
		{
		}

		public static string GetName()
		{
			return GetName(Seal);
		}
		
		public static string GetName(int s)
		{
			string seal = seals[s,0];
			seal = seal.Replace(" "," " + tones[Tone,0] + " ");
			return seal;
		}

		public static string GetAction()
		{
			return GetToneAction(Tone);
		}		
		
		public static string GetAction(Tone t)
		{
			return GetToneAction(t.Number);
		}
		
		public static string GetAction(Glyph g)
		{
			return GetGlyphAction(g.Number);
		}
		
		public static string GetToneAction(int t)
		{
			string action = tones[t,2];
			return action;
		}
		
		public static string GetGlyphAction(int g)
		{
			string action = seals[g,2];			
			return action;
		}
		
		public static string GetEssence()
		{
			return GetToneEssence(Tone);
		}
		
		public static string GetEssence(Tone t)
		{
			return GetToneEssence(t.Number);
		}
		
		public static string GetEssence(Glyph g)
		{
			return GetGlyphEssence(g.Number);
		}
		
		public static string GetToneEssence(int t)
		{
			string esssence = tones[t,1];
			return esssence;
		}
		
		public static string GetGlyphEssence(int g)
		{
			string esssence = seals[g,3];
			return esssence;
		}
		
		public static string GetPower()
		{
			return GetTonePower(Tone);
		}
		
		public static string GetPower(Tone t)
		{
			return GetTonePower(t.Number);
		}
		
		public static string GetPower(Glyph g)
		{
			return GetGlyphPower(g.Number);
		}
		
		public static string GetTonePower(int t)
		{
			string power = tones[t,3];
			return power;
		}				 

		public static string GetGlyphPower(int g)
		{
			string power = seals[g,4];
			return power;
		}				 
		
		
		
		public static void Calc()
		{
			Calc(DateTime.Now);
		}
		
		public static void Calc(DateTime date)
		{			
			DateTime d = new DateTime(2008,7,26);
			//log.Debug("Day Out Of Time: " + d.ToString("d"));
			//log.Debug("Day Out Of Time day of year: " + d.DayOfYear);				
			
			CurrentDate = date;
			
			//log.Debug("Year Seal: " + DreamSpellUtil.CalcYearSeal());
			//log.Debug("Year Tone: " + DreamSpellUtil.CalcYearTone());
			//log.Debug("Distance From Day Out Of Time: " + DreamSpellUtil.CalcDistanceFromDayOutOfTime());
			//log.Debug("Year Seal: " + DreamSpellUtil.CalcSeal());
			//log.Debug("Year Tone: " + DreamSpellUtil.CalcTone());
			
			DreamSpellUtil.CalcYearSeal();
			DreamSpellUtil.CalcYearTone();
			DreamSpellUtil.CalcDistanceFromDayOutOfTime();
			DreamSpellUtil.CalcSeal();
			DreamSpellUtil.CalcTone();
			
			DreamSpellUtil.CalcOracle();
			//log.Debug("Guide: " + DreamSpellUtil.Guide);
			//log.Debug("Antiopde: " + DreamSpellUtil.Antipode);
			//log.Debug("Analog: " + DreamSpellUtil.Analog);
			//log.Debug("Occult: " + DreamSpellUtil.Occult);
			//log.Debug("Kin: " + DreamSpellUtil.Kin);			
		}
		
		public static int CalcYearSeal()
		{
			byear = CurrentDate.Year;
			bmonth = CurrentDate.Month;
			bday = CurrentDate.Day;
			
			//log.Debug("byear: " + byear);
			//log.Debug("bmonth: " + bmonth);
			//log.Debug("bday: " + bday);			
			
			//     calc year seal 
			if((bmonth<7)||((bmonth==7)&&(bday<26)))byear--;      
			//log.Debug("byear after --: " + byear);
			yseal=4+(15*(calcyseal(byear)-1));
			//log.Debug("yseal: " + yseal);
			if(yseal==34)yseal=14;
			if(yseal==49)yseal=9;
			
			return yseal;
		}
		
		public static int calcyseal(int bthyear)
		{
			
			int abs = (Math.Abs(1965-bthyear));
			y=abs/4.00;
			y=y-Math.Floor(y);
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
			y=Math.Abs(byear-1967);
			x=y/13; 
			rem=x-Math.Floor(x);
			//z ytone=Math.round((rem*13)+1); 
			rem=Math.Floor((rem*13)+.5); 
			ytone=Convert.ToInt32( Math.Floor(rem+1) );
			
			if(byear<1967)
			{
				ytone=Convert.ToInt32(Math.Floor(Math.Floor(.99999+(rem/13.00))*15-(rem+1)));
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
			Tone=ytone;
			for(i=1;i<=daydist;i++)
			{
				Tone++;
				if(Tone>13)Tone=1;
			}
			return Tone;
		}
		
		public static void CalcOracle()
		{
			// calc oracle
			if(Seal>=Tone) magseal=Seal-Tone+1;
			else magseal=20+Seal-Tone+1;
			guide=getguide(magseal,Tone);
			antipode=Seal+10;
			if(antipode>20) antipode=Seal-10;
			antipode=antipode;
			analog=19-Seal;	if(analog<=0)analog=analog+20;
			Occult=21-Seal;
			Kin=getkin(magseal,Tone);			
		}
		
		public static int getguide(int sealone, int mtone)
		{
			double one23,next;
			one23 =1;
			if(sealone<=7)one23++;
			if(sealone<=14)one23++;
			for(i=1;i<mtone;i++){
				next=(20*(Math.Floor(one23*1/3))-7);
				one23++;
				if(one23>3)one23=1;
				sealone+=Convert.ToInt32(next);
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
				next=(20*(Math.Floor(one23*1/3))-7);
				one23++;
				if(one23>3)one23=1;
				sealone+=next;
				if(sealone==14) one23=2;
			}
			return(i*13+ktone);		
		}
		
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

		public static string[,] Seals {
			get {
				return seals;
			}
			set {
				seals = value;
			}
		}

		public static string[,] Tones {
			get {
				return tones;
			}
			set {
				tones = value;
			}
		}
	}
}
