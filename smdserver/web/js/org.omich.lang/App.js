(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	ns.App = org.omich.Class.extend({
		init: function ()
		{
			var scope = this;
			var $tabsPanel = $("#org-omich-lang-tabsPanel");
			this._tabsPanel = new ns.ModuleTabsPanel( {
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
				onTabClick: function (evt)
				{
					scope._pageController.activatePage(evt.pageId);
				}
			}, $tabsPanel);
			
			var $contentPanel = $("#org-omich-lang-contentPanel");
			this._pageController = new ns.PageController($contentPanel, {
				pages:[{
						pageId: "login",
						title: "Login",
						authType: ns.AuthSettings.TYPE_NO_AUTH,
						contentPanelConstructor: ns.ModuleLoginForm,
						contentPanelSettings: {
							onLogin: function ()
							{
								scope.updateIsLoggedIn(true);
							}
						}
					},{
						pageId: "register",
						title: "Register",
						authType: ns.AuthSettings.TYPE_NO_AUTH,
						contentPanelConstructor: ns.ModuleRegisterForm,
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
			
			this._$userPanel = $("#org-omich-lang-userPanel");
			this._userPanel = new ns.ModuleUserPanel({
					onLogout:function (){scope.updateIsLoggedIn(false);}
				}, this._$userPanel);
			
			scope.updateIsLoggedIn(false);

			ns.ServerApi.callIsLoggedIn(function (result)
			{
				scope.updateIsLoggedIn(result);
			});
		},
		
		updateIsLoggedIn: function (isLoggedIn)
		{
			this._pageController.updateIsLoggedIn(isLoggedIn);
			this._tabsPanel.updateIsLoggedIn(isLoggedIn);

			this._$userPanel.css("display", isLoggedIn ? "block" : "none");
		}
	});
	
	ns.App.onLoad = function ()
	{
		ns.App.instance = new ns.App();
	}
})();