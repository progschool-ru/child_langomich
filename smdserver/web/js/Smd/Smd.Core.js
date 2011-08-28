Smd = {}

Smd.Core = {
	addModule : function(name, module, params)
	{
		var instance = new module.Module(params);
		this._modules[name] = instance;
		instance.api = this.API;
		
		if(module.App)
		{
			instance.App = function(instanceParams)
			{
				module.App.call(this, instance.api, params, instanceParams);
			}
			instance.App.prototype = module;
		}
	},

	getModule : function(name)
	{
		return this._modules[name];
	},

	createApp : function(name, params)
	{
		if(this._modules[name] && this._modules[name].App)
		{
			this._apps.push(new this._modules[name].App(params));
			return true;
		}
		return false;
	},

	API : {
		$ : $,

		getCurrentLocation : function()
		{
			return document.location;
		},
		
		getModule : function(name)
		{
			return Smd.Core.getModule(name);
		},

		escapeToJavaString : function(value)
		{
			return Smd.Unicode.escapeToUtf16(value);
		},

		unescapeFromJavaString : function(value)
		{
			return Smd.Unicode.quickUnescapeFromUtf16(value);
		},

		ajax : function(url, settings)
		{
			settings.type = "POST";
			return jQuery.ajax(url, settings);
		}
	},
	
	_modules : {},
	_apps    : []
};