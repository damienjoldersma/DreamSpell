var SVG_NS = "http://www.w3.org/2000/svg";
var XLINK_NS = "http://www.w3.org/1999/xlink";

function elog(s)
{
	if( console )
		console.log(s);
}

var sessionId = "NA";
function setSessionId( id )
{
	sessionId = id;
}

//dojo.require("dojo.event");
dojo.require("dojox.fx");
dojo.require("dojox.xml.DomParser"); 
dojo.require("dojo._base.json")
dojo.require("dojo.back");
dojo.require("dijit.form.DateTextBox");

var emergetk = {};
var emerge_post_load;
var $ = document.getElementById;
var Log, LogPane_Log;
function log(aMessage)
{
    //GBLTut_ConsoleService.logStringMessage('My_Extension: ' + aMessage);
    //consoleService.logStringMessage(aMessage);
    //alert( aMessage );
    if( Log != null )
		Log.AppendText( aMessage + "<BR>" );
	else if( LogPane_Log != null )
		LogPane_Log.AppendText( aMessage + "<BR>" );
	//alert( aMessage );
}

Function.prototype.toName = function() {
    var sConstructor = this.toString(); 
    var aMatch = sConstructor.match( /\s*function (.*)\(/ ); 
    if( aMatch && aMatch[1] ) return aMatch[1]; else return null;
}

Function.prototype.inherits = function (parent) {
    var parentCnName = parent.toName();
    var parentObj = parent.prototype;
    for( var k in parentObj )
        this.prototype[k] = parentObj[k];
    var d = 0;
    var p = parentObj;
    //var d = 0, p = (this.prototype = new parent());
    this.prototype[parentCnName] = parent;
    this.prototype.uber = function uber(name) {
        var f, r, t = d, v = parent.prototype;
        if (t) {
            while (t) {
                v = v.constructor.prototype;
                t -= 1;
            }
            f = v[name];
        } else {
            f = p[name];
            if (f == this[name]) {
                f = v[name];
            }
        }
        d += 1;
        r = f.apply(this, Array.prototype.slice.apply(arguments, [1]));
        d -= 1;
        return r;
    }
    this.prototype.uberArgs = function uberArgs(name, args) {
        var f, r, t = d, v = parent.prototype;

        if (t) {
            while (t) {
                v = v.constructor.prototype;
                t -= 1;
            }
            f = v[name];
        } else {
            f = p[name];
            if (f == this[name]) {
                f = v[name];
            }
        }
        d += 1;
        r = f.apply(this, args);
        d -= 1;
        return r;
    }
    return this;
}

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

function getSendUrl()
{
	if( window.sendUrl )
		return window.sendUrl;
	var url = document.location.href;
	if( url.indexOf('#') > -1 )
	   	url = url.substring(0,url.indexOf('#'));
	return url;
}

var twoWayComet = false;
var expireEventId, expired, expireState, slidingExpiration = 1000 * 60 * 15;
function Send( id, evt, arg, sync, formNode )
{
	//if( expireEventId )
	//	clearInterval( expireEventId );
	//expireEventId = setTimeout( expire, slidingExpiration );
	showThrobber();
	
	if( twoWayComet && cometHandler && cometHandler.comet && cometHandler.comet.send )
	{
		cometHandler.comet.send( { "id":id, "evt":evt, "arg":arg } );
		return;
	}
	
	var sepChar = '?';
	if( window.sendUrl )
		var url = window.sendUrl;
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
	
	if( ! formNode )
	{
		dojo["xhr"+method]({
	        url: url,
	        sync: sync,
	        content: arg || arg == 0 ? {arg:arg} : null,        
	        load: doLoad,
			handleAs:"javascript"
	    });
	}
	else
	{
		dojo.require("dojo.io.iframe");
		dojo.io.iframe.send({
			method: "POST",
	        url: url,
	        content: arg || arg == 0 ? {arg:arg} : null,        
	        load: doLoad,
			handleAs:"javascript",
	        form: formNode,
	        multipart: formNode ? true : null,
	        enctype: formNode ? "multipart/form-data" : null
	    });
	}
}

var lastRE;

function receive(e, data)
{
	//console.log( "clientjs received data", data );
	//console.log( e.data );
	//data = e.data.replace(/\\/g,"\\\\")
	//data = e.data;
	//console.log( "recv", e, data );
	lastRE = e;	
	dojo.eval(data);
	hideThrobber();	
}

function expire()
{
	console.log( "expiring" );
	Send( "root", "Expire" );
	clearInterval( expireEventId );
	expired = true;
}

function setExpireState( state )
{
	console.log( "setting expire state", state );
	expireState = state;
}

var confirming = false;
function restore()
{
	console.log( "restoring" );
	if( expired && expireState )
	{
		Send( "root", "Restore", dojo.toJson(expireState) ); 
		expired = false;
		expireState = null;
	}
	else
	{
		if( ! confirming )
		{
			confirming = true;
			if( confirm("Oops.  Looks like we lost our connection.  Press OK to refresh.") )
			{
				document.location.reload();
			}
			confirming = false;
		}
	}
}	

onRecv = null;
function doLoad(type, evaldObj)
{ 
	//eval( type );
	hideThrobber();
	if( onRecv ) 
		onRecv(type,evaldObj);
}

var currentFrameId;
function AddBack(id)
{
    //have to handle the case where this id is already on the querystring
    if( document.location.hash.indexOf(id) == 1 )
	return;
    currentFrameId = id;
}

var dndCreator = function( input, hint )
{
	//console.log("dnd creator", input, hint, arguments );
	var node = dojo.doc.createElement("div");
	if( hint == 'avatar' )
	{
		node.innerHTML = input.elem.innerHTML; 
	}	
	else
	{
		node = input.elem;
	}
	
	dojo.addClass(node, "dojoDndItem");

	var type;
	if( input.dndType )
		type = [input.dndType];
	else
		type = ["*"];

//	console.log( "drop type: ", type );
	node.id = dojo.dnd.getUniqueId();
	var n = {node:node,data:input,type:type};
	return n;
}

dojo.subscribe("/dnd/drop", function(source, nodes, copy, target)
{
	//console.log( "drop event: ", arguments );
	var e = nodes[0];
	var t = target;
	
	setTimeout( function() {

		var p = t.node;
		var pos = -1;
		for( var i = 0; i < p.childNodes.length; i++ )
		{
			if( p.childNodes[i] == e )
			{
				pos = i;
				break;
			}
		}
		
		console.log( e );

		if( pos > -1 )
			Send( p.id, "OnReceiveDrop", e.widget.id + "," + pos  );
		else
			console.log( pos );
	},20 );

});

Widget.prototype.MakeDragSource = function (dragArg)
{
//	console.log("making drag source ", this, dragArg, dragArg.dropSource );
    if( dojo.dnd && dragArg && dragArg.dropSource )
	    dragArg.dropSource.insertNodes( false, [this] );
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

function Widget( args ) //id, baseElem, className, appearEffect )
{
  //  try
   // {
    
    for( var k in args )
        if( (args[ k ] || args[k] == 0 ) && ! ( args[k] == '' && this[k]) )
            this[ k ] = args[ k ];
    if( this.elemType )
    {
    	var formElem = null;
        if( ! this.elemNS )
        {
            this.elem = document.createElement(this.elemType);
            if (  args.isForm )
            	formElem = document.createElement("FORM");
        }
        else
        {
            this.elem = document.createElementNS(this.elemNS, this.elemType);
            if (  args.isForm == "true" )
            	formElem = document.createElementNS(this.elemNS,"FORM");
        }

		if ( args.isForm ) 
		{ 
			//alert('inserting'); //this.elem.appendChild( formElem )
			//this.p.elem = formElem.appendChild(this.p.elem);
		}

        if( this.opac )
        	dojo.style(this.elem,"opacity",this.opac);
        this.elem.id = this.id;
        if( this.cn )
        {
        	this.className = this.cn;
            try
            {
                this.elem.className = this.cn;
            }
            catch(e)
            {
                this.elem.setAttribute("class",this.cn);
            }
        }
        if( this.ea ) this.SetElem(this.ea);
        if( this.s ) this.SetStyle( this.s );
        if( this.ep )
        	for( var k in this.ep )
				this.elem[k] = this.ep[k];
    }
    this.args = args;
    if( this.p )
    {
    	//if ( args.isForm ) { alert('inserting'); this.elem.appendChild( formElem ) }        
        this.p.AddWidget( this );
        this.baseElem = this.p.elem;       	
        if ( args.isForm )
        {
        	var parentDiv = this.p.elem.parentNode;        	
        }
        	//this.baseElem = formElem.appendChild(this.p.elem);
        //else
        	//this.baseElem = this.p.elem;
        if( this.p.OnChildAdded )
            this.p.OnChildAdded(this);
    }
    else if( ! this.baseElem )
    	this.baseElem = document.body;
    if( this.onDelayedMouseOver )
    {
        //this.elem.onmouseover = this.OnDelayedMouseOver.bindAsEventListener(this);
        dojo.connect( this.elem, "onmouseover", this, "OnDelayedMouseOver" );
    }
    this.WireUpEvents();
	if( this.elem )
		this.elem.widget = this;
    window[this.id] = this;
//    }
//    catch(e){console.debug("error setting up " + args.id ); console.debug(e);}    
}

Widget.prototype.test = function() { console.log( "testing ", this ); }

Widget.prototype.WireUpEvents = function(wireUpChildren)
{
	//console.debug( "wiring up events for ", this, this.elem );
	if( this.onReceiveDrop && dojo.dnd)
    {
        //dojo.dnd.HtmlDropTarget
		//console.log( "wiring up drop source, accept: ", this.dndAccept );

		if( ! this.dndAccept ) 
			this.dndAccept = "*";
		this.dropSource = new dojo.dnd.Source(this.elem, {creator:dndCreator,copyOnly:false,accept:[this.dndAccept]});		
    }
    if( this.onClick )
    {
        dojo.connect( this.elem, "onclick", this, "Click" );
    }
    if( this.onBlur )
    {
        dojo.connect( this.elem, "onblur", this, "Blur" );
    }
	if( this.onKeyPress )
	{
		console.log("connecting onkeyup", this.KeyPress);
		dojo.connect( this.elem, "onkeyup", this, "KeyPress" );
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

Widget.prototype.Debug = function()
{
	Send( this.id, "Debug", this.arg, null, null );
}

var Click = Widget.prototype.Click = function (evt)
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
	//if( evt && evt.stopPropogation ) evt.stopPropogation();
}

Widget.prototype.KeyPress = function(e)
{
	console.log(e);
//	Send( this.id, "OnKeyPress", 
}

Widget.prototype.InvokeEvent = function( evt, arg )
{
	Send( this.id, evt, arg );
}

Widget.prototype.ScrollToBottom = function()
{
	setTimeout( function() {
		this.elem.scrollTop = this.elem.offsetHeight;
	}, 10 );
}

Widget.prototype.WipeOutLeft = function()
{
	
}

Widget.prototype.WipeInLeft = function()
{
	
}

Widget.prototype.WipeOutRight = function()
{
	
}

Widget.prototype.WipeInRight = function()
{
	
}

//AddClass
Widget.prototype.ac = function(cssClass)
{
	dojo.addClass( this.elem, cssClass );
}

//RemoveClass
Widget.prototype.rc = function(cssClass)
{
	dojo.removeClass( this.elem, cssClass );	
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

Widget.prototype.OnChanged = function (e)
{
	Send( this.id, this.evt?this.evt : "OnChanged", this.GetValue(),this.sync );
	lastOnChanged = null;
}

Widget.prototype.SetElem = function(args)
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
        /*else if( k.substr(0,2) == 'on' )
        {
            dojo.connect( this.elem, k, v );
        }*/
        else
        	this.elem.setAttribute(k, v);
    }
}

Widget.prototype.SetAttribute = function(attribute,value)
{
    if( attribute == "opac" )
		dojo.style(this.elem,"opacity", value);
	else
    	this.elem.setAttribute( attribute, value );
}

Widget.prototype.SetProperty = function(attribute,value)
{
   	this[ attribute ] = value;
}

Widget.prototype.SetElementProperty = function(attribute,value)
{
   	this.elem[ attribute ] = value;
}

Widget.prototype.SetStyle = function(pairing)
{
    //console.debug(dojo.string.paramString( "setting %{att} to %{val} on %{widget}", {att:attribute,val:value,widget:this.id} ) );
    for( var k in pairing )
        this.elem.style[k] = pairing[k];
}

Widget.prototype.SetSvgStyle = function( newStyle )
{
	if( ! this.elem ) alert( "svgsetstyle on this.elem is null." ); 
    this.elem.setAttribute("style",newStyle);
}


Widget.prototype.OnDragOver = function()
{
    this.elem.setAttribute("class",this.className + " dropHighlight");
}

Widget.prototype.OnDragOut = function()
{
    this.elem.setAttribute("class",this.className);
}

Widget.prototype.Move = function( newParent, newId )
{
    newParent.elem.appendChild( this.elem );
    this.id = newId;
    if( this.dragSource )
        this.dragSource.dragArg = newId;
}

Widget.prototype.SetDragHandle = function (elem)
{
    if( this.dragSource ) this.dragSource.setDragHandle(elem);
}

Widget.prototype.OnReceiveDrop = function(evt)
{ 
    var position = 0;
    var node = evt.dragObject.domNode;
    for( var i = 0; i < node.parentNode.childNodes.length; i++ )
    {
        if( node.parentNode.childNodes[i] == node )
        {
             position = i;
             break;
        }
    }
    Send( this.id, "OnReceiveDrop", evt.dragSource.dragArg + "," + position);
    this.elem.setAttribute("class",this.className);
    return true;
}


Widget.prototype.OnDelayedMouseOver = function(evt)
{
	if( this.isover )
		return;
	this.isover = true;
    this.mouseX = evt.pageX;
    this.mouseY = evt.pageY;
    this.timeoutid = setTimeout( dojo.hitch( this, this.OnDelayReached ), 200 );
    dojo.connect( this.elem, "onmouseout", this, "OnDelayedMouseOut" );
    //this.elem.onmouseout = this.OnDelayedMouseOut.bindAsEventListener(this);
}

Widget.prototype.OnDelayReached = function()
{
    Send( this.id, "OnDelayedMouseOver", this.mouseX + "," + this.mouseY );
   
    if( this.onDelayedMouseOut )
    {
    	dojo.event.disconnect( this.elem, "onmouseout",this,"OnDelayedMouseOut");
        dojo.connect( this.elem, "onmouseout", this, "OnDelayedMouseOutSend" );
    }
        //this.elem.onmouseout = this.OnDelayedMouseOutSend.bindAsEventListener(this);
}

Widget.prototype.OnDelayedMouseOut = function()
{
	this.isover = false;
	dojo.event.disconnect( this.elem, "onmouseout",this,"OnDelayedMouseOut");
    clearTimeout( this.timeoutid );
}

Widget.prototype.SetRollingDelay = function( id, func, time )
{
    if( this[id] ) clearTimeout( this[id] );
    this[id] = setTimeout( dojo.hitch( this, func ), time );
}

Widget.prototype.OnDelayedMouseOutSend = function()
{
	this.outtimeoutid = dojo.lang.setTimeout( this, this.OnDelayOutReached, 50 );
	dojo.connect( this.elem, "onmouseover", this, "OnDelayedMouseOutCancel" );
}

Widget.prototype.OnDelayedMouseOutCancel = function()
{
	clearTimeout( this.outtimeoutid );
}

Widget.prototype.OnDelayOutReached = function()
{
    dojo.event.disconnect( this.elem, "onmouseout", this, "OnDelayedMouseOutSend" );
    Send( this.id, "OnDelayedMouseOut", "" );
    this.isover = false;
}

Widget.prototype.AddWidget = function( child )
{
    if( ! this.widgets ) { this.widgets = []; }
    this.widgets[ this.widgets.length] = child;
}

var lastFocus;
Widget.prototype.Focus = function()
{
	try
	{
		if( lastFocus )
			lastFocus.Blur();
		if( this.elem.focus )
		{
			this.elem.focus();
		}
		else
		{
			this.bodyOnClickId = dojo.connect(document.body,"onclick", this, "blurTest");
			this.bodyOnKeyUp = dojo.connect(document.body, "onkeyup", this, "blurTest" );
			lastFocus = this;
		}
	}
	catch(e){}
}

Widget.prototype.Blur = function(evt)
{
	dojo.disconnect( this.bodyOnClickId );
	dojo.disconnect( this.bodyOnKeyUp );
	Send( this.id, "OnBlur", this.arg, null, null );
	lastFocus = null;
}

Widget.prototype.blurTest = function(evt)
{
	var isIn = false;
	if( evt.keyCode )
	{
		if( evt.keyCode == 9 )
			this.Blur();
		else
			return;
	}
	else
	{
		isIn = false;
		var t = evt.target;
		do
		{
			if( t == this.elem )
				isIn = true;
			t = t.parentNode;
		} while( t );
	}
	if( ! isIn )
		this.Blur();
}

var Effect = {};
Widget.prototype.Render = function ()
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
}

Widget.prototype.Show = function () 
{
    //Effect.Appear( this.id );
    this.elem.style.display = this.oldDisplay ? this.oldDisplay : "";
}

Widget.prototype.Hide = function () 
{
    this.oldDisplay = this.elem.style.display;
    this.elem.style.display = "none";
}

Widget.prototype.TweakDisplay = function () 
{
    this.oldDisplay = this.elem.style.display;
    this.elem.style.display = "none";
    setTimeout( dojo.hitch( this, this.Show ), 50 );
}

Widget.prototype.FadeShow = function(duration)
{
   dojo.lfx.html.fadeShow(this.elem,duration || 5000 ).play();
}

Widget.prototype.FadeHide = function(duration)
{
   dojo.lfx.html.fadeHide(this.elem,duration || 5000 ).play();
}

Widget.prototype.FadeOutAndIn = function(outDuration,inDuration)
{
   _this = this;
   dojo.lfx.html.fadeOut(this.elem,outDuration,function() { _this.FadeShow(inDuration); } ) 
}

Widget.prototype.Center = function()
{
	elem = this.elem;
			var top = ( dijit.getViewport().h / 2 - (elem.offsetHeight / 2  ) ) - 100
			if ( top < 0 ) top = "50"; // TODO if center is less than zero just set it 50px from top, but it would be nice to do this right!
   		var settings = { top: top + "px" };
   		dojo.style(elem, settings );
}

function centerElem(elem)
{
	var elementWidth = dojo.style.getInnerWidth(elem);
	var elementHeight = dojo.style.getInnerHeight(elem);
	
    var browserWidth = document.body.clientWidth;
	var browserHeight = document.body.clientHeight;

	// in Firefox if we are in standards compliant mode
	// (with a strict doctype), then the browser width
	// and height have to be computed from the root level
	// HTML element not the BODY element
	if(!dojo.isIE && document.compatMode == "CSS1Compat"){
	        browserWidth = document.body.parentNode.clientWidth;
	        browserHeight = document.body.parentNode.clientHeight;
	}else if(dojo.isIE && document.compatMode == "CSS1Compat"){
	        // IE 6 in standards compliant mode has to be calculated
	        // differently
	        browserWidth = document.documentElement.clientWidth;
	        browserHeight = document.documentElement.clientHeight;
	}else if(dojo.isSafari ){ // Safari works different
	        browserHeight = self.innerHeight;
	}

      // get where we are scrolled to in the document
      // the code below works in FireFox
      var scrolledByWidth = window.scrollX;
      var scrolledByHeight = window.scrollY;
      // compute these values differently for IE;
      // IE has two possibilities; it is either in standards
      // compatibility mode or it is not
      if(typeof scrolledByWidth == "undefined"){
              if(document.compatMode == "CSS1Compat"){ // standards mode
                      scrolledByWidth = document.documentElement.scrollLeft;
                      scrolledByHeight = document.documentElement.scrollTop;
              }else{ // Pre IE6 non-standards mode
                      scrolledByWidth = document.body.scrollLeft;
                      scrolledByHeight = document.body.scrollTop;
              }
      }

      // compute the centered position    
      var x = scrolledByWidth + (browserWidth - elementWidth) / 2;
      var y = scrolledByHeight + (browserHeight - elementHeight) / 2; 

	  elem.style.position = 'absolute';
	  if( y < 10 ) y = 10;
	  if( x < 10 ) x = 10;
      elem.style.top = y + "px";
      elem.style.left = x + "px";   
}

Widget.prototype.StretchToExtents = function()
{
	this.elem.style.width = document.documentElement.scrollWidth ? document.documentElement.scrollWidth : 
		document.documentElement.scrollLeft;
	this.elem.style.height = document.documentElement.scrollHeight ? document.documentElement.scrollHeight : 
		document.documentElement.scrollHeight;
}

Widget.prototype.Remove = function () 
{
    if( this.elem.parentNode )
        this.elem.parentNode.removeChild( this.elem );
    else if( this.elem.style )
        this.elem.style.setAttribute("display","none");
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

Widget.prototype.Clone = function (cloneArgs)
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
    /*
    for( var k in this )
    {
        if( k == "elem" )
        {
            newWidget.elem = this.elem.cloneNode(true);
            newWidget.elem.id = this.elem.id + "_";
        }
        else if( this[k] && this[k]["Clone"] && this[k]["Clone"].constructor == Function )
        {
            newWidget[k] = this[k].Clone();
        }
        else
        {
            newWidget[k] = this[k];
        }
    }*/
    return newWidget;
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
    Widget.call(this, args);
    if( args && args.pl )
    {
    	this.oldPadding = this.elem.style.padding;
    	this.elem.style.padding = "10px";
    }
}

etk_i.prototype.OnLoad = function()
{
	this.InvokeEvent("OnLoad", this.loader.width + "," + this.loader.height); //this.elem.src = this.url;
	this.elem.style.padding = this.oldPadding;
}

//image
etk_i.inherits( Widget );

etk_i.prototype.SetElem_ = function(args)
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
        this.uber("SetElem", args );
    }
}

//imagebutton
ib.inherits( etk_i );
function ib(args)
{
    etk_i.call(this,args);
    //this.elem.onclick = Click.bindAsEventListener(this);    
}

function SetAttribute( widget, attribute, value )
{
    widget.SetAttribute(attribute,value);	
}

function SetProperty( widget, attribute, value )
{
    widget.SetAttribute(attribute,value);	
}

function getObj( o )
{
	var str = "";
	for( var k in o )
	{
		try
		{
			console.debug(k + ": " + o[k]);
		}
		catch(e)
		{
		}
	}
	return str;
}

bu.inherits( Widget );
function bu( args )
{
    this.elemType = "INPUT";
    this.Widget(args);
	this.elem.type = "button";
	this.elem.value = this.label;
	this.elem.arg = this.arg;
}

bu.prototype.SetText = function( html )
{
	//this.textNode.data = newText;
	this.elem.value = html;
}

lb.inherits( Widget );
function lb( args )
{
	this.elemType = "A";
	args.onClick = 1;
	this.Widget(args);
	if( args.label )
		this.elem.innerHTML = args.label;
	this.elem.setAttribute("href","javascript:void(1)");
}

lb.prototype.SetText = function(html)
{
	this.elem.innerHTML = html;
}

HtmlElement.inherits( Widget );
function HtmlElement( args )
{
	this.elemType = args.tn;
	this.Widget(args);
}
var he = HtmlElement;

lb.prototype.SetText = function( html )
{
	this.elem.innerHTML = html;
}

li.inherits(Widget);
function li( args )
{
	this.elemType = "A";
	this.Widget(args);
	this.elem.innerHTML = args.label;
	this.elem.setAttribute("href",args.url);
}

Autocomplete.inherits( Widget );

function Autocomplete( args )
{
	this.elemType = "DIV";	
	this.Widget( args );
}

Autocomplete.prototype.Render = function(index)
{
    this.uber("Render", index); 

	dojo.require("dijit.form.FilteringSelect");
	dojo.require("emergetk.autoCompleteStore");
	var store = new emergetk.autoCompleteStore({url:window.sendUrl});
	store.widget = this;

	//store.fetch({serverQuery:{name:'a'}, queryOptions:{ignoreCase:false}});
	


	var args = {           
		//store: store
		searchDelay:300,
		pageSize:10
        };


	this.dijit = new dijit.form.FilteringSelect(args, this.elem );
	this.dijit.store = store;
    dojo.connect( this.dijit, "onChange", this, "Changed" );
	if( this.value )
		this.dijit.setValue( this.value );
}

Autocomplete.prototype.Changed = function(val)
{
	Send( this.id, "OnChange", this.dijit.store.getValue(this.dijit.item, "id") );
}

Autocomplete.prototype.SetValue = function( val )
{
	this.dijit.setValue( val );
}

/* CHARTING */

Chart.inherits( Widget )
function Chart( args )
{
	this.elemType = "DIV";
	this.Widget( args );
}

Chart.prototype.Render = function(index)
{
	this.uber("Render", index );
	dojo.require("dojox.charting.Chart2D");
	//dojo.require("dojox.charting.themes.PlotKit.green");
	this.chart = new dojox.charting.Chart2D(this.elem);
	//this.chart.setTheme(dojox.charting.themes.PlotKit.green);

	if( this.theme )
	{
		dojo.require( this.theme );
		this.chart.setTheme( eval(this.theme) );
	}

	if( this.axes )
	{
		_chart = this.chart;
		dojo.forEach( this.axes, function(a){_chart.addAxis( a.name, a ); } );
	}
	
	for( var pk in this.plots )
	{
		var plot = this.plots[pk];
		this.chart.addPlot( plot.name, plot );
		for( var sk in plot.series )
		{
			var series = plot.series[sk];
			if( !series || ! series.points )
				continue;
			var seriesData = series.points;
			switch( plot.type )
			{
			case "Bars":
				seriesData =  dojo.map(seriesData, function(item){ return item.y; });
				break;
			case "Columns":
				seriesData =  dojo.map(seriesData, function(item){ return item.y; });
				break;
			}
			console.log( seriesData );
			this.chart.addSeries(series.name,seriesData, { plot: plot.name } );
		}
	}
	this.chart.render();
}

/* END CHARTING */

//textbox
tb.inherits( Widget );
function tb( args )//id, baseElem, className, defaultValue, isPassword, isDisabled, rows, cols )
{		
    this.elemType = "INPUT";
    
    if( args.isRich || args.isInline || args.isCodeView )
    {
        this.elemType = "DIV";        
    }

	if( ( args.rows > 1 && ! args.isInline ) && ! args.isCodeView )
	{
	    this.elemType = "TEXTAREA";
	}

	this.Widget( args );
	
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

tb.prototype.StartChanged = function(e)
{
	addWaitFor(this);
	this.OnChanged();
}

tb.prototype.KeyUp = function( e )
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
}

tb.prototype.SetText = function( newText, width )
{
	//this.elem.value = newText ? newText : this.defaultValue ? this.defaultValue : '';
	if( this.elemType != "DIV" )
		this.elem.value = newText;
	else if ( this.elem )
		this.elem.innerHTML = newText ? newText : "";
	if( width ) this.elem.style.width = width;
	//if( this.rows == 1 && newText ) this.elem.size = newText.length + 5;
	
	if( this.args.isCodeView )
    {
        dojo._loadUri("/EmergeTk/CodeView/shCore.js" );
        switch( this.args.lang )
        {
        case "co#":
         	dojo._loadUri("EmergeTk/CodeView/shBrushCSharp.js" );
            break;
        case "javascript":
            dojo._loadUri("EmergeTk/CodeView/shBrushJScript.js" );
            break;
        case "xml":
            dojo._loadUri("EmergeTk/CodeView/shBrushXml.js" );
            break;
        case "sql":
            dojo._loadUri("EmergeTk/CodeView/shBrushSql.js" );
            break;
        }
        this.elem.name = this.id;
        this.elem.className = this.args.lang;
        //console.debug(this.elem.className);
        dp.SyntaxHighlighter.HighlightAll(this.id);
       // this.elem = this.baseElem.getElementsByTagName('DIV')[1];
    }
}

var tbid = 0;
var tinyLoaded = false;
tb.prototype.Render = function(index)
{
    this.uber("Render", index);
    if( this.args.isRich )
    {
        dojo.require("dojo.widget.Editor2");
        this.editor = new dijit.Editor( 
			{ 	shareToolbar: false, 
				toolbarAlwaysVisible: true,
				focusOnLoad: false,
				width: (this.cols ? this.cols : 80) + "em",
				height: (this.rows ? this.rows : 1) + "em"
			},this.elem);
	    dojo.connect(this.editor,"onBlur",this,"OnChanged");
		this.elem = this.editor.domNode;
        this.elem.style.border = "1px solid black";
    }
    else if( this.isSpinner )
    {
		dojo.require("dijit.form.NumberSpinner");
    	this.editor = new dijit.form.NumberSpinner({ "class":this.className,id:this.id, value:this.defaultValue}, this.elem );
    	dojo.connect(this.editor,"onChange",this,"OnChanged");
    	dojo.connect(this.editor,"_arrowReleased",this,"OnChanged");
    }
    else if( this.isInline )
    {
	    dojo.require("dijit.InlineEditBox");
		dojo.require("dijit.form.Textarea");
		dojo.require("dijit.form.TextBox");

    	this.editor = new dijit.InlineEditBox({ autoSave:this.autoUpdate,renderAsHtml:true,"class":this.className,id:this.id,
    		editor:this.rows > 1 ? "dijit.form.Textarea" : "dijit.form.TextBox" }, this.elem );
    	dojo.connect(this.editor,"onChange",this,"OnChanged");
    }
    else if( this.args.isCodeView )
    {
        dojo._loadUri( "EmergeTk/CodeView/shCore.js" );
      //  dojo.style.insertCssFile( "EmergeTk/CodeView/SyntaxHighlighter.css" )
        switch( this.args.lang )
        {
        case "co#":
            dojo._loadUri("EmergeTk//CodeView/shBrushCSharp.js" );
            break;
        case "javascript":
            dojo._loadUri("EmergeTk//CodeView/shBrushJScript.js" );
            break;
        case "xml":
            dojo._loadUri("EmergeTk//CodeView/shBrushXml.js" );
            break;
        case "sql":
            dojo._loadUri("EmergeTk//CodeView/shBrushSql.js" );
            break;
        }
        this.elem.name = this.id;
        this.elem.className = this.args.lang;
        //console.debug(this.elem.className);
        dp.SyntaxHighlighter.HighlightAll(this.id);
       //this.elem = this.baseElem.getElementsByTagName('DIV')[1];
    }
}

function globalTextCallBack(inst)
{
    Send(inst.formTargetElementId, "OnChanged",inst.getBody().innerHTML);
}

si.inherits( Widget );
function si( args )
{
    this.elemType = "INPUT";
    this.Widget(args);
    dojo.connect( this.elem, "onchange",this, "OnChanged" );
	this.elem.arg = this.arg;
	this.GetValue = function()
	{
	    return this.elem.checked;
	}
}

si.prototype.SetValue = function(v)
{
	if( v ) this.elem.checked = true;
	else this.elem.checked = null;
}

//label
l.inherits( Widget );
function l( args )
{
    if( args.html && ( args.html.indexOf('<') > -1 || args.html.indexOf('&') > -1) )
    {
    	this.elemType = "span";
    	this.Widget( args );
    	this.elem.innerHTML = args.html;
    }
    else
    {
    	this.Widget( args );
     	this.elem = document.createTextNode(args.html || "" );
     	if( ! dojo.isIE ) this.elem.id = args.id;
    }
}

l.prototype.SetHtml = function(html)
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

la.inherits( Widget );
function la( args )
{
	this.elemType = args.tn ? args.tn : "DIV";
	this.className = "label";
	this.Widget( args );
	this.elem.innerHTML = args.html ? args.html : '&nbsp;';
	this.SetValue = this.SetText;
}

la.prototype.SetText = function( html )
{
	//this.textNode.data = newText;
	this.elem.innerHTML = html;
}

la.prototype.AppendText = function( newText )
{
	if( newText.indexOf( "Server Error in" ) == -1 )
		newText = "<PRE>" + newText + "</PRE>";
	this.elem.innerHTML += newText ;
}

h.inherits( Widget );
function h( args )
{
	this.elemType = "DIV";
	this.Widget( args );
	//this.hoverContainer
	this.elem.style.display = 'none';
	dojo.connect( this.hoverContainer, "onmouseover", this, "Hover" );
	dojo.connect( this.hoverContainer, "onmouseout", this, "Unhover" );
	dojo.connect( this.elem, "onmouseout", this, "Unhover" );
}

var lastHover;
var HoverManager = {};

HoverManager.OnHover = function(widget) 
{
}

HoverManager.OnUnhover = function(widget)
{
}

h.prototype.Hover = function(e)
{
	if( this.hoverContainer.hovering )
		return;
	var tg = (window.event) ? e.srcElement : e.target;
	while( tg != null )
	{
		if( tg == this.hoverContainer )
		{
			if( lastHover != null && lastHover.style.display == 'inherit' )
			{
				lastHover.style.display = 'none';
			}
			this.hoverContainer.hovering = 1;
			HoverManager.OnHover(this);			
			this.elem.style.display = '';
			lastHover = this.elem;
			break;
		}
		tg = tg.parentNode;
	}
	//need to cancel bubbling, since we don't want to activate outer hoverboxes
	e.cancelBubble = true;	
}

h.prototype.Unhover = function(e)
{
	
	if (!e) var e = window.event;
	var tg = (window.event) ? e.srcElement : e.target;
	var reltg = (e.relatedTarget) ? e.relatedTarget : e.toElement;
	while (reltg != null && reltg != tg && reltg != this.hoverContainer )
	{
		reltg= reltg.parentNode;
	}
	if (reltg== tg || reltg == this.hoverContainer ) return;
	HoverManager.OnUnhover(this);
	this.hoverContainer.hovering = 0;
	//if( tg == this.hoverContainer || tg == this.elem )
	//{
	this.elem.style.display = 'none';
	//}
}

dialog.inherits( Widget );
function dialog( args )
{
	dojo.require("dijit.Dialog");
    	this.elemType = "DIV";
	this.Widget(args);
}

dialog.prototype.Render = function(args)
{	
	this.uber("Render",args);
	this.dijit = new dijit.Dialog(this,this.elem);
	this.dijit.startup();
	this.elem = this.dijit.containerNode;
}

dialog.prototype.ShowDialog = function()
{
	this.dijit.show();
}

dialog.prototype.Remove = function () 
{
	this.dijit.hide();
}

dp.inherits( Widget );
function dp( args )
{
	if( ! args.isCalendar )
		dojo.require("dijit.form.DateTextBox");
	else
		dojo.require("dijit._Calendar");
    this.elemType = "DIV";
    this.Widget(args);
}

dp.prototype.Render = function(args)
{
	this.uber("Render",args);
	if( ! this.isCalendar )
		this.dijit = new dijit.form.DateTextBox({},this.elem);
	else
		this.dijit = new dijit._Calendar({},this.elem);
    this.dijit.startup();
    this.dijit.setValue(new Date(this.date));	
    dojo.connect( this.dijit, "onChange", this, "SelectDate" );
}

dp.prototype.SelectDate = function(date)
{
    Send( this.id, "SetDate", dojo.date.stamp.toISOString(this.dijit.value) );	
}
dp.prototype.ChangeDate = function(date)
{
	//console.log("settign date", this, new Date(date) );
	this.dijit.setValue(new Date(date));
}

Window.inherits( Widget );
function Window( args )
{
    dojo.require("dojo.widget.ResizeHandle");
    dojo.require("dojo.widget.FloatingPane");
    this.elemType = "DIV";
    this.Widget(args);
}

Window.prototype.Render = function()
{
    this.uber("Render", arguments);
    var win = this.window = dojo.widget.createWidget("FloatingPane",{id:this.id + "_win",title:this.args.label,displayCloseAction:this.displayClose},this.elem);
    win.resizeTo(this.height, this.width);
    this.elem = win.containerNode;
}

ContextMenu.inherits( Widget );
function ContextMenu( args )
{
    dojo.require("dojo.widget.Menu2");
    this.elemType = "DIV";
    this.Widget(args);
    this.OnChildAdded = function(child){ 
        var menuItem = dojo.widget.createWidget("MenuItem2",{id:child.id + "_mi",iconSrc:child.icon,caption:child.label,submenuId:child.subMenuId});
	    this.menu.addChild(menuItem);	    
	    dojo.connect( menuItem, "onClick", child, "Click" );
	}
} 

ContextMenu.prototype.Render = function()
{
    this.uber("Render", arguments);
    var menu = this.menu = dojo.widget.createWidget("PopupMenu2",{id:this.id + "_cm",contextMenuForWindow:true},this.elem);
    this.elem = menu.domNode;
}

MenuItem.inherits( Widget );
function MenuItem( args )
{
    this.elemType = "DIV";
    this.Widget(args);
}

tnode.inherits( Widget );
function tnode( args )
{
    this.elemType = "DIV";
    if( args.c )
    	var children = [args.id];
    else
    	var children = [];
    this.data = { id:args.id, label:args.l, children:children, type: args.t};
    this.Widget(args);
    this.loadedChildren = [];
    var _self = this;
    this.OnChildAdded = function(child){
    	this.loadedChildren[this.loadedChildren.length] = child.data;
    	child.Tree = this.Tree;
	}
    this.hasNotReceivedChildren = true;
}

tnode.prototype.Render = function()
{
	
}

tnode.prototype.Expand = function()
{	
	var node = this.Tree.tree._itemNodeMap[this.id];
	node.expand();
}

tnode.prototype.Select = function()
{
	var _self = this;
	setTimeout( function() {
		var node = _self.Tree.tree._itemNodeMap[_self.id];
		_self.Tree.tree.focusNode(node);
	}, 20 );
}

tnode.prototype.receiveChildren = function(data){
	//console.log( this.Tree.tree );
	if( this.Tree.tree )
	{
		if( this.p.hasNotReceivedChildren )
		{
			//when parent receives children, call me.
			if( ! this.p.childrenToReceive )
				this.p.childrenToReceive = [];
			this.p.childrenToReceive[ this.p.childrenToReceive.length ] = this;
			return;
		}
		
		//console.log( this.loadedChildren );

		childArr = this.loadedChildren;
		var parentItem =  this.Tree.tree.store._getItemByIdentity(this.id );
		parentItem.children.splice(0,1);
		//var onComplete = this.Tree.tree.onComplete;
		//this.Tree.tree.onComplete = function(){};
		

		for( var i = 0; i < childArr.length; i++ )
		{
			//if( i == childArr.length - 1 )
			//	this.Tree.tree.onComplete = onComplete;
			//this.Tree.tree.model.newItem(childArr[i], parentItem );
			var n = this.Tree.tree.store.newItem(childArr[i], { parent: parentItem, attribute: "children" } );  
			//console.log(this.Tree.tree.store.isItemLoaded(n));

		}



		dijit.tree.ForestStoreModel.prototype._onNewItem.call( this.Tree.tree.model, null, {item: parentItem});

//		this.Tree.tree.model.onChildrenChange( parentItem, childArr );
		if( childArr.length > 0 )
			this.Expand();

		this.hasNotReceivedChildren = false; 
		if( this.childrenToReceive )
		{
			for( var i = 0; i < this.childrenToReceive.length; i++ )
				this.childrenToReceive[i].receiveChildren();
		}
		return;


		
	}
	else
	{
		if( ! this.Tree.startNodes )
			this.Tree.startNodes = [];
		this.Tree.startNodes[ this.Tree.startNodes.length ] = this;
	}
};

Tree.inherits( Widget );
function Tree( args )
{
    this.elemType = "DIV";
    this.Widget(args);
    this.data = { identifier:'id',label:'label',items:[] };
    this.OnChildAdded = function(child){ 
        this.data.items[this.data.items.length] = child.data;
        child.Tree = this;
    }
}


Tree.prototype.Startup = function()
{
//	this.Tree.tree.model._onNewItem = null;
	this.model = new dijit.tree.ForestStoreModel( 
		{ store: new emergetk.lazyStore({data:this.data}) } );
	this.tree = new dijit.Tree(
    	{
    		widgetId:this.id + "_t",    		
    		id:this.id,
    		store: new emergetk.lazyStore({data:this.data}),
    		isExpandable:true,
    		isExpanded:false,
    		persist:false,
    		getIconClass:dojo.hitch(this,"getIconClass")
    	},this.elem);
    dojo.connect( this.tree, "onClick", this, "treeClick" );
    this.tree.startup();
    this.tree.model._onNewItem = function() {};
    if( this.startNodes )
    {
   		for( var i = 0; i < this.startNodes.length; i++ )
   			this.startNodes[i].receiveChildren();
    }
}

Tree.prototype.getIconClass = function(item, opened)
{
	if( item && item.type[0] != "")
		return item.type[0];
	else
		return "na";
}

Tree.prototype.treeClick = function(event, source)
{
	var widget = window[event.id[0]];
	window[event.id[0]].Click();
}

Tree.prototype.Render = function()
{
   	dojo.require("dijit.Tree");
   	//dojo.require("dojo.data.ItemFileWriteStore");
   	dojo.require("emergetk.lazyStore");
    this.uber("Render", arguments);
}

Toolbar.inherits( Widget );
function Toolbar( args )
{
    this.elemType = "DIV";
    this.Widget(args);
    this.OnChildAdded = function(child){ 
        var toolbarItem = dojo.widget.createWidget("ToolbarButton",{id:child.id + "_i",icon:child.icon,label:child.label});
	    this.toolbar.addChild(toolbarItem);
	    dojo.connect( toolbarItem, "onClick", child, "Click" );
	}
}

Toolbar.prototype.Render = function()
{
    dojo.require("dojo.widget.Toolbar");
    this.uber("Render", arguments);
    var toolbarContainer = this.toolbarContainer = dojo.widget.createWidget("ToolbarContainer",{id:this.id + "_tc"},this.elem);
    var toolbar = this.toolbar = dojo.widget.createWidget("Toolbar",{id:this.id + "_tb"});
    toolbarContainer.addChild(toolbar);
    this.elem = toolbarContainer.domNode;
}

etk_t.inherits( Widget );
function etk_t( args )
{
	dojo.require("dojo.widget.TabContainer");
	dojo.require("dojo.widget.ContentPane");
	this.elemType = "DIV";	
	this.OnChildAdded = function(child){ 
	    var newTab = dojo.widget.createWidget("ContentPane",{id:child.id + "_cp",label:child.ea.label},child.elem);
	    //this.tabPane._setupTab(newTab);
	    child.tab = newTab;
	    this.tabPane.addChild(newTab);
	    child.Render = function() {};
	    //this.tabPane._doSizing();
	}
    this.Widget(args);
}

etk_t.prototype.SelectTab = function(tab)
{
    this.tabPane.selectTab( tab.tab);
}

etk_t.prototype.Render = function()
{
    
    
    var closeMode = this.closeMode ? this.closeMode.toLowerCase() : 'none';
    var labelPosition = this.labelPosition ? this.labelPosition.toLowerCase() : 'top';
    if( labelPosition == "left" || labelPosition == "right" )
    {
        labelPosition += "-h";
    }
    var tabPane = dojo.widget.createWidget("TabContainer",{id:this.id + "_tp",closeButton:closeMode,labelPosition:labelPosition});
    this.tabPane = tabPane;
    this.tabPane._doSizing();
    if( this.args["top"] )
        tabPane.domNode.style.top = this.args["top"];
    if( this.args["left"] )
        tabPane.domNode.style.left = this.args["left"];
    if( this.args["height"] )
        tabPane.domNode.style.height = this.args["height"];
    if( this.args["width"] )
        tabPane.domNode.style.width = this.args["width"];    
    this.elem = tabPane.domNode;
    this.isLoading = true;
    this.OnTabChanged = function(tab) 
    { 
        if( this.isLoading )
        {
            this.selectedTab = tab;
            this.isLoading = false;
            return;
        }
        if( this.selectedTab == tab ) return;
        this.selectedTab = tab;
        Send(this.id, "OnTabChanged", tab.label); 
    }
    dojo.connect( tabPane, "_showTab", this, "OnTabChanged");
    this.tabPane = tabPane;
    
    this.uber("Render", arguments);
}

SplitPane.inherits( Widget );
function SplitPane( args )
{
    dojo.require("dojo.widget.SplitContainer");
    this.elemType = "DIV";
    this.Widget(args);
    this.OnChildAdded = function(child){ 
	    var newPane = dojo.widget.createWidget("SplitContainerPanel",{id:child.id + "_spp"},child.elem);
	    this.splitPane.addChild(newPane);
	    this.splitPane.postCreate({});
	    this.splitPane.onResized();
	    if( this.widgets.length > 1 )
	    {
	        this.splitPane.postCreate({});
	       
	        this.elem.style.visibility = 'visible';
       }	        
	}
}

SplitPane.prototype.Render = function()
{
    this.uber("Render", arguments);
    //this.elem.setAttribute("orientation","vertical");
    //this.elem.setAttribute("style","width: 50%; height: 50%;");
    if( ! this.orientation )
    {
        this.orientation = "horizontal";
    }
    else
    {
        this.orientation = this.orientation.toLowerCase();
    }
    
    var isHorizontal = this.orientation == "horizontal" ? 1 : 0;
    
    console.debug("orientation:" + isHorizontal);
    var spArgs = {sizerWidth:4,sizeMin:50,sizeShare:85,id:this.id + "_sp",activeSizing:1,isHorizontal:isHorizontal,orientation:this.orientation};
    var splitPane = this.window = dojo.widget.createWidget("SplitContainer",spArgs,this.elem);
    if( this.args["top"] )
        splitPane.domNode.style.top = this.args["top"];
    if( this.args["left"] )
        splitPane.domNode.style.left = this.args["left"];
    if( this.args["height"] )
        splitPane.domNode.style.height = this.args["height"];
    if( this.args["width"] )
        splitPane.domNode.style.width = this.args["width"];    
    this.elem = splitPane.domNode;
    //this.elem.style.visibility = 'hidden';
    this.isLoading = true;
    this.splitPane = splitPane;
}

SliderWidget.inherits( Widget );
function SliderWidget(args)
{
    this.elemType = "DIV";
    this.orientation = "Horizontal";
    this.Widget( args );
    this.GetValue = function() { return this.val; }
    this.DelayChangeEvent = function() {
        this.val = Math.round(this.slider.getValue());
        if( this.timeoutid ) clearTimeout( this.timeoutid );
        this.timeoutid = setTimeout( dojo.hitch(this, this.OnChanged ), 200 );
    }
}

SliderWidget.prototype.Render = function(index)
{
    this.uber("Render", index);
	dojo.require("dijit.dijit"); // optimize: load dijit layer
	dojo.require("dijit.form.Slider");
	dojo.require("dijit.form.Button");

	var rulesNode = document.createElement('div');
	this.elem.appendChild(rulesNode);

	// setup the rules
	var sliderRules = new dijit.form.HorizontalRule({count:5,style:"width:5px"},rulesNode);

    this.slider = new dijit.form.HorizontalSlider({
            minimum:this.min,
            maximum:this.max,
            value:this.val
        },this.elem);

	


	this.slider.startup();
	sliderRules.startup();
    this.elem = this.slider.containerNode;
    dojo.connect(this.slider,"onChange", this, "DelayChangeEvent");
}

SliderWidget.prototype.SetAttribute = function(attribute, value )
{
    //console.debug(dojo.string.paramString( "val == 'val' ? %{expr}", {expr:attribute=="val"} ) );
    switch(attribute)
    {
    case "val":
        this.slider.setValue(value);
        break;
    case "min":
        this.slider.setMinimum(value);
        break;
    case "max":
        this.slider.setMaximum(value);
        break;
    default:
        this.uber("SetAttribute", attribute, value );
        break;
    }
}
//dojo.require("dojo.xml.Parse");
//var parser = new dojo.xml.Parse();
dr.inherits( Widget );
function dr( args ) //id, baseElem, className, options, selIndex )
{
    this.elemType = "SELECT";
	this.Widget( args );
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
}

dr.prototype.Update = function()
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
}

dr.prototype.Render = function()
{
    this.uber("Render",arguments);
    if( this.isComboBox )
	{
	    dojo.require("dojo.widget.ComboBox");
	    this.combobox = dojo.widget.createWidget("ComboBox",{id:this.id + "_cb"},this.elem);
	    this.RollingDelayChanged = function(evt) { 
	        this.SetRollingDelay( "onchangedTimer", this.OnChanged, 300 ); }
	    dojo.connect( this.combobox, "setValue", this, "RollingDelayChanged" );
	    this.GetValue = function() { return dojo.widget.getWidgetById(this.id + "_cb").getValue(); }
	}
}

dr.prototype.AddOption = function( label, id, selected )
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
}

dr.prototype.SetSelectedIndex = function( newIndex )
{
	if( this.selIndex ) this.elem.childNodes[ this.selIndex ].selected = false;
	this.elem.childNodes[ newIndex ].selected = true;
	this.selIndex = newIndex;
}

dr.prototype.SetValue = function ( value )
{
    for( var i = 0; i < this.elem.childNodes.length; i++ )
    {
        if( this.elem.childNodes[i].value == value || this.elem.childNodes[i].innerHTML == value )
        {
            this.SetSelectedIndex( i );
            return;
        }
    }
}

ph.inherits( Widget );
function ph( args )
{
    this.elemType = "div";
    this.Widget(args);
}

FadingAlert.inherits( Widget );
function FadingAlert( args )
{
    this.elemType = "DIV";
    this.className = "fadingAlert";
    this.Widget( args );
	this.elem.innerHTML = args.html;
	this.elem.style.display = "none";
}

FadingAlert.prototype.Render = function()
{
    this.uber("Render");
    Effect.Appear( this.elem );
};

po.inherits( Widget );
function po( args )
{
	this.Widget( args );
}

po.prototype.Render = function(){}

po.prototype.Start = function()
{
	this.timerId = setInterval( this.id + ".Poll();", this.interval * 1000);
}

po.prototype.Poll = function()
{
	Send( this.id, "OnPoll", null );
}

DataGrid.inherits( Widget );
function DataGrid( args )
{
	//is two dimensional array, rows/cols
	//exposes two complex args -- rowTemplate and rows
	this.elemType = "TABLE";
	this.cn = "dataGrid";
	this.count = 0;
	this.pageIndex = 0;
	this.pages = {};
	this.rowClass = '';
	this.selectedRowClass = args.selectedRowClass;
	this.Widget(args);
    this.pages[this.pageIndex] = this.rows;
	var eTable = this.elem;
	var eTBody = document.createElement("TBODY");
	eTable.appendChild( eTBody );
	this.tbody = eTBody;
	this.SetHeader();
	this.SetRows();
	
	if( this.footerButtons )
	{
	    var foot = this.elem.createTFoot();
	    var tr = document.createElement("TR");
    	var td = document.createElement("TD");
    	foot.appendChild( tr );
	    td.colSpan = this.rowTemplate.length;
        for( var i = 0; i < this.footerButtons.length; i++ )
	    {
	        var name = this.footerButtons[i];
	        var newLinkButton = new lb({id:this.id + "_-1_-1_" + name, onClick:1, label:name, baseElem:td });
	        this.footerButtons[name] = newLinkButton;
	        newLinkButton.evt = name;
	        td.appendChild( newLinkButton.elem );
	    }   
	    tr.appendChild( td );
	    this.footTd = td;
	}
}

DataGrid.prototype.AddFooterButton = function( args )
{
    var newLinkButton = new lb({id:this.id + "_-1_-1_" + args.name, onClick:1, label:args.name, baseElem:this.footTd });
    newLinkButton.evt = args.name;
    this.footerButtons[args.name] = newLinkButton;
    if( args.pos >= this.footTd.childNodes.length )
    {
        this.footTd.appendChild( newLinkButton.elem );
    }
    else
    {   
        this.footTd.insertBefore( newLinkButton.elem, this.footTd.childNodes[args.pos] );
    }
}

DataGrid.prototype.RemoveFooterButton = function( name )
{
    this.footerButtons[ name ].Remove();
}

DataGrid.prototype.NewRow = function(newId)
{
   this.rows[ this.rows.length ] = [];
   var newRow = [];
   for( var i = 0; i < this.rowTemplate.length;i++ )
   {
        var value = this.rowTemplate[i];        
        newRow[ newRow.length ] = value.viewTemplate.Clone({id:this.id + "_" + this.rows.length + "_" + i + "_view",parent:this});
   }
   newRow.newId = newId;
   this.AddRow("TD",  newRow, this.rows.length-1 );
}

DataGrid.prototype.SetHeader = function ()
{
    this.headers = [];
    for( var i = 0; i < this.rowTemplate.length; i++ )
        this.headers[this.headers.length] = this.rowTemplate[i].headerTemplate;
    this.AddRow("TH", this.headers , -1, CellHeaderClicked );
}

function CellHeaderClicked()
{
    Send( this.grid.id, "OnHeaderClick", this.colName );
}

DataGrid.prototype.SetRows = function ()
{
    for( var i = 0; i < this.rows.length; i++ )
    {
       var newRow = [];
       for( var j = 0; j < this.rowTemplate.length;j++ )
       {
            var value = this.rowTemplate[j];        
            newRow[ newRow.length ] = value.viewTemplate.Clone({id:this.id + "_" + i + "_" + j + "_view",parent:this});
            if( this.rows[i][j] && newRow[ newRow.length -1].SetText ) newRow[ newRow.length-1 ].SetText(this.rows[i][j] );    
       }
       this.AddRow("TD", newRow, i );
    }    
}

DataGrid.prototype.DataBind = function( rows )
{
    for( var i = 0; i < this.rows.length; i++ )
    {
        this.rows[i].rowElem.elem.parentNode.removeChild( this.rows[i].rowElem.elem);
    }
    this.pages = {};
    this.rows = rows;
    this.count = 0;
    this.pageIndex = 0;
    this.pages[this.pageIndex] = rows;
    this.SetRows();
}

DataGrid.prototype.AddRow = function( rowType, rowData, rowIndex, cellClickEvent )
{
	var eRow = new DataRow( this, rowIndex );
	for( var i = 0; i < rowData.length; i++ )
	{
		var eCell = document.createElement(rowType);
		if( rowData[i].elem )
		{
		    eCell.appendChild( rowData[i].elem );
		    eCell.newId = rowData.newId;
		    eCell.viewWidget = rowData[i];
		    if( this.rowTemplate[i].editTemplate && rowType != "TH" )
		    {
		        eCell.className = "unselectedCell";
       		    eCell.grid = this;
       		    eCell.rowIndex = rowIndex;
	    	    eCell.editTemplate = this.rowTemplate[i].editTemplate;
	    	    for( var widget in eCell.editTemplate.widgets )
	    	    {
	    	        if( eCell.editTemplate.widgets[widget].SetValue )
	    	            eCell.editTemplate.widgets[widget].SetValue();
	    	        else
	    	            console.debug("edit widget does not have a setValue:" + eCell.editTemplate.widgets[widget].id);
	    	    }
	    	    eCell.elem = rowData[i].elem;
	    	    eCell.CellOnClick = CellOnClick;
	    	    dojo.connect( eCell, "onclick", eCell, "CellOnClick");
		    }
		    if( cellClickEvent )
		    {
		        eCell.grid = this;
		        eCell.colName = this.rowTemplate[i].name;
		        eCell.onclick = cellClickEvent;
		    }
		}
		eRow.elem.appendChild( eCell );
		eRow.cells[ eRow.cells.length ] = eCell;
	}
	this.tbody.appendChild( eRow.elem );
	if( rowIndex >= 0 && rowIndex < this.rows.length ) this.rows[rowIndex].rowElem = eRow;
	this.count++;
}

DataGrid.prototype.RemoveRow = function( index )
{
    this.tbody.deleteRow( this.rows[index].rowElem.elem.rowIndex );
	this.rows.splice( index, 1 );
}

function DataRow( datagrid, index )
{
	this.grid = datagrid;
	this.index = index;
	this.elem = document.createElement("TR");
	this.elem.className = this.grid.rowClass;
	dojo.connect( this.elem, "onclick", this, "RowSelect");
	//this.elem.onclick = this.RowSelect.bindAsEventListener(this);
	this.cells = [];
}

DataRow.prototype.RowSelect = function()
{
    if( ! this.grid.selIndex ) this.grid.selIndex = this.index;
    else if( this.grid.selIndex == this.index ) return;
    this.grid.selIndex = this.index;
    if( this.grid.lastSelectedRow ) this.grid.lastSelectedRow.elem.className = this.grid.rowClass;
    if( this.grid.selectedRowClass ) this.elem.className = this.grid.selectedRowClass;
    this.grid.lastSelectedRow = this;
	Send( this.grid.id, "OnRowSelect",this.index  );
}

DataGrid.prototype.ChangePage = function ( args )
{
    for( var i = 0; i < this.rows.length;i++ )
    {
        this.rows[i].rowElem.elem.style.display="none";
    }
    
    if( !this.pages[this.pageIndex] )
        this.pages[this.pageIndex] = this.rows;
    
    this.pageIndex = args.pageIndex;
    
    if( !this.pages[this.pageIndex] )
        this.rows = args.rows;
    else
        this.rows = this.pages[this.pageIndex]
    
    this.SetRows();
}

function CellOnClick(evt)
{
    if( this.grid.currentCell )
    {
        dojo.connect( this.grid.currentCell, "onclick", this.grid.currentCell, "CellOnClick");
        this.grid.currentCell.editElem.style.display = "none";
        this.grid.currentCell.elem.style.display = "";   
        this.grid.currentCell.className = "unselectedCell";
        this.onkeydown = null;
        //Send( this.grid.id, "OnEdit", "{rowIndex:" + this.grid.currentCell.rowIndex + ",cellIndex:" + 
    }
    this.grid.currentCell = this;
    this.className = "selectedCell";
    if( ! this.editElem && this.editTemplate)
    {
        var newString = this.newId ? "_newId_" + this.newId : "";
        this.editWidget = this.editTemplate.Clone({id:this.grid.id + "_" + this.rowIndex + "_" + this.cellIndex + "_edit" + newString,parent:this.grid});
        this.editWidget.evt = "OnEdit";
        this.editWidget.elem.id = this.editWidget.id;
        if( this.editWidget.SetValue && this.grid.rows[ this.rowIndex ] )
        {
            this.editWidget.SetValue( this.grid.rows[ this.rowIndex ][ this.cellIndex ], this.clientWidth );
        }
        this.editElem = this.editWidget.elem;        
        this.appendChild( this.editElem );
    }
    else
    {
        this.editElem.style.display = "block";
    }
    this.CellKeyPressHandler = CellKeyPressHandler;
    dojo.connect( this.editElem, "onkeydown", this, "CellKeyPressHandler");
    this.elem.style.display = "none";
    this.editElem.focus();
    dojo.event.disconnect( this, "onclick", this, "CellOnClick");
    //if(evt && evt.preventBubble )evt.preventBubble();
}

function CellKeyPressHandler( evt )
{
    if( ! evt ) evt = window.event;
    if( ! evt.ctrlKey && evt.keyCode != Event.KEY_TAB) return;
    var update = true;
    switch( evt.keyCode )
    {
        case Event.KEY_LEFT:           
            this.grid.Select( this.cellIndex -1, this.rowIndex );
            break;
        case Event.KEY_RIGHT:
        case Event.KEY_TAB:
            this.grid.Select( this.cellIndex +1, this.rowIndex );
            break;
        case Event.KEY_UP:
            this.grid.Select( this.cellIndex, this.rowIndex-1 );
            break;
        case Event.KEY_DOWN:
            this.grid.Select( this.cellIndex, this.rowIndex+1 );
            break;
        default:
            update = false;
    }
    if( update && this.editElem.onchange ) this.editElem.onchange();
}

DataGrid.prototype.Select = function ( x, y )
{
    if( this.rows[ y ] && this.rows[ y ].rowElem.cells[ x ] )
    {
        var cell = this.rows[ y ].rowElem.cells[ x ];
        if( cell && cell.onclick ) cell.onclick();
    }
}

DataGrid.prototype.AddCell = function ( rowIndex, cellIndex, id )
{
	var td = this.rows[ rowIndex ].element.insertCell( cellIndex );
	this.rows[ rowIndex ].cells[ id ] = td;
}

co.inherits(Widget);
function co(args)
{
    this.elemType = "div";
    this.Widget(args);
    this.elem.style.width = 1;
    this.elem.style.height = 1;
    this.elem.style.visibility = 'hidden';
    this.cometUrl = "http://" + document.location.hostname + ":6668/" + getSendUrl() + "?sid=" + sessionId;
    this.elem.innerHTML = '<object id="cometflash" type="application/x-shockwave-flash"' +
		'data="/Themes/Default/Scripts/Comet.swf"' + 
		'width="100" height="100">' +
		'<param name="movie"' +
		'value="/Themes/Default/Scripts/Comet.swf" />' +
		'<div id="noflash" style="width:1px;height:1px"></div>' +
		'</object>';	
	cometHandler = this;
}

var cometHandler;
co.prototype.Render = function(index) 
{
	this.uber("Render", index);
	this.comet = dojo.byId("cometflash");
	this.noflash = dojo.byId("noflash");
	if( this.noflash.offsetWidth == 1 )
	{
		console.log( "no flash found" );
		this.noflash.innerHTML = '<iframe src="' + this.cometUrl + '"></iframe>';
	}
}

function connectComet()
{
	console.log("connecting comet");
	cometHandler.comet.connect(document.location.hostname, getSendUrl(), cometHandler.port, sessionId);
	console.log("done");
}	

p.inherits(Widget);
function p( args )
{
	if( ! this.elemType ) this.elemType = "DIV";
    //this.className = "pane";
    this.Widget(args);
	if( this.left ) this.elem.style.left = this.left;
	if( this.top ) this.elem.style.top = this.top;
	if( this.width ) this.elem.style.width = this.width;
	if( this.height ) this.elem.style.height = this.height;
}

FlashPane.inherits( p );
function FlashPane( args )
{
    this.p( args );
    var flashHTML = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="500" height="500" id="testXml-01" align="middle"><param name="allowScriptAccess" value="sameDomain" /> <param name="movie" value="%{src}" /><param name="quality" value="high" /><param name="bgcolor" value="#ffffff" /><embed src="%{src}" quality="high" bgcolor="#ffffff" width="500" height="500" name="testXml-01" align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />';
    this.elem.innerHTML = dojo.string.paramString( flashHTML, {src:args.src} );
}

function getHost()
{
    return document.location.hostname;
}

var documentWriteBuffer = null;
document.writeln = document.write = function(str){documentWriteBuffer+=str;}

Browser.inherits( p );
function Browser( args )
{
    var _this = this;
    this.p(args);
	
	this.GetValue = function() {  { return _this.source; } }
	this.hostBaseUrl = getRoot(); //getSchemeAndDomain();
	this.hostAbsoluteBaseUrl = getSchemeAndDomain();
	if( this.source ) this.source = new dojo.uri.Uri( this.source );
}
document.domain = getHost();
cssOwnerPropertyName = "ownerNode";
parentNodePropertyName = "parentNode";
if( dojo.isIE )
{
    window.onerror = function(){ return true; }
    cssOwnerPropertyName = "owningElement";
    parentNodePropertyName = "parentElement";
}
Browser.prototype.Paint = function(data)
{
    //data = "<scr" + "ipt>document.domain = '" + getHost() + "'</sc" + "ript>" + data;
    //this.elem.src = this.source;
    
    //console.debug( "document.styleSheets.length (before):" + document.styleSheets.length );
    
    while( document.styleSheets.length > 2 )
    {        
        document.styleSheets[document.styleSheets.length-1][cssOwnerPropertyName][parentNodePropertyName].removeChild(document.styleSheets[document.styleSheets.length-1][cssOwnerPropertyName]);
    }
    
    console.debug("document.styleSheets.length (after):" + document.styleSheets.length )
    
    this.elem.style.visibility = "hidden";
    this.elem.innerHTML = data;
    
    this.baseUrl =  this.source.scheme + '://' + this.source.authority + '/';
    if( this.source.path == '' && this.source[this.source.length-1] != '/' )
        this.source.uri += '/';
    this.frameBaseUrl =  buildRoot( this.source.uri );
    this.frameAbsoluteBaseUrl = this.source.scheme + '://' + this.source.authority + '/';
    var alinks = this.elem.getElementsByTagName('A');
    var imgs = this.elem.getElementsByTagName('IMG');
    var scripts = this.elem.getElementsByTagName('SCRIPT');
    var links = null;
    
    if( dojo.render.html.ie )
    {
        var re = /<link.*?href="?(.*?\.css)"?.*?>/gi;
        var linkStrings = data.match(re)
        var linkIndex = 0;
        if( linkStrings )
            while( linkStrings[linkIndex] )
            {
                var str = "" + linkStrings[linkIndex].toString();
                var match = /<link.*?href="?(.*?\.css)"?.*?>/gi.exec(str);
                var url = match[1];
                this.insertCssFile( this.fixUri(url) )
                linkIndex++;
            }
    }
    
    var imports = data.match(/@import\s+"(\S+)"/g);
    if( imports )
        for( var i = 0; i < imports.length; i++ )
        {
            var url = /@import\s+"(\S+)"/.exec(imports[i])[1];
            this.insertCssFile( this.fixUri(url) );
        }
   
    if( ! dojo.render.html.ie )
    {
        links = this.elem.getElementsByTagName('LINK');
    }
    
    this.fixUris( imgs, "src" );
    this.fixUris( scripts, "src" );            
    this.fixUris( alinks, "href" );
    if( links )
    {
        this.fixUris( links, "href" );
        this.fixUris( links, "HREF" );
    }
    
    for( var i = 0; i < alinks.length; i++ )
    {
        var link = alinks[i];                
        link.href = "javascript:" + this.id + ".GoTo('" + link.href +"');";
        link.target = '';
        for( var k in link )
        {
            if( k.indexOf("on") == 0 )
            {
                link[k] = null;
            }
        }
    }    
    
    for( var i  = 0; i < scripts.length; i++ )
    {
        break;
        documentWriteBuffer = '';
        var s = scripts[i];
        if( s.src )
        {
            try
            {
                var js = dojo.hostenv.getText( s.src, false, false );
                js = js.replace(/var/g,"\r\n");
                js = js.replace(/function (\S+?)\s*?\(/g,"$1 = function(")
                console.debug("external js:\r\n" + js);
                eval( js );
            }
            catch(e){console.debug("external js error:" + e.message);}
        }
        else
        {
            try
            {
                var js = s.text.replace(/function (\S+?)\s*?\(/g,"$1 = function(");
                js = js.replace(/var/g,"\r\n");
                console.debug("inline js:\r\n" + js);
                eval( js );
            }
            catch(e){console.debug("inline js error:" + e.message);}
        }
        if( documentWriteBuffer != '' )
        {
            var span = document.createElement("SPAN");
            span.innerHTML = documentWriteBuffer;
            s.parentNode.replaceChild(span, s);
        }
    }
    
    if( links )
        for( var i = 0; i < links.length;i++ )
        {
            var link = links[i];
            if( link.rel=="stylesheet" )
            {
                console.debug("inserting stylesheet: " + link.href );
                this.insertCssFile( link.href )
            }
        }
    
    this.elem.style.height = document.body.clientHeight;
    //this.elem.style.width = "70%";
    this.elem.style.visibility = "visible";
}

Browser.prototype.insertCssFile = function(filename)
{
    dojo.style.insertCssFile( filename );
    
    var styleSheets = document.styleSheets;
    for( var i = 0; i < styleSheets.length; i++ )
    {
        if(styleSheets[i][cssOwnerPropertyName].href == filename )
        {
            //setTimeout( dojo.hitch( this, "sandBoxCss", 500, styleSheets[i] );
            break;
        }
    }
}

Browser.prototype.sandBoxCss = function( sheet )
{
    for( var k in sheet )
    {
        console.debug( k );
    }
}

Browser.prototype.fixUris = function( list, prop )
{
    for( var i = 0; i < list.length; i++ )
    {
        var e = list[i];
        e[prop] = this.fixUri(e[prop]);
    }
}

Browser.prototype.fixUri = function(str)
{
    if( str && str.indexOf( "http://" ) == -1 )
    {
        if( str.charAt(0) == "/" )
        {
            str = this.frameAbsoluteBaseUrl + str;
        }
        else
        {
            str = this.frameBaseUrl + str;
        }
    }
    else if( str && str.indexOf(this.hostBaseUrl) > -1 )
    {
        str = str.replace(this.hostBaseUrl,this.frameBaseUrl) ;
    }
    else if( str && str.indexOf(this.hostAbsoluteBaseUrl) > -1 )
    {
        str = str.replace(this.hostAbsoluteBaseUrl,this.frameAbsoluteBaseUrl) ;
    }
    return str;
}

Browser.prototype.GoTo = function( url )
{
    this.source = new dojo.uri.Uri(url);
    this.OnChanged();
}

Browser.prototype.SetAttribute = function(attribute, value)
{
    if( attribute == "source" )
    {
        this.source = new dojo.uri.Uri( value );
    }
    else
    {
        this.uber("SetAttribute", attribute, value );
    }
}

function DocumentOnClick(evt)
{
	Send( "root", "OnClick", evt.clientX + "," + evt.clientY );
}

/*
svgSelectableElements = [];
SvgCircle.inherits(Widget);
function SvgCircle( args )
{
    if( dojo.isIE ){ return new ph(args); }
	this.elemNS = SVG_NS;
	this.elemType = "circle";
	//this.className = 'defaultCircle';
	this.onDelayedMouseOver = true;
	this.Widget(args);
	this.elem.setAttribute("cx", this.x );
	this.elem.setAttribute("cy", this.y );
	this.elem.setAttribute("r", this._r );
	//this.elem.setAttribute("fill", this.fill );
	this.elem.setAttribute("stroke", this.stroke );
	dojo.connect( this.elem, "onclick", this, "Click" );
	svgSelectableElements[svgSelectableElements.length] = this;
}

SvgLine.inherits(Widget);
function SvgLine( args )
{
    if( dojo.isIE ){ return new ph(args); }
	this.elemNS = SVG_NS;
	this.elemType = "line";
	this.Widget(args);
	this.elem.setAttribute("x1", this.x1 );
	this.elem.setAttribute("y1", this.y1 );
	this.elem.setAttribute("x2", this.x2 );
	this.elem.setAttribute("y2", this.y2 );
	this.elem.setAttribute("stroke", this.color );
	this.elem.onclick = this.Click;
	//this.elem.style.zIndex = -1;
}

SvgRect.inherits(Widget);
function SvgRect( args )
{
    if( dojo.isIE ){ return new ph(args); }
    this.elemNS = SVG_NS;
	this.elemType = "rect";
	this.Widget(args);
	this.elem.setAttribute("x", this.x );
	this.elem.setAttribute("y", this.y );
	this.elem.setAttribute("width", this.width );
	this.elem.setAttribute("height", this.height );
	this.elem.setAttribute("fill", this.color );
	this.elem.setAttribute("stroke", 'black' );
	this.elem.onclick = this.Click;
}

SvgPath.inherits(Widget);
function SvgPath( args )
{
    if( dojo.isIE ){ return new ph(args); }
    this.elemNS = SVG_NS;
	this.elemType = "path";
	this.Widget(args);
}
*/

/*
function(evt)
{
	var str = "Events:\r\n";
	for( var k in evt )
	{
		if( typeof( k ) != "function" )
			str += k + ": " + evt[k] + "\t\t";
	}
	alert(str);
}*/

/*
SvgImage.inherits(Widget);
function SvgImage( args )
{
    if( dojo.isIE ){ return new ph(args); }
	this.elemNS = SVG_NS;
	this.elemType = "circle";
	this.className = 'image';
	this.Widget(args);
	this.elem.setAttribute("x", this.x );
	this.elem.setAttribute("y", this.y );
	this.elem.setAttribute("width", this.width );
	this.elem.setAttribute("height", this.height );
	this.elem.setAttributeNS(XLINK_NS, "href", this.url );
	//this.elem.setAttribute("stroke", 'black' );
	this.elem.onclick = this.Click;
}

SvgGroup.inherits(Widget);
function SvgGroup( args )
{
    if( dojo.isIE ){ return new ph(args); }
	this.elemNS = SVG_NS;
	this.elemType = "g";
	this.Widget(args);
	if( this.transform) this.elem.setAttribute("transform", this.transform );
	this.elem.onclick = this.Click;
}

SvgText.inherits(Widget);
function SvgText( args )
{
    if( dojo.isIE ){ return new ph(args); }
	this.elemNS = SVG_NS;
	this.elemType = "text";
	this.Widget(args);
	this.elem.setAttribute("x", this.x );
	this.elem.setAttribute("y", this.y );
	this.elem.appendChild( document.createTextNode( this.text ) );
	this.elem.setAttribute("text-anchor", this.align );
	this.elem.setAttribute("font-size", this.fontSize );
	//this.elem.setAttribute("dominant-baseline", "mathematical");
	
	//this.elem.setAttribute("height", this.height );
	//this.elem.setAttribute("fill", this.color );
	//this.elem.setAttribute("stroke", 'black' );
}

SvgText.prototype.SetText = function (newText)
{
    this.elem.removeChild(this.elem.firstChild);
    this.elem.appendChild(document.createTextNode(newText));
}

SvgHost.inherits(Widget);
function SvgHost( args )
{
    if( dojo.isIE ){ return new ph(args); }
    this.elemNS = SVG_NS;
	this.elemType = "svg";
	this.Widget(args);
	this.elem.setAttribute("top", this.top );
	this.elem.setAttribute("left", this.left );
    this.elem.setAttribute("height", this.height );
	this.elem.setAttribute("width", this.width);
	this.elem.setAttributeNS(SVG_NS, "preserveAspectRatio", "none" );
	if( this.vb != '' )
	{
		this.elem.setAttribute("viewBox", this.vb );
	}
	
	if( this.pannableElement != null)
	{
		this.elem.pannableElement = this.pannableElement; 
		document.addEventListener( "keydown", zoom_keydown_handler, false );
		this.elem.addEventListener( "mousedown", pan_mousedown_handler, false );
	}
}

SvgGradient.inherits(Widget);
function SvgGradient( args )
{
    if( dojo.isIE ){ return new ph(args); }
    this.elemNS = SVG_NS;
    this.elemType = args.gradientType || "radialGradient";
	this.Widget(args);
	this.elem.id = args.gradientId;
	var defs = this.baseElem.getElementsByTagName("defs")
	if( defs.length == 0 )
	{
		defs = document.createElementNS( SVG_NS, "defs" );
		this.baseElem.appendChild( defs );
	}
	else
	{
	    defs = defs[0];
	}
	if( this.elemType == "radialGradient" ) 
	{
	    this.elem.setAttribute( "cx", this.cx );
	    this.elem.setAttribute("cy", this.cy );
	}
	else
	{
	    this.elem.setAttribute("x1", this.x1 );
	    this.elem.setAttribute("y1", this.y1 );
	    this.elem.setAttribute("x2", this.x2 );
	    this.elem.setAttribute("y2", this.y2 );
	}
	 
	var stopArray = this.stops.split(" ");
	for( var i = 0; i < stopArray.length; i+=2 )
	{
		var stop = document.createElementNS( SVG_NS, "stop" );
		stop.setAttribute( "offset", stopArray[i] );
		stop.setAttribute("stop-color", stopArray[i+1] );
		this.elem.appendChild( stop );
	}
	defs.appendChild( this.elem );
}

SvgGradient.prototype.Render = function(){};
*/

var gdx = 0,gdy = 0;
var tx = 0, ty = 0;
var otx = 0, oty = 0;
var pannableElement;
var panHost;
var sy = 1, scaleDeltaY = 0;
var mode = "panning";

function zoom_keydown_handler( evt )
{
	if( evt.keyCode == 17 )
	{
		mode = "zooming"
		document.addEventListener( "keyup", zoom_keyup_handler, false );
	}	
}

function zoom_keyup_handler( evt )
{
	if( evt.keyCode == 17 )
	{
		mode = "panning";
		document.removeEventListener( "keyup", zoom_keyup_handler, true );
		//alert( sy );
	}
}

function pan_mousedown_handler( evt )
{
	pannableElement = $( this.pannableElement );//this.pannableElement;
	panHost = this;
	if( mode == "panning" )
	{
		gdx = evt.clientX - tx;
		gdy = evt.clientY - ty;
	}
	else
	{
		scaleDeltaY = evt.clientY;
	}
	document.addEventListener( "mousemove", pan_move_handler, true );
	document.addEventListener( "mouseup", pan_up_handler, true );
}

var translate = "", scale = "";
var lastTransX = 0, lastTransY = 0;
function pan_move_handler(evt)
{
    if( mode == "panning" )
    {
		tx = evt.clientX - gdx;
		ty = evt.clientY - gdy;
		if( abs( tx - lastTransX ) > 15 || abs( ty - lastTransY ) > 15 )
		{
			lastTransX = tx;
			lastTransY = ty;
			translate = "translate( " + tx + "," + ty + ")";
		}
		
		if( abs( otx - tx ) > 200 || abs( oty - ty > 200 ) )
		{
			otx = tx;
			oty = ty;
			sendPan();
		}
	}
    else
	{
		sy += (evt.clientY - scaleDeltaY)/1000;
		if( sy < 0.1 )
			sy = 0.1
		scale = " scale( " + sy + ")";
	}
	pannableElement.setAttribute("transform", translate + scale );
}

function sendPan()
{
	Send( panHost.id, "OnPan", tx + "," + ty );
}

function pan_up_handler(evt)
{
    document.removeEventListener( "mousemove", pan_move_handler, true );
    document.removeEventListener( "mouseup",  pan_up_handler, true );
    if( otx != tx || oty != ty )
    {
		otx = tx;
		oty = ty;
		sendPan();
	}
}

function abs( x )
{
	if( x < 0 ) x *= -1;
	return x;
}

function attachMouseDrag(elem)
{
    elem.onmousedown = mouseDragDown;
}

var dragging = false;
function mouseDragDown(evt)
{
    if( document.onmousemove != null )
        document.onmousemove_ = document.onmousemove;
    if( document.onmouseup != null )
        document.onmouseup_ = document.onmouseup;
    this.mouseDragMove = mouseDragMove;
    this.mouseDragUp = mouseDragUp;
    dojo.connect( document, "onmousemove", this, "mouseDragMove" );
    dojo.connect( document, "onmouseup", this, "mouseDragUp" );

/* Unfortunately, this is not working for some reason.
    dojo.connect(document, "onmouseup", elem, "mouseDragUp");
    dojo.connect(document, "onmousemove", elem, "mouseDragMove"); */
    this.startX = evt.clientX;
    this.startY = evt.clientY;
    this.box = new Box( this.startX, this.startY, 1,1); 
    this.mask = createMask(this.box);
    dragging = true;
    return false;    
}

function mouseDragMove(evt)
{
    if( ! dragging ) return;
    var width = evt.clientX - this.startX;
    var height = evt.clientY - this.startY;
    if( width < 0 )
    {
        this.box.p1.x = this.startX + width;
        this.mask.style.left = this.box.p1.x;
        width *= -1;
    }
    if( height < 0 )
    {
        this.box.p1.y = this.startY + height;
        this.mask.style.top = this.box.p1.y;
        height *= -1;
    }
    this.box.width = width;
    this.box.height = height;
    this.box.p2.x = this.box.p1.x + width;
    this.box.p2.y = this.box.p1.y + height;
    this.mask.style.width = this.box.width;
    this.mask.style.height = this.box.height;
    //evt.preventBubble();
    //return false;
}

function mouseDragUp(evt)
{
    if( ! dragging ) return;
    dragging = false;
    document.onmousemove = document.onmousemove_;
    document.onmouseup = document.onmouseup_;
    this.mask.style.display = 'none';
    dojo.event.disconnect(document, "mouseup", this, "mouseDragUp");
    dojo.event.disconnect(document, "mousemove", this, "mouseDragMove");
    var newBox = Svg.translateBox( this.box, this.getScreenCTM().inverse() )
    findContainedElements(newBox);
    document.onselectstart=null;
    //evt.preventBubble();
    //return false;
}

function findContainedElements(box)
{
    var foundElems = "found:";
    for( var i = 0; i < svgSelectableElements.length; i++ )
    {
        if( Svg.elemInsideBox( svgSelectableElements[i].elem, box ) )
        {
            svgSelectableElements[i].elem.style.setProperty("fill", "yellow","important" );
        }
    }
}

function Svg(){}

//all boxes are assumed that p1 is the top left corner, and p2 is the bottom right.
Svg.elemInsideBox = function(elem, box)
{
    var eBox = Svg.extractBox(elem);
    if( Svg.pointInsideBox( eBox.p1, box ) )
        return true;
    if( Svg.pointInsideBox( eBox.p2, box ) )
        return true;
    if( Svg.pointInsideBox( new Svg.Point(eBox.p1.x, eBox.p2.y), box ) )
        return true;
    if( Svg.pointInsideBox( new Svg.Point(eBox.p2.x, eBox.p1.y), box ) )
        return true;
}

Svg.pointInsideBox = function ( p, box )
{
    return p.x > box.p1.x &&
			p.x < box.p2.x &&
			p.y > box.p1.y &&
			p.y < box.p2.y;
}

Svg.applyMatrix = function ( point, matrix )
{
    return new Svg.Point(
        matrix.a*point.x + matrix.c*point.y + matrix.e,
        matrix.b*point.x + matrix.d*point.y + matrix.f );
}

Svg.Point = function(x,y)
{
    this.x = x;
    this.y = y;
}

Svg.extractBox = function( elem )
{
    var box = elem.getBBox();
    box.p1 = new Svg.Point(box.x, box.y);
    box.p2 = new Svg.Point( box.p1.x + box.width, box.p1.y + box.height);
    return box;
}

Svg.translateBox = function( box, matrix )
{
    var box2 = {};
    box2.p1 = Svg.applyMatrix( box.p1, matrix );
    box2.p2 = Svg.applyMatrix( box.p2, matrix );
    box2.height = box2.p2.y - box2.p1.y;
    if( box2.height < 0 ) 
    {
        box2.height *= -1;
        box2.p1.y -= box2.height;
        box2.p2.y = box2.p1.y + box2.height;
        //box2.p1.y -= box2.height;
        //box2.p1.y -= window.scrollY;
    }
    box2.width = box2.p2.x - box2.p1.x;
    if( box2.width < 0 ) box2.width *= -1;
    return box2;
}

function animate()
{
    var anim = new dojo.animation.Animation( new dojo.math.curves.Line([0.2,45],[1.0,0]),1000);
    
    dojo.connect(anim, "onAnimate", function(e) {
        left_c_Group0.elem.transform.baseVal.getItem(0).setScale( e.x, -1*e.x );
	    //left_c_Group0.elem.transform.baseVal.getItem(0).setScale( e.x, -1*e.x );
	    //left_c_Group0.elem.transform.baseVal.getItem(2).setSkewY( e.y, -1*e.y );
    });
    
    anim.play();
}

var mask;
function maskElem(elem)
{
    var box = extractBox(elem);
    var matrix = elem.getScreenCTM();
    var newBox = translateBox( box, matrix );
    createMask(newBox)
}

function Box(x,y,width,height)
{
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.p1 = new Svg.Point( x,y);
    this.p2 = new Svg.Point( x+width, y+height);
}

function createMask(box)
{
    var div = document.createElement("DIV");
    div.className = "popUp";
    div.style.top = box.p1.y - window.scrollY;
    div.style.left = box.p1.x;
    div.style.width = box.width;
    div.style.height = box.height;
    document.body.appendChild(div);
    return div;
}

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

function addPostLoad(f)
{
	dojo.connect('emerge_post_load',f.toName());
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
