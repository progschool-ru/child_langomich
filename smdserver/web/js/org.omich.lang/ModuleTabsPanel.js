(function ()
{
	var ns = org.omich.nsSelf("lang");
	
	var EVT_TAB_CLICKED = "TabClicked";
	
	var handleTabClick = function (tabsPanel, tabSettings, event)
	{
		var tabEvent = {
			pageId: tabSettings.pageId,
			target: event.target
		};

		tabsPanel._dispatcher.dispatchEvent(EVT_TAB_CLICKED, tabEvent);
	}

	var initTabs = function (scope, tabs, $div)
	{
		var $ul = $("<ul/>");
		$ul.addClass("header-menu");
		for(var i = 0; i < tabs.length; ++i)
		{
			var $li = $("<li/>");
			$ul.append($li);
			
			scope._tabs.push({
					$li: $li,
					settings: tabs[i]
			});
		}	
		$div.append($ul);
	}
	
	var updateTabs = function (scope)
	{
		for(var i = 0; i < scope._tabs.length; ++i)
		{
			var $li = scope._tabs[i].$li;
			
			if(ns.AuthSettings.isCorresponding(scope._tabs[i].settings.authType,
						scope._isLoggedIn))
			{
				$li.css("display", "block");
			}
			else
			{
				$li.css("display", "none");
			}
			
			if(scope._tabs[i].settings.pageId == scope._selectedPageId)
			{
				if($li.children("span").length == 0)
				{
					$li.empty();
					var $span = $("<span/>");
					$span.addClass("current");
					$span.append(scope._tabs[i].settings.title);
					$li.append($span);
					
					$li.unbind("click");
				}
			}
			else
			{
				if($li.children("a").length == 0)
				{
					$li.empty();
					var $a = $("<a/>");
					$a.attr("href", "#" + scope._tabs[i].settings.pageId);
					$a.append(scope._tabs[i].settings.title);
					$li.append($a);
					
					$a.click(function(event){event.preventDefault();});
					(function()
					{
						var tabSettings = scope._tabs[i].settings;
						$li.click(function(event){handleTabClick(scope, tabSettings, event)});
					})();
				}
			}
		}
	}
	
	ns.ModuleTabsPanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);

			this._isLoggedIn = false;
			this._dispatcher = new ns.EventDispatcher(this);
			this._settings = settings;

			this._dispatcher.addListener(EVT_TAB_CLICKED, settings.onTabClick);

			if($div)
			{
				this.appendTo($div);
			}
		},
		
		appendTo: function ($div)
		{
			this._tabs = [];
			initTabs(this, this._settings.tabs, $div);
			updateTabs(this);
		},

		getDispatcher: function () {return this._dispatcher;},
		
		setSelectedPageId: function (pageId)
		{
			this._selectedPageId = pageId;
			updateTabs(this);
		},
		
		updateIsLoggedIn: function (isLoggedIn)
		{
			this._isLoggedIn = isLoggedIn;
			updateTabs(this);
		}
	});
})();