(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var EVT_TAB_CLICKED = "TabClicked";
	
	var handleTabClick = function (tabsPanel, tabSettings, event)
	{
		var tabEvent = {
			pageId: tabSettings.pageId,
			target: event.target
		};

		tabsPanel._dispatcher.dispatchEvent(EVT_TAB_CLICKED, tabEvent);
	}

	var initTabs = function (tabsPanel, tabs, $div)
	{
		var $ul = $("<ul/>");
		$ul.addClass("header-menu");
		for(var i = 0; i < tabs.length; ++i)
		{
			var $li = $("<li/>");
			var $a = $("<a/>");
			$a.attr("href", "#" + tabs[i].pageId);
			$a.append(tabs[i].title);
			$li.append($a);
			$ul.append($li);
			
			$a.click(function(event){event.preventDefault();});
			(function()
			{
				var tabSettings = tabs[i];
				$li.click(function(event){handleTabClick(tabsPanel, tabSettings, event)});
			})();
		}
		
		$div.append($ul);
	}
	
	ns.ModuleTabsPanel = org.omich.Class.extend({
		init: function ($div, settings)
		{
			this._dispatcher = new ns.EventDispatcher(this);
			this._$div = $div;
			initTabs(this, settings.tabs, $div);
			
			this._dispatcher.addListener(EVT_TAB_CLICKED, settings.onTabClick);
		},

		getDispatcher: function () {return this._dispatcher;}
	});
})();