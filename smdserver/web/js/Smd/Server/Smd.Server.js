Smd.Server = {
	Module : function(api, params)
	{
		this.api = api;
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
		return ["en", "es"];
	},

	SMD_PROTOCOL_PREFIX_LENGTH : "smd://".length
}
Smd.Server.Module.prototype = Smd.Server;
