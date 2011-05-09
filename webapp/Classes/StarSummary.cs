// StarSummary.cs created with MonoDevelop
// User: damien at 6:31 PM 10/28/2008
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
	
	public class StarSummary : Generic
	{		
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(StarSummary));
		
		DreamFolk dreamFriend = null;
		
		public StarSummary()
		{
		}
		
		public override void PostInitialize()
		{
			base.PostInitialize();
			
			//this.DreamFriend = Main.Current.CurrentDreamFriend;
			//this.Record = DreamFriend;
			
			//log.Warn("PostInit with  " + (this.Record == null ? "null" : this.Record.Id.ToString()));
			
			//this.DataBindWidget();
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
