// Default.aspx.cs created with MonoDevelop
// User: damien at 2:38 PM 10/13/2008
//
// Copyright Skull Squadron, All Rights Reserved.

using System;
using System.Web;
using System.Web.UI;

namespace DreamSpell
{
	
	
	public partial class Default : System.Web.UI.Page
	{
		
		public virtual void button1Clicked(object sender, EventArgs args)
		{
			button1.Text = "You clicked me";
		}
	}
}
