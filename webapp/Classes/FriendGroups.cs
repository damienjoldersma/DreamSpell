// FriendGroups.cs created with MonoDevelop
// User: damien at 12:48 PM 11/2/2008
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
	
	public class FriendGroups : Generic
	{
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(FriendGroups));
		
		private DreamFolk dreamFriend;
	
		public FriendGroups()
		{
		}
		
		public override void Initialize ()
		{
			base.Initialize ();
			
			
		}

		
		public override void PostInitialize ()
		{
			base.PostInitialize ();
			
			log.Debug("PostInit BEGIN");
			
			Pane tones = this.Find<Pane>("Tones");
			Pane seals = this.Find<Pane>("Seals");
			Pane guides = this.Find<Pane>("Guides");
			Pane allies = this.Find<Pane>("Allies");
			Pane secrets = this.Find<Pane>("Secret");
			Pane focus = this.Find<Pane>("Focus");

			DreamFriend = (DreamFolk)this.Record;
			
			Repeater<DreamFolk> toneList = Find<Repeater<DreamFolk>>("toneList");
			if ( toneList != null && DreamFriend != null  && DreamFriend.Tones.Count > 0)
			{	
				tones.AppendClass("clearfix");
				toneList.DataSource = DreamFriend.Tones;
				toneList.DataBind();				
			}		
			else
				tones.AppendClass("none");

			Repeater<DreamFolk> sealList = Find<Repeater<DreamFolk>>("sealList");
			if ( sealList != null && DreamFriend != null  && DreamFriend.Seals.Count > 0)
			{	
				seals.AppendClass("clearfix");
				sealList.DataSource = DreamFriend.Seals;
				sealList.DataBind();				
			}		
			else
				seals.AppendClass("none");

			
			Repeater<DreamFolk> guideList = Find<Repeater<DreamFolk>>("guideList");
			if ( guideList != null && DreamFriend != null  && DreamFriend.Guides.Count > 0)
			{	
				guides.AppendClass("clearfix");
				guideList.DataSource = DreamFriend.Guides;
				guideList.DataBind();				
			}		
			else
				guides.AppendClass("none");
			
			Repeater<DreamFolk> alliesList = Find<Repeater<DreamFolk>>("alliesList");
			if ( alliesList != null && DreamFriend != null && DreamFriend.Allies.Count > 0)
			{						
				allies.AppendClass("clearfix");
				alliesList.DataSource = DreamFriend.Allies;
				alliesList.DataBind();				
			}		
			else
				allies.AppendClass("none");
			
			Repeater<DreamFolk> secretList = Find<Repeater<DreamFolk>>("secretList");
			if ( secretList != null && DreamFriend != null && DreamFriend.Occults.Count > 0)
			{						
				secrets.AppendClass("clearfix");
				secretList.DataSource = DreamFriend.Occults;
				secretList.DataBind();
			}		
			else
				secrets.AppendClass("none");
			
			Repeater<DreamFolk> focusList = Find<Repeater<DreamFolk>>("focusList");
			if ( focusList != null && DreamFriend != null && DreamFriend.FocusList.Count > 0)
			{		
				focus.AppendClass("clearfix");
				focusList.DataSource = DreamFriend.FocusList;
				focusList.DataBind();
			}		
			else
				focus.AppendClass("none");
			
			log.Debug("PostInit END");
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
