var sessionId = "NA";
function setSessionId( id )
{
	sessionId = id;
}

var emergetk = {};
var emerge_post_load;
var $ = document.getElementById;

function buildRoot(url)
{
    return url.substr(0,url.lastIndexOf("/")+1);
}
function getRoot()
{
    return buildRoot(document.location.href);
}

function getSchemeAndDomain()
{
    return location.protocol + '//' + location.host + '/';
}

function loadUri(uri)
{
    dojo.hostenv.loadUri(uri);
}

function SetTitle(t)
{
	document.title = t;
}

function doTwoWayComet(val)
{
	twoWayComet = val;
}

var twoWayComet = false;
var expireEventId, expired, expireState, slidingExpiration = 1000 * 60 * 15;
function Send( id, evt, arg, sync, formNode )
{
	//if( expireEventId )
	//	clearInterval( expireEventId );
	//expireEventId = setTimeout( expire, slidingExpiration );
	showThrobber();
	
	var sepChar = '?';
	if( sendUrl )
		var url = sendUrl;
	else
	{
		url = document.location.href;
		if( url.indexOf('#') > -1 )
	    	url = url.substring(0,url.indexOf('#'));
	}
	
	if( url.indexOf('?') > 0 )
		sepChar = '&';
	
	
	url += sepChar + "widget=" + id + "&event=" + evt + "&sid=" + sessionId;	
	var method = "Get";
	if( arg == 0 || ( arg && arg != "undefined" ) || formNode )
	{
		method = "Post";
	}
	
	var a = new Ajax();
	console.log(a);
	a.responseType = Ajax.JSON;
	a.ondone = doLoad;
}

var lastRE;
var functionMap = {};
var clientVars = {};

onRecv = null;
function doLoad(json)
{ 
	//eval( type );
	for( var i = 0; i > json.length; i++ )
	{
		var msg = json[i];
		switch(msg.type)
		{
		case "set":
			clientVars = msg;
			break;
		case "cmd":
			break;
		default:
			w( functionMap[msg.type], msg );
		}
	}
	hideThrobber();
	if( onRecv ) 
		onRecv(type,evaldObj);
}

function w(type, args)
{
	var widget = new type(args);
	var rendered = false;
	if( widget.r && ! widget.isDragItem ) 
	{
		widget.Render();
		rendered = true;
	}
	return widget;
}

var clicksOnHold = [];

var waitFors = [];

var lastOnChanged = null;

function addWaitFor(o)
{
	waitFors[waitFors.length] = o;
}

function removeWaitFor(o)
{
	for( var i = 0; i < waitFors.length; i++ )
		if( waitFors[i] == o )
			waitFors.splice(i,1);
	if( ! waitFors.length )
	{
		while( clicksOnHold.length )
			clicksOnHold.pop().Click();
	}
}

function findElemChild( pelem, childid )
{
	for( var i = 0; i < pelem.childNodes.length; i++ )
	{
		if( pelem.childNodes[i].id == childid )
			return pelem.childNodes[i];
	}
}

function debugAll()
{
	Send( "root", "Debug", this.arg, null, null );
}


function makeWidget( w, args ) 
{    
	//begin-methods
	w.WireUpEvents = function(wireUpChildren)
	{
		//console.debug( "wiring up events for ", this, this.elem );
		if( this.onReceiveDrop )
	    {
		//dojo.dnd.HtmlDropTarget
			if( dojo.dnd ) {
		  	      this.dropSource = new dojo.dnd.Source(this.elem, {creator:dndCreator,copyOnly:true});
			}
	    }
	    if( this.onClick )
	    {
		dojo.connect( this.elem, "onclick", this, "Click" );
	    }
	    if( this.onBlur )
	    {
		dojo.connect( this.elem, "onblur", this, "Blur" );
	    }
	    
	    if( wireUpChildren && this.widgets )
	    {
	    	for( var k in this.widgets )
	    	{
	    		var child_widget = this.widgets[k];
	    		var child_node = findElemChild(this.elem, child_widget.id );
	    		if( child_widget.elem != child_node )
	    		{    			 
	    			child_widget.elem = child_node;
	    			child_widget.WireUpEvents(true);
	    		}
	    	}
	    }
	};

	w.Debug = function()
	{
		Send( this.id, "Debug", this.arg, null, null );
	};

	w.Click = function (evt)
	{
	//    console.debug("click element:" + this.elem);
	    if( this.elem && this.elem.getScreenCTM )
	    {
		var newPoint = Svg.applyMatrix( {x:evt.clientX,y:evt.clientY}, this.elem.getScreenCTM().inverse() )
		this.arg = Math.round(newPoint.x) + "," + Math.round(newPoint.y);        
	    }
	    if( lastOnChanged != null )
	   	{
	   		lastOnChanged.sync = true;
	    	lastOnChanged.OnChanged();
	    }
	    
	    evt = this.evt?this.evt : "OnClick";
	    if( waitFors.length )
	    {
	    	clicksOnHold.push(this);
	    	return
	    }
	    
	    if( this.clientClickHandler )
	    {
	    	
	    	this.clientClickHandler();
	    }	
	    	       
	    Send( this.id, evt, this.arg, null, this.formNode );
	};

	w.InvokeEvent = function( evt, arg )
	{
		Send( this.id, evt, arg );
	};


	w.OnChanged = function (e)
	{
		Send( this.id, this.evt?this.evt : "OnChanged", this.GetValue(),this.sync );
		lastOnChanged = null;
	};

	w.SetElem = function(args)
	{
	    for( var k in args )
	    {
		var v = args[k];
	       	if( ! this["ea"] )
	       		this.ea = {};
	       	this.ea[k] = v;
		if( k == "className" )
		{
			try
		    { 
		        this.elem.className = v;
		    }
		    catch(e)
		    {
		        this.elem.setAttribute("class",v);
		    }
		}
		//else if( k.substr(0,2) == 'on' )
		//{
		//    dojo.connect( this.elem, k, v );
		//}
		else
			this.elem.setAttribute(k, v);
	    }
	};

	w.SetAttribute = function(attribute,value)
	{
	    if( attribute == "opac" )
			dojo.style(this.elem,"opacity", value);
		else
	    	this.elem.setAttribute( attribute, value );
	};

	w.SetProperty = function(attribute,value)
	{
	   	this[ attribute ] = value;
	}

	w.SetElementProperty = function(attribute,value)
	{
	   	this.elem[ attribute ] = value;
	};

	w.SetStyle = function(pairing)
	{
	    //console.debug(dojo.string.paramString( "setting %{att} to %{val} on %{widget}", {att:attribute,val:value,widget:this.id} ) );
	    for( var k in pairing )
		this.elem.style[k] = pairing[k];
	};

	w.AddWidget = function( child )
	{
	    if( ! this.widgets ) { this.widgets = []; }
	    this.widgets[ this.widgets.length] = child;
	};

	w.Render = function ()
	{
	    if( this.appearEffect )
	    {
		Element.setOpacity( this.elem, 0.0 );
	    }
	    if( ! this.baseElem ) this.baseElem = document.body;
	    if( this.idx >= 0 && this.idx < this.baseElem.childNodes.length )
		{
			this.baseElem.insertBefore( this.elem, this.baseElem.childNodes[ this.idx ] )
		}
		else
		{
			if( this.baseElem.tagName == "SCRIPT" && this.elem.nodeName == "#text" && dojo.isIE)
				this.baseElem.text = this.elem.nodeValue;
			else
				this.baseElem.appendChild( this.elem );
		}
	   // new Effect.Opacity( this.id, {duration:3.0, transition: Effect.Transitions.linear, from: 0.0, to: 0.9 } );	
	
		if( this.appearEffect )
		{
		    if( Effect[ this.appearEffect ] )
			Effect[this.appearEffect]( this.id );
		    else
			eval( this.appearEffect );
		}	
	};
	w.baseRender = w.Render;

	w.Show = function () 
	{
	    //Effect.Appear( this.id );
	    this.elem.style.display = this.oldDisplay ? this.oldDisplay : "";
	};

	w.Hide = function () 
	{
	    this.oldDisplay = this.elem.style.display;
	    this.elem.style.display = "none";
	};

	w.Remove = function () 
	{
	    if( this.elem.parentNode )
		this.elem.parentNode.removeChild( this.elem );
	    else if( this.elem.style )
		this.elem.style.setAttribute("display","none");
	};

	w.Clone = function (cloneArgs)
	{
	    var args = cloneObj(this.args);//?this.Widget( args );
	    args.parent = null;
	    if( cloneArgs ) blend( args, cloneArgs );
	    if( ! cloneArgs || ! cloneArgs.id ) args.id += "_";
	    var newWidget = new this.constructor(args);
	    if( this.widgets )
	    {
			for( var i = 0; i < this.widgets.length; i++ )
			{
				var child = this.widgets[i];
				args = cloneObj(child.args);
				args.parent = newWidget;
				args.id = newWidget.id + "_" + child.id;
				var newChild = new child.constructor(args);
				newChild.baseElem = newWidget.elem;
				newChild.Render();
			}
	    }
	   
	    return newWidget;
	};
	//end-methods

	//begin-constructor
	for( var k in args )
	if( (args[ k ] || args[k] == 0 ) && ! ( args[k] == '' && w[k]) )
	    w[ k ] = args[ k ];
	if( w.elemType )
	{
		var formElem = null;
		if( ! w.elemNS )
		{
		    w.elem = document.createElement(w.elemType);
	 	   if (  args.isForm )
	    		formElem = document.createElement("FORM");
		}
		else
		{
		    w.elem = document.createElementNS(w.elemNS, w.elemType);
		    if (  args.isForm == "true" )
	  	  	formElem = document.createElementNS(w.elemNS,"FORM");
		}
	
		if ( args.isForm ) 
		{ 
			//alert('inserting'); //this.elem.appendChild( formElem )
			//this.p.elem = formElem.appendChild(this.p.elem);
		}

		if( w.opac )
			dojo.style(w.elem,"opacity",this.opac);
		w.elem.id = w.id;
		if( w.cn )
		{
			w.className = w.cn;
		    try
		    {
			w.elem.className = w.cn;
		    }
		    catch(e)
		    {
			w.elem.setAttribute("class",w.cn);
		    }
		}
		if( w.ea ) 
			w.SetElem(w.ea);
		if( w.s ) 
			w.SetStyle( w.s );
		if( w.ep )
			for( var k in w.ep )
					w.elem[k] = w.ep[k];
	}
	w.args = args;
	if( w.p )
	{
		w.p.AddWidget( w );
		w.baseElem = w.p.elem;       	
		if ( args.isForm )
		{
			var parentDiv = w.p.elem.parentNode;        	
		}
	
		if( w.p.OnChildAdded )
		    w.p.OnChildAdded(w);
	}
	else if( ! w.baseElem )
		w.baseElem = document.body;
	if( w.onDelayedMouseOver )
	{
		dojo.connect( this.elem, "onmouseover", w, "OnDelayedMouseOver" );
	}
	w.WireUpEvents();
	if( w.elem )
		w.elem.widget = w;
	window[w.id] = w;
	//end-constructor
}


function cloneObj(source)
{
    var dest = {};
    for( var k in source )
    {
        dest[k] = source[k];
    }
    return dest;
}

function blend(base,addin)
{
    for( var k in addin )
        base[k]=addin[k];
}

etk_i = function (args)
{
    this.elemType = "IMG";
    if( args && args.pl && args.ea.src )
    {
    	this.url = args.ea.src;
    	args.ea.src = "Images/loading.gif";
    	var i = document.createElement('img');
    	i.src = this.url;
    	this.loader = i;
    	dojo.connect(i,"onload",this,"OnLoad");
    }    
    makeWidget(this, args);
    if( args && args.pl )
    {
    	this.oldPadding = this.elem.style.padding;
    	this.elem.style.padding = "10px";
    }

    this.OnLoad = function()
	{
		this.InvokeEvent("OnLoad", this.loader.width + "," + this.loader.height); //this.elem.src = this.url;
		this.elem.style.padding = this.oldPadding;
	};

	this.SetElem_ = function(args)
	{
		if( args.src )
		{
		    //console.debug(this.elem.src);
		    if( this.elem.src == '' )
		        this.elem.src = args.src;
		    else
		    {
		        var newImage = document.createElement("IMG");
		        newImage.src = args.src;
		        _this = this;
		        newImage.widget = this;
		        newImage.onload = function() { alert(this.widget.elem);this.widget.elem.src = this.src; this.widget.FadeShow(2000); }
		    }
		}
		else
		{
		    this.SetElem(args );
		}
	};
}
functionMap["etk_i"] = etk_i;

function SetAttribute( widget, attribute, value )
{
    widget.SetAttribute(attribute,value);	
}

function SetProperty( widget, attribute, value )
{
    widget.SetAttribute(attribute,value);	
}


function bu( args )
{
    this.elemType = "INPUT";
    makeWidget(this, args);
	this.elem.type = "button";
	this.elem.value = this.label;
	this.elem.arg = this.arg;

	this.SetText = function( html )
	{
		//this.textNode.data = newText;
		this.elem.value = html;
	};
}
functionMap["bu"] = bu;


function lb( args )
{
	this.elemType = "A";
	args.onClick = 1;
	makeWidget(this, args);
	if( args.label )
		this.elem.innerHTML = args.label;
	this.elem.setAttribute("href","javascript:void(1)");

	SetText = function(html)
	{
		this.elem.innerHTML = html;
	}
}
functionMap["lb"] = lb;


function HtmlElement( args )
{
	this.elemType = args.tn;
	makeWidget(this, args);
	this.SetText = function( html )
	{
		this.elem.innerHTML = html;
	};
}
functionMap["he"] = HtmlElement;
functionMap["HtmlElement"] = HtmlElement;


function li( args )
{
	this.elemType = "A";
	makeWidget(this, args);
	this.elem.innerHTML = args.label;
	this.elem.setAttribute("href",args.url);
}
functionMap["li"] = li;

//textbox
function tb( args )//id, baseElem, className, defaultValue, isPassword, isDisabled, rows, cols )
{		
	//methods

	this.StartChanged = function(e)
	{
		addWaitFor(this);
		this.OnChanged();
	};

	this.KeyUp = function( e )
	{
		lastOnChanged = this;
		if( this.onEnter )
		{			
			var kC = window.event ? event.keyCode :
			e && e.keyCode ? e.keyCode :
			e && e.which ? e.which : null;
			if (kC && kC == 13 )
			{
				Send( this.id, "OnEnter", this.elem.value );
			}
		}
	};

	this.SetText = function( newText, width )
	{
		if( this.elemType != "DIV" )
			this.elem.value = newText;
		else if ( this.elem )
			this.elem.innerHTML = newText ? newText : "";
		if( width ) this.elem.style.width = width;		
	};


	//constructor
    this.elemType = "INPUT";
    
    if( args.isRich || args.isInline || args.isCodeView )
    {
        this.elemType = "DIV";        
    }

	if( ( args.rows > 1 && ! args.isInline ) && ! args.isCodeView )
	{
	    this.elemType = "TEXTAREA";
	}

	makeWidget(this, args);
	
	if( args.isRich || args.isInline || args.isCodeView )
	{
	    this.elem.innerHTML = args.defaultValue;
	}
	else if( this.elemType == "TEXTAREA" )
	{
        this.elem.rows = args.rows;
		this.elem.cols = args.cols;
	}
	else
	{
		if( args.cols )
			this.elem.size = args.cols;
		if( args.isPassword )
			this.elem.type = "password";
	}
	
	if( this.onKeyUp )
	{
	    this.RollingDelayChanged = function(evt) { 
	        this.SetRollingDelay( "onchangedTimer", this.OnChanged, 300 ); }
		
	    dojo.connect( this.elem, "onkeyup", this, "RollingDelayChanged" );
	}
	dojo.connect( this.elem, "onkeyup", this, "KeyUp" );
	//dojo.connect( this.elem, "onblur", function(e){console.debug("onblur:" + e);} );
	if( args.isDisabled )
		this.elem.disabled = true;
	//this.elem.className = args.className;
	this.elem.value = args.defaultValue ? args.defaultValue : '';
	this.elem.id = args.id;
	this.elem.name = args.name;
	_this = this;
	dojo.connect( this.elem, "onchange", this, "StartChanged" );
	this.SetValue = this.SetText;
	this.GetValue = function() { 
		if( args.isRich || args.isInline || args.isSpinner ) 
			return this.editor.value;		
		else return this.elem.value; };
}
functionMap["tb"] = tb;


function si( args )
{
    this.elemType = "INPUT";
    makeWidget(this, args);
    dojo.connect( this.elem, "onchange",this, "OnChanged" );
	this.elem.arg = this.arg;
	this.GetValue = function()
	{
	    return this.elem.checked;
	};

	this.SetValue = function(v)
	{
		if( v ) this.elem.checked = true;
		else this.elem.checked = null;
	};
}
functionMap["si"] = si;

//label
function l( args )
{
    if( args.html && ( args.html.indexOf('<') > -1 || args.html.indexOf('&') > -1) )
    {
    	this.elemType = "span";
    	makeWidget(this, args);
    	this.elem.innerHTML = args.html;
    }
    else
    {
    	makeWidget(this, args);
     	this.elem = document.createTextNode(args.html || "" );
     	if( ! dojo.isIE ) this.elem.id = args.id;
    }

	this.SetHtml = function(html)
	{

		if( html.indexOf( '<' ) > -1 )
		{
			//e = document.createElement("div");
			//e.innerHTML = html;
			this.elem.innerHTML = html;
			//this.elem = e;
		}
		else
		{		
			this.elem.nodeValue = html;
		}
	
	}
}
functionMap["l"] = l;

function la( args )
{
	this.elemType = args.tn ? args.tn : "DIV";
	this.className = "label";
	makeWidget(this, args);
	this.elem.innerHTML = args.html ? args.html : '&nbsp;';
	this.SetText = function( html )
	{
		//this.textNode.data = newText;
		this.elem.innerHTML = html;
	};
	this.SetValue = this.SetText;
	this.AppendText = function( newText )
	{
		if( newText.indexOf( "Server Error in" ) == -1 )
			newText = "<PRE>" + newText + "</PRE>";
		this.elem.innerHTML += newText ;
	};
}
functionMap["la"] = la;

function h( args )
{
	this.elemType = "DIV";
	makeWidget(this, args);
	//this.hoverContainer
	this.elem.style.display = 'none';
	dojo.connect( this.hoverContainer, "onmouseover", this, "Hover" );
	dojo.connect( this.hoverContainer, "onmouseout", this, "Unhover" );
	dojo.connect( this.elem, "onmouseout", this, "Unhover" );

	this.Hover = function(e)
	{
		var tg = (window.event) ? e.srcElement : e.target;
		while( tg != null )
		{
			if( tg == this.hoverContainer )
			{
				if( lastHover != null && lastHover.style.display == 'inherit' )
				{
					lastHover.style.display = 'none';
				}
				this.elem.style.display = 'inherit';
				lastHover = this.elem;
			}
			tg = tg.parentNode;
		}
		//need to cancel bubbling, since we don't want to activate outer hoverboxes
		e.cancelBubble = true;	
	};

	this.Unhover = function(e)
	{
		if (!e) var e = window.event;
		var tg = (window.event) ? e.srcElement : e.target;
		var reltg = (e.relatedTarget) ? e.relatedTarget : e.toElement;
		while (reltg != null && reltg != tg )
			reltg= reltg.parentNode;
		if (reltg== tg) return;
	
		//if( tg == this.hoverContainer || tg == this.elem )
		//{
			this.elem.style.display = 'none';
		//}
	};
}
functionMap["h"] = h;

var lastHover;

function dialog( args )
{
    this.elemType = "DIV";
	makeWidget(this, args);

	dialog.prototype.Render = function(args)
	{	
		this.uber("Render",args);
		this.dijit = new dijit.Dialog(this,this.elem);
		this.dijit.startup();
		this.elem = this.dijit.containerNode;
	}

	dialog.prototype.ShowDialog = function()
	{
		console.log("not implemented");
	}

	dialog.prototype.Remove = function () 
	{
		console.log("not implemented");
	}
}
functionMap["dialog"] = dialog;

//dojo.require("dojo.xml.Parse");
//var parser = new dojo.xml.Parse();
function dr( args ) //id, baseElem, className, options, selIndex )
{
    this.elemType = "SELECT";
	makeWidget(this, args);
	if(! this.args.options )
		return;
	this.optionList = args.options;
	this.idsList = null;
	if( args.ids ) this.idsList = args.ids;
	var selIndex = this["ea"]?this.ea.selectedIndex:-1;
	for( var i = 0; i < this.optionList.length; i++ )
	{
	    this.AddOption( this.optionList[i], this.idsList ? this.idsList[i] : i, i == selIndex );
    }
    if( ! this.isComboBox )
    {
        dojo.connect( this.elem, "onchange", this, "OnChanged" );
	    this.GetValue = function() { return this.elem.value; };
	}
	//if( args.selectedIndex >= 0 ) this.SetSelectedIndex(args.selectedIndex);

	this.Update = function()
	{
		this.optionList = this.options;
		if( this.ids ) 
			this.idsList = this.ids;
		while( this.elem.hasChildNodes() )
			this.elem.removeChild( this.elem.firstChild );
		var selIndex = this["ea"]?this.ea.selectedIndex:-1;
		for( var i = 0; i < this.optionList.length; i++ )
		{
			this.AddOption( this.optionList[i], this.idsList ? this.idsList[i] : i, selIndex == i );
		}
	};

	this.AddOption = function( label, id, selected )
	{
		    var o = document.createElement("OPTION");
			o.appendChild( document.createTextNode( label ) );
			if( id == null )
			{
				id = this.optionList.length - 1;
			}
			o.value = id;
		
			o.selected = selected;
			this.elem.appendChild( o );
	};

	this.SetSelectedIndex = function( newIndex )
	{
		if( this.selIndex ) this.elem.childNodes[ this.selIndex ].selected = false;
		this.elem.childNodes[ newIndex ].selected = true;
		this.selIndex = newIndex;
	};

	this.SetValue = function ( value )
	{
		for( var i = 0; i < this.elem.childNodes.length; i++ )
		{
		    if( this.elem.childNodes[i].value == value || this.elem.childNodes[i].innerHTML == value )
		    {
		        this.SetSelectedIndex( i );
		        return;
		    }
		}
	};
}
functionMap["dr"] = dr;

function ph( args )
{
    this.elemType = "div";
    makeWidget(this, args);
}
functionMap["ph"] = ph;

function p( args )
{
	if( ! this.elemType ) this.elemType = "DIV";
    //this.className = "pane";
    makeWidget(this, args);
	if( this.left ) this.elem.style.left = this.left;
	if( this.top ) this.elem.style.top = this.top;
	if( this.width ) this.elem.style.width = this.width;
	if( this.height ) this.elem.style.height = this.height;
}
functionMap["p"] = p;
///END WIDGETS


var startingBookmarkId;
function checkForBookmark()
{
	startingBookmarkId = getHash();
}

function sendBookmark()
{
	checkForBookmark();
//	function Send( id, evt, arg, sync, formNode )
	Send( "root", "OnBookmark", startingBookmarkId );
}

function getHash(){ 
	var h = window.location.hash;
	if(h.charAt(0) == "#") { h = h.substring(1); }
	return h; //dojo.isMozilla ? h : decodeURIComponent(h); 
}
	
function setHash(h){
	if(!h) { h = "" };
	window.location.hash = h; //encodeURIComponent(h);
}

var bookmarksInitialized = false;
function setBookmark(bookmark)
{
	if( bookmark == getHash() )
		return;
	console.log( "client side setBookmark", bookmark );
	var state = {
		handle:function(type){
			console.log( "client side bookmark handled", type, bookmark );
			bookmark == getHash()
			Send("root","OnBookmark",bookmark);
		},
		changeUrl:bookmark
	}
	
	if( ! bookmarksInitialized )
	{
		dojo.back.setInitialState(state);
		bookmarksInitialized = true;
	}
	
	dojo.back.addToHistory(state);
}

function bookmarkHandler(state)
{
	console.log(arguments);
}

function clearChildren( domNode )
{
	while( domNode.childNodes.length )
		domNode.removeChild( domNode.childNodes[0] );
}

function emerge_post_load()
{
	connectMouseMove();
}

var mouseMoveConnected = 0;
function connectMouseMove()
{
	if( ! mouseMoveConnected )
	{
		dojo.connect( document.body, "onmousemove", null, general_mousemove );	
	}
}

var throbber;
var throbRate = 100;
function showThrobber()
{
	if( ! throbber )
	{
		throbber = document.createElement("div");
		throbber.className = "throbber";
	}
	else
	{
		throbber.style.display = '';
	}
	if( ! throbber.timerId )
		throbber.timerId = setInterval(pulseThrobber,throbRate);
	throbber.frame = 0;
	document.body.appendChild( throbber );
	throbber.isVisible = true;
	throbber.style.top = (lastMouseY+throbberOffsetY) + "px";
	throbber.style.left = (lastMouseX+throbberOffsetX) + "px";
}

function pulseThrobber()
{
	throbber.style.backgroundPosition = (throbber.frame * 18) + "px 0px";
	throbber.frame = (throbber.frame-1) % -10;
	
}

function hideThrobber()
{
	if( ! throbber ) return;
	clearInterval( throbber.timerId );
	throbber.style.display = 'none';
	throbber.isVisible = false;
	throbber.timerId = null;
}

var throbberOffsetX = 12, throbberOffsetY = 12;
var lastMouseX = 0, lastMouseY = 0;
function general_mousemove(e)
{
	var posx = 0;
	var posy = 0;
	if (!e) var e = window.event;
	if (e.pageX || e.pageY) 	{
		posx = e.pageX;
		posy = e.pageY;
	}
	else if (e.clientX || e.clientY) 	{
		posx = e.clientX + document.body.scrollLeft
			+ document.documentElement.scrollLeft;
		posy = e.clientY + document.body.scrollTop
			+ document.documentElement.scrollTop;
	}
	// posx and posy contain the mouse position relative to the document
	// Do something with this information
	lastMouseX = posx;
	lastMouseY = posy;
	if( throbber && throbber.isVisible )
	{
		throbber.style.top = (posy+throbberOffsetY) + "px";
		throbber.style.left = (posx+throbberOffsetX) + "px";
	}
}
