(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.ModuleMessagePanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			this._$messageDiv = $("<div/>");
			
			if(!settings || !settings.message)
				return;

			this._$messageDiv.append(settings.message);

			if($div)
			{
				this.appendTo($div);
			}
		},
		appendTo: function ($div){$div.append(this._$messageDiv);},
		setMessage: function (message)
		{
			this._$messageDiv.empty();
			this._$messageDiv.append(message);
		},
		refresh: function (){}
	});
})();
