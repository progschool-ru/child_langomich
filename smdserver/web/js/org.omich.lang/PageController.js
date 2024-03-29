(function ()
{
	var ns = org.omich.nsSelf("lang");
	var EVT_PAGE_CHANGED = "pageChanged";
	var EVT_DIV_RESIZED = "resized";
	
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
		init: function ($contentDiv, $sideDiv, settings)
		{
			this._pages = {};
			this._settings = settings;
			this._$div = $contentDiv;
			this._$sideDiv = $sideDiv;
			this._dispatcher = new ns.EventDispatcher(this);
			this._dispatcher.addListener(EVT_PAGE_CHANGED, settings.onPageChanged);
			this._isLoggedIn = false;
			
			var scope = this;
			this._$div.getDispatcher = function(){return scope._dispatcher;}
			$(window).resize(function()
			{
				scope._dispatcher.dispatchEvent(EVT_DIV_RESIZED);
			});
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
						this._pages[pageId] = new ns.Page(ps);
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
			this._$sideDiv.empty();
			this._dispatcher.removeListeners(EVT_DIV_RESIZED);

			window.location.hash = pageId;

			if(this._pages[pageId])
			{
				this._pages[pageId].appendTo(this._$div, this._$sideDiv);
			}
			else
			{
				for(var i = 0; i < this._settings.pages.length; ++i)
				{
					var ps = this._settings.pages[i]
					if(ps.pageId == pageId)
					{
						this._pages[pageId] = new ns.Page(ps);
						this._pages[pageId].appendTo(this._$div, this._$sideDiv);
						break;
					}
				}
			}
			
			this._pageId = pageId;
			this._dispatcher.dispatchEvent(EVT_PAGE_CHANGED, {pageId:pageId, target: this});
		},
	});
})();