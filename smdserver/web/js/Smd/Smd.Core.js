Smd = {}

Smd.Core = {
	addModule : function(name, module)
	{
		this._modules[name] = new module(this.API);
	},

	getModule : function(name)
	{
		return this._modules[name];
	},

	createApp : function(name, arguments)
	{
		if(this._modules[name] && this._modules[name].App)
		{
			this._apps.push(new this._modules[name].App(arguments));
			return true;
		}
		return false;
	},

	API : {
		$ : $,

		getModule : function(name)
		{
			return Smd.Core.getModule(name);
		}
	},
	
	_modules : {},
	_apps    : []
};