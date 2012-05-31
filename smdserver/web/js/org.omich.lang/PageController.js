(function ()
{
	var ns = org.omich.nsSelf("lang");
	var EVT_PAGE_CHANGED = "pageChanged";
	
	var checkAuth = function (scope, pageId)
	{
		var ps = scope._settings.pages;
		var anotherPageId;
		for(var i = 0; i < ps.length; ++i)
		{
			if(ns.AuthSettings.isCorresponding(ps[i].authType, scope._isLoggedIn))
			{
				if (ps[i].pageId == pageId)
				{
					return pageId;
				}
				else if(!anotherPageId)
				{
					anotherPageId = ps[i].pageId;
				}
			}
		}
		
		return anotherPageId;
	}

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
			this.activatePage();
		},
		
		getPageById: function (pageId)
		{
			if(!this._pages[pageId])
			{
				for(var i = 0; i < this._settings.pages.length; ++i)
				{
					var ps = this._settings.pages[i];
					if(ps.pageId == pageId)
					{
						this._pages[pageId] = new ps.contentPanelConstructor(ps.contentPanelSettings);
						break;
					}
				}
			}

			return this._pages[pageId];
		},
		
		activatePage: function (pageId)
		{
			pageId = pageId || this._pageId;	
			pageId = checkAuth(this, pageId);
			
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
		},
	});
})();