(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	ns.App = org.omich.Class.extend({
		init: function ()
		{
			var scope = this;
			var $tabsPanel = $("#org-omich-lang-tabsPanel");
			this._tabsPanel = new ns.ModuleTabsPanel($tabsPanel, {
				tabs:[{
					title: "Login",
					pageId: "login",
					authType: ns.AuthSettings.TYPE_NO_AUTH
				},{
					title: "Register",
					pageId: "register",
					authType: ns.AuthSettings.TYPE_NO_AUTH
				},{
					title: "About",
					pageId: "about",
					authType: ns.AuthSettings.TYPE_DOESNT_MATTER
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
						authType: ns.AuthSettings.TYPE_NO_AUTH,
						contentPanelConstructor: ns.ModuleLoginForm,
						contentPanelSettings: {
							message: "Welcome to LangOmich"
						}
					},{
						pageId: "register",
						title: "Register",
						authType: ns.AuthSettings.TYPE_NO_AUTH,
						contentPanelConstructor: ns.ModuleMessagePanel,
						contentPanelSettings: {
							message: "Try it yourself"
						}
					},{
						pageId: "about",
						title: "About",
						authType: ns.AuthSettings.TYPE_DOESNT_MATTER,
						contentPanelConstructor: ns.ModuleMessagePanel,
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
				scope.updateIsLoggedIn(result);
			});
		},
		
		updateIsLoggedIn: function (isLoggedIn)
		{
			this._pageController.updateIsLoggedIn(isLoggedIn);
			this._tabsPanel.updateIsLoggedIn(isLoggedIn);
		}
	});
	
	ns.App.onLoad = function ()
	{
		ns.App.instance = new ns.App();
	}
})();