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
			this._tabsPanel = new ns.ModuleTabsPanel($tabsPanel, {
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
				onTabClick: function(evt)
				{
					scope._pageController.activatePage(evt.pageId);
				}
			});
			
			var $contentPanel = $("#org-omich-lang-contentPanel");
			this._pageController = new ns.PageController($contentPanel, {
				pages:[{
						pageId: "login",
						title: "Login",
						authType: ns.AuthTypes.NO_AUTH,
						contentPanelConstructor: ns.MessagePanel,
						contentPanelSettings: {
							message: "Welcome to LangOmich"
						}
					},{
						pageId: "register",
						title: "Register",
						authType: ns.AuthTypes.NO_AUTH,
						contentPanelConstructor: ns.MessagePanel,
						contentPanelSettings: {
							message: "Try it yourself"
						}
					},{
						pageId: "about",
						title: "About",
						authType: ns.AuthTypes.DOESNT_MATTER,
						contentPanelConstructor: ns.MessagePanel,
						contentPanelSettings: {
							message: "It's the best site in the world"
						}
					}],
				onPageChanged: function (evt)
				{
					scope._tabsPanel.setSelectedPageId(evt.pageId);
				} 
			});

			ns.ServerApi.callIsLoggedIn(function(result)
			{
				scope._pageController.activatePage("login");
			});
		}
	});
	
	ns.App.onLoad = function ()
	{
		ns.App.instance = new ns.App();
	}
})();