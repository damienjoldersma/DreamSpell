// GlyphLabel.cs created with MonoDevelop
// User: damien at 10:15 AM 10/14/2008
//
// Copyright Skull Squadron, All Rights Reserved.

using System;
using EmergeTk;
using EmergeTk.Model;
using EmergeTk.Widgets.Html;
using EmergeTk.Model.Security;
using DreamSpell.Model;

namespace DreamSpell
{
	public class GlyphLabel : Label
	{
		
		public GlyphLabel()
		{
		}
		
		public GlyphLabel(Glyph glyph)
		{
			if ( glyph != null )
				this.Text = glyph.Name;
		}
	}
}
