(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.PageController = org.omich.Class.extend({
		init: function ($div, settings)
		{
			this._pages = {};
			this._settings = settings;
			this._$div = $div;
		},
		
		activatePage: function (pageId)
		{
			this._$div.empty();

			if(this._pages[pageId])
			{
				this._pages[pageId].appendTo(this._$div);
				this._pages[pageId].refresh();
				return;
			}

			for(var i = 0; i < this._settings.pages.length; ++i)
			{
				var ps = this._settings.pages[i]
				if(ps.pageId == pageId)
				{
					this._pages[pageId] = new ps.contentPanelConstructor(ps.contentPanelSettings);
					this._pages[pageId].appendTo(this._$div);
					return;
				}
			}
		}
	});
})();