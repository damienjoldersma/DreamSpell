

using DreamSpell;
using DreamSpell.Model;
using EmergeTk;
using EmergeTk.Model;
using EmergeTk.Model.Search;
using EmergeTk.Model.Security;
using EmergeTk.Widgets.Html;
using Facebook;
using Facebook.API;
using Facebook.Entity;
using System;
using System.Collections;
using System.Collections.ObjectModel;
using System.Data;
using System.Runtime.Remoting.Messaging;
using System.Threading;
using System.Xml;
using System.Web;
using System.Web.SessionState;



	public class Main : Context
	{
		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(Main));

		private string apikey = Setting.GetValueT<string>("apikey","NA");
		private string apisecret = Setting.GetValueT<string>("apisecret","NA");
		FacebookAPI facebookApi;
		string facebook_id = null;
		Facebook.Entity.User fb_user;
		bool isFaceBookSession = false;
		DreamFolk currentDreamFriend;
		static IRecordList<Glyph> glyphs;		
		static IRecordList<Tone> tones;
		DreamFolk now;
		
		public Main()
		{
		  //this.ClientId = "Main";
		}
		
		public override void Initialize ()
		{
			base.Initialize ();

			
		
			log.Debug("**** Going to get new client of main ****",this.ClientId);
			string newClientId = RootContext.GetNewClientId(this);
			log.Debug("**** Going to set new client of main ****",this.ClientId,newClientId);
			this.ClientId = newClientId;
			log.Debug("**** Set new client of main ****",this.ClientId);

			log.Debug("**** INIT TIME ****",this.ClientId);
			log.Warn("**** WARN INIT TIME ****",this.ClientId);
		
			log.Debug("**** apikey:",apikey);
			log.Debug("**** apisecret:",apisecret);

			foreach (var item in HttpContext.Current.Request.Params) {
					log.Debug("request item",item,HttpContext.Current.Request[(string)item]);
				}

			InitializeServices();
									
			if ( Glyphs == null )
				Glyphs = DataProvider.LoadList<Glyph>();
			if ( Tones == null )
				Tones = DataProvider.LoadList<Tone>();			
			
			if ( Glyphs.Count == 0 )
			{
				Glyph.InitDataBase();
				Glyphs = DataProvider.LoadList<Glyph>();
			}
			if ( Tones.Count == 0 )
			{				
				Tone.InitDataBase();
				Tones = DataProvider.LoadList<Tone>();			
			}
			
			log.Debug("Tones",Tones);
			log.Debug("Glyphs",Glyphs);
			
			Now = new DreamFolk();
			Now.FacebookId = "Now";
			Now.Birthday = DateTime.Now;
			log.Warn("Now is " + Now.GetName(),Now,DateTime.Now,Now.Birthday);
			
			Label l = this.Find<Label>();
				if ( l == null ) l = this.CreateWidget<Label>();
			l.Text = DateTime.Now.ToShortDateString();	

			
			// check to see if force records needs to be recalc
			if ( HttpContext.Current.Request["recalc"] == "1" )
			{
				log.Warn("RECALCULATING BIRTHDAYS!");
				//DataHelper helper = new DataHelper();
				//new Thread (new ThreadStart (helper.Recalc)).Start();
				ThreadPool.QueueUserWorkItem(new WaitCallback(Recalc));
			}
			else
				log.Warn("skppping recalc",HttpContext.Current.Request["recalc"]);
			
//			DreamSpellUtil.Calc();
//			log.Debug("Year Seal: " + DreamSpellUtil.CalcYearSeal());
//			log.Debug("Year Tone: " + DreamSpellUtil.CalcYearTone());
//			log.Debug("Distance From Day Out Of Time: " + DreamSpellUtil.CalcDistanceFromDayOutOfTime());
//			log.Debug("Year Seal: " + DreamSpellUtil.CalcSeal());
//			log.Debug("Year Tone: " + DreamSpellUtil.CalcTone());
//			DreamSpellUtil.CalcOracle();
//			log.Debug("Guide: " + DreamSpellUtil.Guide);
//			log.Debug("Antiopde: " + DreamSpellUtil.Antipode);
//			log.Debug("Analog: " + DreamSpellUtil.Analog);
//			log.Debug("Occult: " + DreamSpellUtil.Occult);
//			log.Debug("Kin: " + DreamSpellUtil.Kin);
			
			log.Debug("This HttpContext.Request",HttpContext.Current.Request);
						
			bool isAdded = HttpContext.Current.Request["fb_sig_added"] == "1";
			
			string owner_fb_id = HttpContext.Current.Request["owner_fb_id"];
			
			string sessionKey = HttpContext.Current.Request["fb_sig_session_key"];
			
			string userId = HttpContext.Current.Request["fb_sig_user"];

			log.Debug("isAdded",isAdded);
			log.Debug("owner_fb_id",owner_fb_id);
			log.Debug("sessionKey",sessionKey);
			log.Debug("userId",userId);
			
			if ( userId != "775949236") 
			{
				log.Debug("User is not me", userId);
				//return;
			}
			else
			{
				log.Debug("User is me!");
			}
			//return;
			log.Debug("Checking for nullOrEmpty sessionKey",sessionKey);
			
			if( string.IsNullOrEmpty(sessionKey) )
			{
				log.Debug("No sessionKey");
				//this.HttpContext.Response.Redirect(@"http://www.facebook.com/login.php?api_key=" + apikey + @"&v=1.0");
				//return;
				log.Warn("No facebook session 1");
				
				StarSummary starSummary = Find<StarSummary>("StarHeader");
				if ( starSummary != null )
				{
					starSummary.Visible = false;
				}
				
				Pane me = Find<Pane>("Me");
				//me.AppendClass("none");
				log.Warn("No facebook session 2");
				
				Pane friends = Find<Pane>("Friends");
				//friends.AppendClass("none");
				
				TabPane tabPane = Find<TabPane>();
				if ( tabPane != null )
				{
					foreach (Pane pane in tabPane.tabsByLabels.Values)
					{
						log.Debug("looking at pane: " + pane.Id);
						if ( pane.Id == "Me" || pane.Id == "Friends" )
						{
							pane.AppendClass("none");
						
							//HtmlElement li = pane.FindAncestor
							//li.AppendClass("none");
						
						}
					}
				
					foreach (Object obj in tabPane.tabsByLabels.Keys)
					{
						if ( ! (obj is HtmlElement) )
						{
							log.Debug("obj is not an  htmlElement",obj);
							continue;
						}
					
						HtmlElement li = (HtmlElement)obj;
						log.Debug("looking at key: " + li.Id + " " + li.TagName + " " + li.ClassName);
					
						if ( li.ClassName == "MeTab" || li.ClassName == "FriendTab" )
						{
							li.AppendClass("none");
						
							//HtmlElement li = pane.FindAncestor
							//li.AppendClass("none");
						
						}
						else
							li.AppendClass("no-top-margin");
					}
				}
				
			}
			else
			{
			log.Debug("Working with a facebook session id");
				log.Debug( sessionKey, userId ); 
				
				IsFaceBookSession = true;
				
				if( sessionKey != null && userId != null )
					handleFacebookUser(sessionKey, userId);			
			}

			log.Debug("Moving on");
			if ( CurrentDreamFriend == null )
			{
				log.Debug("Setting CurrentDreamFriend to Now");
				CurrentDreamFriend = Now;
			}
			log.Debug("Going to bind CurrentDreamFriend",CurrentDreamFriend);
			
//			ConfirmButton button = this.RootContext.CreateWidget<ConfirmButton>(this);
//			button.Label = "Recalc";
//			button.OnClick += delegate { ThreadPool.QueueUserWorkItem(new WaitCallback(Recalc)); };
			
			//this.RootContext.CreateWidget<FriendList>(this);
			
			//this.Record = Now;
			this.Record = CurrentDreamFriend;
			this.DataBindWidget(this.Record);
			
			log.Debug("Binded CurrentDreamFriend");
			
			Pane todayPane = this.Find<Pane>("Today");
			Pane mePane = this.Find<Pane>("Me");
			Pane friendsPane = this.Find<Pane>("Friends");
			FriendList friendList = this.Find<FriendList>();
		
			if ( todayPane != null )
			{
				log.Debug("Binding todayPane");
				todayPane.DataBindWidget(Now);
			}
			
			if ( mePane != null )
			{
				log.Debug("Binding mePane");
				mePane.DataBindWidget(CurrentDreamFriend);
			}
			
			if ( friendsPane != null && friendList != null )
			{
				log.Debug("Binding friendsPane");
				friendsPane.DataBindWidget(CurrentDreamFriend);
				log.Debug("Binding friendList");
				friendList.DataBindWidget(CurrentDreamFriend);
			}
			
			log.Debug("Creating starHeaderNow");
			StarSummary starHeaderNow = this.Find<StarSummary>("StarHeaderNow");
			if ( starHeaderNow != null )
			{
				log.Debug("Binding starHeaderNow");
				starHeaderNow.Record = Now;
				starHeaderNow.DataBindWidget();
			}
		}
		
		private void handleFacebookUser(string sessionKey, string userId )
		{
			facebookApi = new FacebookAPI();
			
			facebookApi.IsDesktopApplication = false;
					facebookApi.ApplicationKey = apikey;
					facebookApi.Secret = apisecret;

			facebookApi.SessionKey = sessionKey;
						facebookApi.UserId = userId;

					facebookApi.ConnectToFacebook();
			
			FacebookId = facebookApi.GetLoggedInUser();
			Collection<Facebook.Entity.User> fb_users = facebookApi.GetUserInfo(facebook_id);
			fb_user = fb_users[0];
			log.Debug(fb_user);
			
			IRecordList<DreamFolk> dreamFolk = DataProvider.LoadList<DreamFolk>(new FilterInfo("FacebookId",FacebookId));
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
			else if ( !dreamPerson.Updated )
			{
				log.Warn("Updating dreamPerson");
				dreamPerson.Birthday = dreamPerson.Birthday;
				dreamPerson.Updated = true;
				dreamPerson.Save();				
			}
			
			CurrentDreamFriend = dreamPerson;
			Now.Friends = CurrentDreamFriend.Friends;
				}

		public static Main Current { get { return (Main)Context.Current; } }
		
		public FacebookAPI FacebookApi {
					get {
						return facebookApi;
					}
					set {
						facebookApi = value;
					}
				}

		public string FacebookId {
			get {
				return facebook_id;
			}
			set {
				facebook_id = value;
			}
		}

		public Facebook.Entity.User FbUser {
			get {
				return fb_user;
			}
			set {
				fb_user = value;
			}
		}

		public DreamFolk CurrentDreamFriend {
			get {
				return currentDreamFriend;
			}
			set {
				currentDreamFriend = value;
			}
		}

		public IRecordList<Glyph> Glyphs {
			get {
				return glyphs;
			}
			set {
				glyphs = value;
			}
		}

		public IRecordList<Tone> Tones {
			get {
				return tones;
			}
			set {
				tones = value;
			}
		}

		public DreamFolk Now {
			get {
				return now;
			}
			set {
				now = value;
			}
		}

		public bool IsFaceBookSession {
			get {
				return isFaceBookSession;
			}
			set {
				isFaceBookSession = value;
			}
		}
		
		public void Recalc(object state)
		{
			log.Warn("doing recalc2");
			log.Warn("doing recalc3");
			IRecordList<DreamFolk> dreamFriends = DataProvider.LoadList<DreamFolk>();
			log.Warn("loaded dreamFriends count: ",dreamFriends.Count);
			foreach (DreamFolk dreamFriend in dreamFriends)
			{
				dreamFriend.Birthday = dreamFriend.Birthday;
				dreamFriend.Save();
				log.Debug("recalced and saved " + dreamFriend.FacebookId);
			}
			log.Warn("birthdays recalculated: " + dreamFriends.Count);			
		}

		static bool initialized = false;

								public static void InitializeServices()
								{
												if( ! initialized )
												{
																//this could be a critical section, but it's not the end of the world if it executes twice.
																initialized = true;
																log.Info("Starting up DreamSpell");

																//set the DataProviderFactory.
																/*
				try
																{
																				DataProvider.Factory = DataProviderFactory.Factory;
																}
																catch (Exception e)
																{
																				log.Error (e);
																}
																log.Debug ("set dataprovider factory", DataProvider.Factory);
				*/
	 
													//set the cache provider.
																string cacheProvider = Setting.GetValueT<string>("CacheProvider");
																log.Debug("cacheProvider key : ", cacheProvider );
																if( ! String.IsNullOrEmpty( cacheProvider ) )
																{
																				Type cacheType = TypeLoader.GetType(cacheProvider);
																				if( cacheType != null )
																				{
																								ICacheProvider cacheInstance = (ICacheProvider)Activator.CreateInstance(cacheType);
																								CacheProvider.Instance = cacheInstance;
																								log.Info("Activated provider ", cacheInstance );
																				}
																				else
																				{
																								throw new InvalidOperationException(string.Format("Could not initialize provider '{0}' for service '{1}'", cacheProvider, "CacheProvider" ));
																				}
																}
																else
																				log.Warn("Using default provider for cache.");

																//set the search provider.
																string searchProvider = Setting.GetValueT<string>("SearchProvider");
																log.Debug("searchProvider key : ", searchProvider );
																if( ! String.IsNullOrEmpty( searchProvider ) )
																{
																				Type searchType = TypeLoader.GetType(searchProvider);
																				if( searchType != null )
																				{
																								ISearchServiceProvider searchInstance = (ISearchServiceProvider)Activator.CreateInstance(searchType);
																								EmergeTk.Model.Search.IndexManager.Instance = searchInstance;
																								log.Info("Activated provider ", EmergeTk.Model.Search.IndexManager.Instance );

																								searchInstance.CommitEnabled = Setting.GetValueT<bool>("SearchCommitEnabled",true);
																				}
																				else
																				{
																								throw new InvalidOperationException(string.Format("Could not initialize provider '{0}' for service '{1}'", searchProvider, "SearchProvider" ));
																				}
																}
																else
																				log.Warn("Using default provider for search.");
												}
								}


	}
	
//	public class DataHelper : Context
//	{
//		private static readonly EmergeTkLog log = EmergeTkLogManager.GetLogger(typeof(DataHelper));
//		
//		public void Recalc()
//		{
//			log.Warn("doing recalc");
//			IRecordList<DreamFolk> dreamFriends = DataProvider.LoadList<DreamFolk>();
//			
//			foreach (DreamFolk dreamFriend in dreamFriends)
//			{
//				dreamFriend.Birthday = dreamFriend.Birthday;
//				dreamFriend.Save();
//			}
//			log.Warn("birthdays recalculated: " + dreamFriends.Count);			
//		}
//	}

