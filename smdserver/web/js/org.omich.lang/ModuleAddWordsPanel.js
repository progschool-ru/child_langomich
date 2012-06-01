(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.ModuleAddWordsPanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			
			if(!settings || !settings.message)
				return;

			if($div)
			{
				this.appendTo($div);
			}
		},
		appendTo: function ($div){$div.append("hello");},
		refresh: function (){}
	});
})();
