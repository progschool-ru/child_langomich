(function ()
{
	var ns = org.omich.nsSelf("lang");
	var EVT_PAGE_CHANGED = "pageChanged";

	ns.PageController = org.omich.Class.extend({
		init: function ($div, settings)
		{
			this._pages = {};
			this._settings = settings;
			this._$div = $div;
			this._dispatcher = new ns.EventDispatcher(this);
			this._dispatcher.addListener(EVT_PAGE_CHANGED, settings.onPageChanged);
			this._isLoggedIn = false;
		},

		getDispatcher: function () {return this._dispatcher;},
		
		updateIsLoggedIn: function (isLoggedIn)
		{
			this._isLoggedIn = isLoggedIn;
		},
		
		activatePage: function (pageId)
		{
			pageId = pageId || this._pageId;
			this._$div.empty();

			window.location.hash = pageId;

			if(this._pages[pageId])
			{
				this._pages[pageId].appendTo(this._$div);
				this._pages[pageId].refresh();
			}
			else
			{
				for(var i = 0; i < this._settings.pages.length; ++i)
				{
					var ps = this._settings.pages[i]
					if(ps.pageId == pageId)
					{
						this._pages[pageId] = new ps.contentPanelConstructor(ps.contentPanelSettings);
						this._pages[pageId].appendTo(this._$div);
						break;
					}
				}
			}
			
			this._pageId = pageId;
			this._dispatcher.dispatchEvent(EVT_PAGE_CHANGED, {pageId:pageId, target: this});
		}
	});
})();