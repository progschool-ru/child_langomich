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
		var postfix =  "?_dc=" + new Date().getTime();
		this.api.ajax(this.getUrl("smd://action/getLanguages") + postfix,
			{
				async : async,
				context : this,
				success : function(event, textStatus, response){
					var preparing = this.api.unescapeFromJavaString(response.responseText.trim());
					var responseObject = JSON.parse(preparing);
					this._languages = responseObject.languages;
				}
			});
	},

	_languages : null,

	SMD_PROTOCOL_PREFIX_LENGTH : "smd://".length
}
Smd.Server.Module.prototype = Smd.Server;
