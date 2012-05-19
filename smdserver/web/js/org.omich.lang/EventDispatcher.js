(function ()
{
var ns = org.omich.nsSelf("lang");
	
ns.EventDispatcher = org.omich.Class.extend({
	init: function (target)
	{
		this._target = target;
		this.addEventListener = this.addListener;
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
			this._listeners[idEvent] = undefinded;
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
			event.currenTarget = this._target || event.target;
			arr.forEach(function (elem)
			{
				elem(event);
			});	
		}
	},
	
	_listeners: {}
});

})();
