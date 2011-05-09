// FriendList.cs created with MonoDevelop
// User: damien at 3:31 PM 10/20/2008
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
	
	
	public class FriendList : Generic
	{
		
		public FriendList()
		{
		}
		
		public override void PostInitialize()
		{
			UnDataBindWidget();
			//this.Record = Main.Current.CurrentDreamFriend;
			//this.DataBindWidget();
			
			FacebookAPI api = Main.Current.FacebookApi;
			if( api == null )
				return;
			Collection<string> friends = api.GetFriendIds();
//			IRecordList<RStuff.Model.Member> members = DataProvider.LoadList<RStuff.Model.Member>(
//            	new FilterInfo( "FacebookId", friends, FilterOperation.In ) );
//            Repeater<RStuff.Model.Member> friendList = Find<Repeater<RStuff.Model.Member>>("friendList");
//            friendList.DataSource = members;
//            friendList.DataBind();
			
			IRecordList<DreamFolk> dreamFolk = DataProvider.LoadList<DreamFolk>(new FilterInfo("FacebookId",Main.Current.FacebookId));
			DreamFolk dreamPerson = null;
			if ( dreamFolk.Count > 0 ) dreamPerson = dreamFolk[0];
			
			if ( dreamPerson == null )
			{
				log.Warn("Creating new dreamPerson");
				dreamPerson = new DreamFolk(Main.Current.FbUser);
				dreamPerson.Save();
			} 
			else if ( dreamPerson.Birthday != Main.Current.FbUser.Birthday )
			{
				dreamPerson.Birthday = (DateTime)Main.Current.FbUser.Birthday;
				dreamPerson.Save();
			}
				
			DreamFolk dreamFriend = null;
			Label label;
			Collection<Facebook.Entity.User> fb_users;
			Pane pane, detailPane;
			bool dreamPersonChanged = false;
			int count = 0;
			int added = 0;
			
			foreach(string friend in friends)
			{
				//if ( count++ > 2 ) break;
				
				dreamFriend = dreamPerson.GetFriend(friend);
				if ( dreamFriend == null )
				{
					fb_users = Main.Current.FacebookApi.GetUserInfo(friend);
					Facebook.Entity.User fb_user = fb_users[0];
					log.Debug(fb_user);
					
					dreamFriend = new DreamFolk(fb_user);
					dreamFriend.Save();
					
					log.Warn("Created new dreamFriend");
					
					dreamPerson.Friends.Add(dreamFriend);
					//dreamPerson.Save();
					dreamPersonChanged = true;
					
					added++;
				}
				else if ( !dreamFriend.Updated )
				{
					log.Warn("Updating dreamFriend");
					dreamFriend.Birthday = dreamFriend.Birthday;
					dreamFriend.Updated = true;
					//dreamFriend.Save();				
				}
				//else
				//	log.Warn("DreamFriend is current and up todate",dreamFriend.FirstName);
				
				
				/*
				pane = this.RootContext.CreateWidget<Pane>(this);
				pane.AppendClass("dream-person");
				
				pane.OnClick += delegate( object sender, ClickEventArgs ea ) 
				{ 
					DreamFolk dreamFolkFriend = (DreamFolk)ea.Source.Record;
					this.RootContext.RawCmd("parent.location='http://www.facebook.com/profile.php?id=" + dreamFolkFriend.FacebookId + "';"); 
				};
				pane.Record = dreamFriend;
				pane.DataBindWidget();
				
				label = this.RootContext.CreateWidget<Label>(pane);
				label.Text = dreamFriend.FirstName;
				label.AppendClass("label-name");
				
				if ( dreamFriend.PictureSmallUrl != null )
				{
					label = this.RootContext.CreateWidget<Label>(pane);
					label.Text = "<img src=\"" + dreamFriend.PictureSmallUrl + "\" class=person>";
				}
				else if ( dreamFriend.PictureUrl != null )
				{
					label = this.RootContext.CreateWidget<Label>(pane);
					label.Text = "<img src=\"" + dreamFriend.PictureUrl + "\" class=person>";
				}
				else if ( dreamFriend.PictureBigUrl != null )
				{
					label = this.RootContext.CreateWidget<Label>(pane);
					label.Text = "<img src=\"" + dreamFriend.PictureBigUrl + "\" class=person>";
				}
				
				DateTime bday = dreamFriend.Birthday;
				//label.Text = date.ToString("d");
				if ( bday.Year == 1900 || bday.Year == DateTime.Now.Year ) 
				{
					detailPane = this.RootContext.CreateWidget<Pane>(pane);
					detailPane.AppendClass("dream-detail");
					
					label = this.RootContext.CreateWidget<Label>(detailPane);
					label.Text = "<img src=\"/Images/tone0.gif\" title=\"Unknown\" class=tone>";
					label.AppendClass("label-tone");
					
					label = this.RootContext.CreateWidget<Label>(detailPane);
					label.Text = "<img src=\"/Images/glyph0.gif\" title=\"Unknown\" class=seal>";
					label.AppendClass("label-glyph");
					
					label = this.RootContext.CreateWidget<Label>(detailPane);
					label.Text = "<a target=\"_parent\" href=\"http://apps.facebook.com/dreamspell/index.aspx?invite=1\" style=\"background-color:#3B5998;border-color:#D9DFEA #0E1F5B #0E1F5B #D9DFEA;border-style:solid;border-width:1px;color:white;margin-right:5px;padding:3px 15px;line-height:24px\">Invite!</a>";
				}
				else
				{
					//DreamSpellUtil.Calc(bday);
					
					detailPane = this.RootContext.CreateWidget<Pane>(pane);
					detailPane.AppendClass("dream-detail");
					
					label = this.RootContext.CreateWidget<Label>(detailPane);
					label.Text = "<img src=\"/Images/tone" + dreamFriend.Tone + ".gif\" title=\""+ dreamFriend.Tone.Name +"\" class=tone>";
					label.AppendClass("label-tone");
					
					label = this.RootContext.CreateWidget<Label>(detailPane);
					label.Text = "<img src=\"/Images/glyph" + dreamFriend.Seal + ".gif\" title=\""+ (dreamFriend.Seal == null ? "Unknown" : dreamFriend.Seal.Name) +"\" class=seal>";
					label.AppendClass("label-glyph");
					
					label = this.RootContext.CreateWidget<Label>(detailPane);
					//label.Text = bday.ToString("d");
					label.Text = dreamFriend.GetName();
					label.AppendClass("label-dreamspell-name");
					
					//break;
					
				}
				*/
				 
			}
			
			if ( dreamPersonChanged )
				dreamPerson.Save();
			log.Debug("Added " + added + " new dream friends");		
			
			Repeater<DreamFolk> friendList = this.Find<Repeater<DreamFolk>>("Friends");
			//if ( friendList != null && Main.Current.CurrentDreamFriend != null && Main.Current.CurrentDreamFriend.Friends.Count > 0)
			if ( friendList != null && dreamFolk.Count > 0)
			{		
				log.Warn("Going to unbind and rebind friendList, friend count " + dreamFolk.Count);
				log.Warn("Going to unbind and rebind friendList, CurrentDreamFriend.friend count " + Main.Current.CurrentDreamFriend.Friends.Count);
				friendList.UnDataBindWidget();
				//friendList.DataSource = Main.Current.CurrentDreamFriend.Friends;
				friendList.DataSource = Main.Current.CurrentDreamFriend.Friends;
				friendList.DataBind();			
				
				
				foreach (DreamFolk d in Main.Current.CurrentDreamFriend.Friends)
					log.Debug(d.FirstName);
			}	
			else
				log.Error("Skipped unbind and rebind of frinedList");
		}
		
	}
}
