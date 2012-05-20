(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.ModuleMessagePanel = ns.ModuleAbstract.extend({
		init: function (settings)
		{
			this._message = settings.message;
		},
		appendTo: function ($div)
		{
			$div.append(this._message);
		},
		refresh: function (){}
	});
})();
