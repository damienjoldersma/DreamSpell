// Tone.cs created with MonoDevelop
// User: damien at 10:20 AM 10/14/2008
//
// Copyright Skull Squadron, All Rights Reserved.

using System;
using EmergeTk;
using EmergeTk.Model;
using EmergeTk.Widgets.Html;
using EmergeTk.Model.Security;
using DreamSpell;

namespace DreamSpell.Model
{
	public class Tone : AbstractRecord
	{
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(Tone));		
		
		int number = 0;
		string name,action,essence,power;
		ImageRecord image;				
		
		public Tone()
		{
		}
		
		public Tone(int n)
		{
			this.Number = n;
		}

		
		public static void InitDataBase()
		{
			IRecordList<Tone> tones = DataProvider.LoadList<Tone>();
			if ( tones.Count == 0 )
			{
				Tone tone;
				for (int i = 0; i <= 13; i++)
				{
					tone = new Tone(i);
					tone.Save();
				}
			}
		}
		
		public string Name {
			get {
				return DreamSpellUtil.Tones[Number,0];
			}		
			set
			{
				this.name = value;
			}
		}
		
		public string Action {
			get {
				return DreamSpellUtil.GetToneAction(Number);
			}	
			set
			{
				this.action = value;
			}
		}
		
		public string Essence {
			get {
				return DreamSpellUtil.GetEssence(this);
			}		
			set
			{
				this.essence = value;
			}
		}
		
		public string Power {
			get {
				return DreamSpellUtil.GetPower(this);
			}		
			set
			{
				this.power = value;
			}
		}
		
		public ImageRecord Image {
			get {
				return image;
			}
			set {
				image = value;
			}
		}

		public int Number {
			get {
				return number;
			}
			set {
				number = value;
			}
		}
		
		public override string ToString()
		{
			return Convert.ToString(Number);
		}
	}
}
