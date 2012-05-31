(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var PID_ABOUT    = "about";
	var PID_LOGIN    = "login";
	var PID_MESSAGE  = "message";
	var PID_REGISTER = "register";
	var PID_WORDS    = "words";
	
	
	ns.App = org.omich.Class.extend({
		init: function ()
		{
			var scope = this;
			var $tabsPanel = $("#org-omich-lang-tabsPanel");
			this._tabsPanel = new ns.ModuleTabsPanel( {
				tabs:[{
					title: "Login",
					pageId: PID_LOGIN,
					authType: ns.AuthSettings.TYPE_NO_AUTH
				},{
					title: "Register",
					pageId: PID_REGISTER,
					authType: ns.AuthSettings.TYPE_NO_AUTH
				},{
					title: "Words",
					pageId: PID_WORDS,
					authType: ns.AuthSettings.TYPE_AUTH
				},{
					title: "About",
					pageId: PID_ABOUT,
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
						pageId: PID_LOGIN,
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
						pageId: PID_REGISTER,
						title: "Register",
						authType: ns.AuthSettings.TYPE_NO_AUTH,
						contentPanelConstructor: ns.ModuleRegisterForm,
						contentPanelSettings: {
							onRegisterAnswer: function (key)
							{
								scope.showMessagePage(key);
								scope.refreshIsLoggedIn();
							}
						}
					},{
						pageId: PID_ABOUT,
						title: "About",
						authType: ns.AuthSettings.TYPE_DOESNT_MATTER,
						contentPanelConstructor: ns.ModuleMessagePanel,
						contentPanelSettings: {
							message: "It's the best site in the world"
						}
					},{
						pageId: PID_WORDS,
						title: "Words",
						authType: ns.AuthSettings.TYPE_AUTH,
						contentPanelConstructor: ns.ModuleWordsPanel
					},{
						pageId: PID_MESSAGE,
						title: "Messages",
						authType: ns.AuthSettings.TYPE_DOESNT_MATTER,
						contentPanelConstructor: ns.ModuleMessagePanel
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

			this.refreshIsLoggedIn();
		},
		
		refreshIsLoggedIn: function ()
		{
			var scope = this;
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
		},
		
		showMessagePage: function (key)
		{
			this._pageController.getPageById(PID_MESSAGE).setMessage(key);
			this._pageController.activatePage(PID_MESSAGE);
		}
	});
	
	ns.App.onLoad = function ()
	{
		ns.App.instance = new ns.App();
	}
})();