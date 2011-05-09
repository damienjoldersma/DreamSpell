// StarInfo.cs created with MonoDevelop
// User: damien at 10:15 AM 11/3/2008
//
// Copyright Skull Squadron, All Rights Reserved.
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using EmergeTk;
using EmergeTk.Model;
using EmergeTk.Widgets.Html;
using Facebook;
using Facebook.Entity;
using Facebook.API;
using DreamSpell.Model;

namespace DreamSpell
{
	
	public class StarInfo : Generic
	{
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(StarInfo));
		
		private DreamFolk dreamFriend;
		
		
		public StarInfo()
		{
			
		}
		
		public override void PostInitialize ()
		{
			base.PostInitialize ();
		}
					
		public DreamFolk DreamFriend {
			get {
				return dreamFriend;
			}
			set {
				dreamFriend = value;
			}
		}
	}
}
