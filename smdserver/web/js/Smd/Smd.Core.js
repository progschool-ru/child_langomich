Smd = {}

Smd.Core = {
	addModule : function(name, module, params)
	{
		this._modules[name] = new module(this.API, params);
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

		encodeToJavaString : function(value)
		{
			return Smd.Unicode2Java.uni2java(value);
		},

		ajax : function(url, settings)
		{
			return jQuery.ajax(url, settings);
		}
	},
	
	_modules : {},
	_apps    : []
};