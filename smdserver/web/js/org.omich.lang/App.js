(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	ns.AuthTypes = {
		AUTH: 0,
		NO_AUTH: 1,
		DOESNT_MATTER: 2
	};
	
	ns.App = org.omich.Class.extend({
		init: function ()
		{
			var scope = this;
			var $tabsPanel = $("#org-omich-lang-tabsPanel");
			this._modTabsPanel = new ns.ModuleTabsPanel($tabsPanel, {
				tabs:[{
					title: "Login",
					pageId: "login",
					authType: ns.AuthTypes.NO_AUTH
				},{
					title: "Register",
					pageId: "register",
					authType: ns.AuthTypes.NO_AUTH
				},{
					title: "About",
					pageId: "about",
					authType: ns.AuthTypes.DOESNT_MATTER
				}],
				onTabClick: function(evt){scope.selectPage(evt);}
			});
		},
		
		selectPage: function (evt)
		{
			window.location.hash = evt.pageId;
		}
	});
	
	ns.App.onLoad = function ()
	{
		ns.App.instance = new ns.App();
	}
})();