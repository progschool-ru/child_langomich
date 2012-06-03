(function ()
{
var ns = org.omich.nsSelf("lang");
	
ns.EventDispatcher = org.omich.Class.extend({
	init: function ()
	{
		this.addEventListener = this.addListener;
		this._listeners = {};
	},

	addListener: function (idEvent, onEvent)
	{
		if(!onEvent)
			return;

		var arr = this._listeners[idEvent] = this._listeners[idEvent] || [];

		if(arr.indexOf(onEvent) < 0)
		{
			arr.push(onEvent);
		}
	},

	removeListener: function (idEvent, onEvent)
	{
		var arr = this._listeners[idEvent];
		
		var i = arr ? arr.indexOf(onEvent) : -2;
		if(i >= 0)
		{
			arr.splice(i, 1);
		}
	},

	removeListeners: function (idEvent)
	{
		if(idEvent)
		{
			delete this._listeners[idEvent];
		}
		else
		{
			this._listeners = {};
		}
	},

	dispatchEvent: function (idEvent, event)
	{
		var arr = this._listeners[idEvent];
		if(arr)
		{
			arr.forEach(function (elem)
			{
				elem(event);
			});	
		}
	},
	
	_listeners: {}
});

})();
