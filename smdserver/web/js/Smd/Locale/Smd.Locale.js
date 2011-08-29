Smd = window.Smd || {};
Smd.Locale = {
	Module: function()
	{
		this.Translation();
	},
	
	get: function(key)
	{
		return this._values[key];
//		var keys = key.split(".");
//		var target = this._values;
//		for(var i in keys)
//		{
//			target = target[keys[i]]
//			if(!target)
//			{
//				return null;
//			}
//		}
//		return target;
	},
	
	set: function(key, value)
	{
		if(!key)
			return;
		this._values[key] = value;
//		
//		var keys = key.split(".");
//		var target = this._values;
//		var i;
//		for(i = 0; i < keys.length - 1; i++)
//		{
//			target[keys[i]] = target[keys[i]] || {};
//			target = target[keys[i]];
//		}
//		target[keys[keys.length -1]] = value;		
	},
	
	_values: {}
}
Smd.Locale.Module.prototype = Smd.Locale;


