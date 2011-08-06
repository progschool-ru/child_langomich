Smd.Server = {
	Module : function(api, params)
	{
		this.api = api;
		this._servletPaths = params.servletPaths;
		this._basePath = params.basePath;

		this._loadWords(false);
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
		return this._languages;
	},

	_loadWords : function(async)
	{
		var postfix =  "?_dc=" + new Date().getTime();
		this.api.ajax(this.getUrl("smd://action/getWords") + postfix,
			{
				async : async,
				context : this,
				success : function(event, textStatus, response){
					var preparing = this.api.unescapeFromJavaString(response.responseText.trim());
					this._words = JSON.parse(preparing);
					this._languages = this._words.languages;
				}
			});
	},

	_words : null,
	_languages : [],

	SMD_PROTOCOL_PREFIX_LENGTH : "smd://".length
}
Smd.Server.Module.prototype = Smd.Server;
