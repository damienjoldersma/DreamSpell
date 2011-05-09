// GalacticSpinCycleView.cs created with MonoDevelop
// User: damien at 1:53 PM 10/20/2008
//
// Copyright Skull Squadron, All Rights Reserved.

using DreamSpell.Model;
using EmergeTk;
using EmergeTk.Model;
using EmergeTk.Widgets.Html;
using Facebook;
using Facebook.Entity;
using Facebook.API;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace DreamSpell
{
	
	
	public class GalacticSpinCycleView : Generic
	{
		
		public GalacticSpinCycleView()
		{
		}
		
		public override void PostInitialize ()
		{
			Glyph glyph;
			GlyphLabel glyphLabel;
			Tone tone;
			ToneLabel toneLabel;
			int kin;			
			
			Pane col;
			Pane row;
			int kinIndex = 1;
			int toneIndex = 1;
			
			Label columnLabel, waveSpellLabel;
			Pane main = this.RootContext.CreateWidget<Pane>(this);
			main.AppendClass("main");
			main.AppendClass("clearfix");
			
			for (int colIndex = 0; colIndex <= 13; colIndex++)
			{
				//log.Debug("colIndex is " + colIndex);
			
				
				col = this.RootContext.CreateWidget<Pane>(main);	
				col.AppendClass("column");
				col.AppendClass("col-" + colIndex);
							
				for (int rowIndex = 0; rowIndex <= 20; rowIndex++)
				{
					//log.Debug("rowIndex is " + rowIndex);
					
					row = this.RootContext.CreateWidget<Pane>(col);
					row.AppendClass("row");
					row.AppendClass("row-" + colIndex);
					
					if ( colIndex == 0 )
					{						
						if ( rowIndex > 0 )
						{
							glyph = new Glyph();
							kin = kinIndex++;
//							if ( kin == Main.Current.Now.Kin )
//								row.AppendClass("kin-today");
							glyph.Name = "Glyph " + kin;
							glyph.Image = new ImageRecord();
							glyph.Image.Url = "/Images/glyph" + kin + ".gif";
							glyphLabel = this.RootContext.CreateWidget<GlyphLabel>(row);
							glyphLabel.Text = "<img src=\"" + glyph.Image.Url + "\" class=\"glyph\">";
							//log.Debug("Made and added glpyh " + glyph.Name);
						}
					}
					else if ( rowIndex == 0 )
					{
						columnLabel = this.RootContext.CreateWidget<Label>(row);
						columnLabel.Text = "" + colIndex;
						columnLabel.AppendClass("column-label");
						if ( colIndex == 1 ) kinIndex = 1;
						
					}
					else
					{
						tone = new Tone();
						toneIndex = NextToneNumber();
						
						kin = kinIndex++;
						if ( kin == Main.Current.Now.Kin )
								row.AppendClass("kin-today");
						
						//tone.Name = "Tone " + toneIndex;
						tone.Image = new ImageRecord();
						tone.Image.Url = "/Images/tone" + toneIndex + ".gif";
						toneLabel = this.RootContext.CreateWidget<ToneLabel>(row);
						toneLabel.Text = "<img src=\"" + tone.Image.Url + "\" class=\"tone\">";
						//log.Debug("Made and added tone " + tone.Name);
						
						waveSpellLabel = this.RootContext.CreateWidget<Label>(row);
						waveSpellLabel.Text = "spell: " + NextWaveSpellNumber();
						waveSpellLabel.AppendClass("wavespell-label");
						
						waveSpellLabel = this.RootContext.CreateWidget<Label>(row);
						waveSpellLabel.Text = "kin: " + kin;
						waveSpellLabel.AppendClass("kin-label");
						
						//if ( toneIndex == Main.Current.Now.Tone.Number )
						//		row.AppendClass("tone-today");
						
						if ( kin == 1 || kin == 20 || 
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
						     kin == 241 || kin == 260 )
							row.AppendClass("portal");
						
						
					}
				}
			}
			
			
			
//			
//			
//			for (int i = 1; i <= 20; i++)
//			{
//				log.Debug("i is " + i);
//				glyph = new Glyph();
//				glyph.Name = "Glyph " + i;
//				glyph.Image = new ImageRecord();
//				glyph.Image.Url = "/Images/glyph" + i + ".gif";
//				glyphLabel = this.RootContext.CreateWidget<GlyphLabel>(this);
//				glyphLabel.Text = "<img src=\"" + glyph.Image.Url + "\">";
//				log.Debug("Made and added glpyh " + glyph.Name);
//			}		
			
//			for (int i = 1; i <= 13; i++)
//			{
//				log.Debug("i is " + i);
//				tone = new Tone();
//				//tone.Name = "Tone " + i;
//				tone.Image = new ImageRecord();
//				tone.Image.Url = "/Images/tone" + i + ".gif";
//				toneLabel = this.RootContext.CreateWidget<ToneLabel>(this);
//				toneLabel.Text = "<img src=\"" + tone.Image.Url + "\" >";
//				log.Debug("Made and added tone " + tone.Name);
//			}		
        }	
		
		public int kinNumber = 0;
		public int NextKinNumber()
		{
			if ( kinNumber++ >= 20 )
				kinNumber = 1;
			return kinNumber;
		}

		
		public int toneNumber = 0;
		public int NextToneNumber()
		{
			if ( toneNumber++ >= 13 )
				toneNumber = 1;
			return toneNumber;
		}
		
		public int waveSpellCount = 0;
		public int waveSpellNumber = 1;
		public int NextWaveSpellNumber()
		{
			if ( waveSpellCount++ >= 13 )
			{
				waveSpellCount = 1;
				waveSpellNumber++;
			}
			return waveSpellNumber;
		}
	}
}
