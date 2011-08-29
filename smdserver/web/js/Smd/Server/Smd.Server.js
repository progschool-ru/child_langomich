Smd = window.Smd || {};
Smd.Server = {
	Module : function(params)
	{
		this._servletPaths = params.servletPaths;
		this._basePath = params.basePath;
	},

	getUrl : function(internalUrl)
	{
		var servletLength = internalUrl.indexOf("/", this.SMD_PROTOCOL_PREFIX_LENGTH);
		var servlet = internalUrl.substring(this.SMD_PROTOCOL_PREFIX_LENGTH, servletLength);
		var action = internalUrl.substring(servletLength);
		return this._basePath + this._servletPaths[servlet] + action;
	},
	
	ajax : function(innerUrl, settings)
	{
		var url = this.getUrl(innerUrl);
		var api = this.api;
		
		settings.type = "POST";
		var success = settings.success;
		var error = settings.error;

		settings.success = function(event, textStatus, response)
		{
			var preparing = api.unescapeFromJavaString(response.responseText.trim());
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
			api.log("error during request: " + url)
			api.log(jqXHR);
			api.log(textStatus);
			api.log(errorThrown);
			error.call(settings.context);
		}
		
		return this.api.ajax(url, settings);
	},

	getLanguages : function()
	{
		if(!this._languages)
		{
			this._loadLanguages(false);
		}
		return this._languages;
	},

	_loadLanguages : function(async)
	{
		this.ajax("smd://action/getLanguages",
			{
				async : async,
				context : this,
				success : function(responseObject)
				{
					this._languages = responseObject.languages;
				}
			});
	},

	_languages : null,

	SMD_PROTOCOL_PREFIX_LENGTH : "smd://".length
}
Smd.Server.Module.prototype = Smd.Server;
