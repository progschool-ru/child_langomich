(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.MessagePanel = org.omich.Class.extend({
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
