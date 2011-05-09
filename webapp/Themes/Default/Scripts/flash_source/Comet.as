package com.skullsquad.comet {
	import flash.display.Sprite;
	import flash.utils.Endian;
	import flash.system.Security;
	import flash.net.*;
	import flash.events.*;
	import flash.external.*;
	import com.adobe.serialization.json.JSON;

	public class Comet extends Sprite {
		
		public function Comet()
		{
			stage.scaleMode = "noScale";
			ExternalInterface.addCallback( "connect", this.connect);
			ExternalInterface.addCallback( "send", this.send);
			ExternalInterface.call("connectComet");
		}

		public function log(...arguments)
		{
			ExternalInterface.call( "console.log", arguments );
			trace( arguments );
		}


		public function connect(host:String, path:String, port:Number, sessionId:String)
		{
			this.host = host;
			this.path = path;
			this.port = port || 6668;
			this.sessionId = sessionId;
			setupSocket();
		}

		var host:String;
		var path:String;
		var port:Number;
		//var socket:XMLSocket;
		var socket:Socket;
		var sessionId:String;
		public function setupSocket()
		{
			log("security type: ", Security.sandboxType );
//			Security.loadPolicyFile( "http://localhost:8080/crossdomain.xml" );
			//log("loading policy file: xmlsocket://localhost:6667 " );
			Security.loadPolicyFile("xmlsocket://localhost:6667"); 
			//log("done.");
//			Security.allowDomain("localhost")
			log( "setting up Socket (NO XML)", host, path, port, sessionId );
			//socket = new XMLSocket();
			socket = new Socket(); //"localhost",6668);
			//socket.endian = Endian.LITTLE_ENDIAN;
			socket.addEventListener( Event.CONNECT, socket_connected );
			socket.addEventListener( Event.CLOSE, socket_closed );
			socket.addEventListener( IOErrorEvent.IO_ERROR, socket_error );
			socket.addEventListener( DataEvent.DATA, socket_data );xmlsocket://localhost:6667
//			socket.addEventListener( DataReceived.eventType, socket_data);
			socket.addEventListener( ProgressEvent.SOCKET_DATA, socket_data );
			socket.addEventListener( SecurityErrorEvent.SECURITY_ERROR, socket_error );
			socket.connect("", 6668);
			log("done setting up (non-XML) socket");
		}

		public function socket_closed(e:Event)
		{
			log( "socket closed ", e );
			//Output.trace("closed: ", e);
		}

		public function socket_error(e:Event)
		{
			log( "socket error ", e, socket );
			//Output.trace("error: " + e);
		}

		public function socket_connected( e:Event )
		{
			log("connected: ", e);
			socket.writeUTF("GET " + path + "?sid=" + sessionId + "&flash=1\n\n");
		}

		//public function socket_data( e:DataEvent )
		public function socket_data( e:ProgressEvent )
		{
			var data = socket.readUTFBytes(socket.bytesAvailable);
			log( "received data", e, data );		
			//log("testing", e.data.replace(/\\/g,"\\\\") );
			ExternalInterface.call( "receive", e, data);
		}

		public function send( o )
		{
			var s:String = JSON.encode(o);
			log("sending data over socket: " + s)
			socket.writeUTF(s);
		}
	}
}
