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
	
	setServerModuleName : function(name)
	{
		this._serverModuleName = name;
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
		
		log : function(message)
		{
			console.log(message);
		},

		ajax : function(innerUrl, settings)
		{
			var core = Smd.Core;
			var scope = this;
			var url = core._modules[core._serverModuleName].getUrl(innerUrl);
			
			settings.type = "POST";
			
			var success = settings.success;
			var error = settings.error;
			
			settings.success = function(event, textStatus, response)
			{
				var preparing = scope.unescapeFromJavaString(response.responseText.trim());
				var answer = JSON.parse(preparing);

				if(answer.success)
				{
					success.call(settings.context, answer);
				}
				else
				{
					error.call(settings.context, answer);
				}				
			}

			settings.error = function(jqXHR, textStatus, errorThrown)
			{
				scope.log("error during request: " + url)
				scope.log(jqXHR);
				scope.log(textStatus);
				scope.log(errorThrown);
				error.call(settings.context);
			}

			return jQuery.ajax(url, settings);
		}
	},
	
	_modules : {},
	_apps    : [],
	_serverModuleName : null
};