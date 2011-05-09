// StarDetail.cs created with MonoDevelop
// User: damien at 5:46 PM 10/27/2008
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
	
	public class StarDetail : Generic
	{	
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(StarDetail));

		DreamFolk dreamFriend = null;
		DateTime date = DateTime.Now;
		Main main;
				
		public StarDetail()
		{
			main = Main.Current;		
		}
		
		
		public override void PostInitialize()
		{
			base.PostInitialize();
			
			//log.Warn("PostInit START");
			
		
			
//			if ( DreamFriend == null )
//			{
//				log.Warn("StartDetail DreamFriend is null - id " + this.Id, Main.Current.CurrentDreamFriend.Name);
//				if ( this.Id == "Today" )
//				{
//					log.Warn("Doing Today!");
//					this.DreamFriend = new DreamFolk(DateTime.Now);
//				}
//				else if ( this.Id == "Me" && Main.Current.CurrentDreamFriend != null )
//				{
//					log.Warn("CurrentDreamFriend isn't null");
//					this.DreamFriend = Main.Current.CurrentDreamFriend;
//				}
//				else if ( Date != null )
//				{
//					log.Warn("Doing stored Date! " + this.Id, Main.Current.CurrentDreamFriend);
//					this.DreamFriend = new DreamFolk(Date);
//				}
//				else
//				{
//					log.Warn("creating new dream friend from datetime now");
//					this.DreamFriend = new DreamFolk(DateTime.Now);
//				}
//				
//				this.Record = DreamFriend;
//				this.DataBindWidget();
//			}
//			else
//				log.Error("Wow dreamfriend isn't null!");

			//log.Warn("PostInit END");
		}
		
		public DreamFolk DreamFriend {
			get {
				
				return dreamFriend;
			}
			set {				
				dreamFriend = value;								
				log.Warn("Bound DreamFriend to " + (dreamFriend == null ? "null" : dreamFriend.GetName()));								
			}
		}

		public DateTime Date {
			get {
				return date;
			}
			set {
				date = value;
			}
		}
	}
}
