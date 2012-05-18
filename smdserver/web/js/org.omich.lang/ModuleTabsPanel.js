(function ()
{
	var ns = org.omich.nsSelf("lang");

	var initTabs = function (tabs, $div)
	{
		var $ul = $("<ul/>");
		$ul.addClass("header-menu");
		for(var i = 0; i < tabs.length; ++i)
		{
			var $li = $("<li/>");
			var $a = $("<a/>");
			$a.attr("href", "#page/" + tabs[i].pageId);
			$a.append(tabs[i].title);
			$li.append($a);
			$ul.append($li);
		}
		
		$div.append($ul);
	}
	
	ns.ModuleTabsPanel = org.omich.Class.extend({
		init: function ($div, settings)
		{
			this._$div = $div;
			initTabs(settings.tabs, $div);
		},
	});
})();