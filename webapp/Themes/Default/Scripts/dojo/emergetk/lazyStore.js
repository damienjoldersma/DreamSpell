if(!dojo._hasResource["emergetk.lazyStore"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["emergetk.lazyStore"] = true;
dojo.provide("emergetk.lazyStore");
dojo.require("dojo.data.ItemFileWriteStore");

dojo.declare("emergetk.lazyStore", dojo.data.ItemFileWriteStore, {
	constructor: function(/* object */ keywordParameters){
		// LazyLoadJSIStore extends ItemFileReadStore to implement an 
		// example of lazy-loading/faulting in items on-demand.
		// Note this is certianly not a perfect implementation, it is 
		// an example.
	},	
	
	isItemLoaded: function(/*object*/ item) {
		return dojo.isObject( item );
	},
		
	loadItem: function(keywordArgs){
		var item = keywordArgs.item;
		var widget;
		if( item.id )
			widget = window[item.id[0]];
		//else
			widget = window[item];
		Send( widget.id, "OnExpand" );
	}
});




}
