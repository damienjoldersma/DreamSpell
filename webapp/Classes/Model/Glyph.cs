// Glyph.cs created with MonoDevelop
// User: damien at 10:14 AM 10/14/2008
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
	public class Glyph : AbstractRecord
	{
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(Glyph));

		string name,action,essence,power;
		ImageRecord image;
		int number = 0;
		
		public Glyph()
		{
		}
		
		public Glyph(int n)
		{
			this.Number = n;
		}
		
		public static void InitDataBase()
		{
			IRecordList<Glyph> glyphs = DataProvider.LoadList<Glyph>();
			if ( glyphs.Count == 0 )
			{
				Glyph glyph;
				for (int i = 0; i <= 20; i++)
				{
					glyph = new Glyph(i);
					glyph.Save();
				}
			}
		}
		
		public string Name {
			get {
				return DreamSpellUtil.Seals[Number,0];
			}
			set {
				name = value;
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
		
		public string Action {
			get {
				return DreamSpellUtil.GetAction(this);
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
		
		public override string ToString()
		{
			return Convert.ToString(Number);
		}
		
		
	}
}
