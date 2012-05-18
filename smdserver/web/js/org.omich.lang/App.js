(function ()
{
	var ns = org.omich.nsSelf("lang");
	
	ns.App = org.omich.Class.extend({
		init: function ()
		{
			var $tabsPanel = $("#org-omich-lang-tabsPanel");
			this._modTabsPanel = new ns.ModuleTabsPanel($tabsPanel);
		}
	});
	
	ns.App.onLoad = function ()
	{
		ns.App.instance = new ns.App();
	}
})();