(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.ModuleMessagePanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);

			this._message = settings.message;
			if($div)
			{
				this.appendTo($div);
			}
		},
		appendTo: function ($div)
		{
			$div.append(this._message);
		},
		refresh: function (){}
	});
})();
