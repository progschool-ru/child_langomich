(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	var getCookie = org.omich.getCookie;
	var setCookie = org.omich.setCookie;
	
	var PID_ABOUT    = "about";
	var PID_LOGIN    = "login";
	var PID_MESSAGE  = "message";
	var PID_PROFILE  = "profile";
	var PID_REGISTER = "register";
	var PID_WORDS    = "words";
	
	var COOKIE_LOGIN = "login";
	
	
	ns.App = org.omich.Class.extend({
		init: function ()
		{
			var scope = this;
			this._model = new ns.ModelWords();
			
			var $tabsPanel = $("#org-omich-lang-tabsPanel");
			this._tabsPanel = new ns.ModuleTabsPanel( {
				tabs:[{
					title: "Login",
					pageId: PID_LOGIN,
					authType: ns.AuthSettings.TYPE_NO_AUTH
				},/*{
					title: "Register",
					pageId: PID_REGISTER,
					authType: ns.AuthSettings.TYPE_NO_AUTH
				},*/{
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
			
			var $contentDiv = $("#org-omich-lang-contentPanel");
			var $sideDiv = $("#org-omich-lang-sidePanel");
			this._pageController = new ns.PageController($contentDiv, $sideDiv,
			{
				pages:[{
						pageId: PID_LOGIN,
						title: "Login",
						authType: ns.AuthSettings.TYPE_NO_AUTH,
						contentPanelConstructor: ns.ModuleLoginForm,
						contentPanelSettings: {
							onLogin: function (login)
							{
								setCookie(COOKIE_LOGIN, login)
								scope.updateIsLoggedIn(true, login);
							}
						}
					},/*{
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
					},*/{
						pageId: PID_WORDS,
						title: "Words",
						authType: ns.AuthSettings.TYPE_AUTH,
						contentPanelConstructor: ns.ModuleWordsPanel,
						contentPanelSettings: {model: this._model},
						sidePanelConstructor: ns.ModuleAddWordsPanel,
						sidePanelSettings: {
							model: this._model,
							onResetLanguages: function(){scope.updateWordsPanel()}
						}
					},{
						pageId: PID_ABOUT,
						title: "About",
						authType: ns.AuthSettings.TYPE_DOESNT_MATTER,
						contentPanelConstructor: ns.ModuleIFramePanel,
						contentPanelSettings: {src: "about.html"}
					},{
						pageId: PID_MESSAGE,
						title: "Messages",
						authType: ns.AuthSettings.TYPE_DOESNT_MATTER,
						contentPanelConstructor: ns.ModuleMessagePanel
					},{
						pageId: PID_PROFILE,
						title: "Profile",
						authType: ns.AuthSettings.TYPE_AUTH,
						contentPanelConstructor: ns.ModuleProfileForm
					}],
				onPageChanged: function (evt)
				{
					scope._tabsPanel.setSelectedPageId(evt.pageId);
				} 
			});
			
			this._$userPanel = $("#org-omich-lang-userPanel");
			this._userPanel = new ns.ModuleUserPanel({
					onLogout:function (){scope.updateIsLoggedIn(false);},
					onProfile:function ()
					{
						scope._pageController.activatePage(PID_PROFILE);
					}
				}, this._$userPanel);
			
			scope.updateIsLoggedIn(false);

			this.refreshIsLoggedIn();
		},
		
		refreshIsLoggedIn: function ()
		{
			var scope = this;
			ns.ServerApi.callIsLoggedIn(function (result)
			{
				scope.updateIsLoggedIn(result, getCookie(COOKIE_LOGIN));
			});
		},
		
		updateIsLoggedIn: function (isLoggedIn, login)
		{
			this._pageController.updateIsLoggedIn(isLoggedIn);
			this._tabsPanel.updateIsLoggedIn(isLoggedIn);

			this._userPanel.setLogin(isLoggedIn ? login : "");
			this._$userPanel.css("display", isLoggedIn ? "block" : "none");
		},
		
		showMessagePage: function (key)
		{
			this._pageController.getPageById(PID_MESSAGE).getContentPanel().setMessage(key);
			this._pageController.activatePage(PID_MESSAGE);
		},
		
		updateWordsPanel: function (key)
		{
			this._pageController.getPageById(PID_WORDS).getContentPanel().refresh();
		}
	});
	
	ns.App.onLoad = function ()
	{
		ns.App.instance = new ns.App();
	}
})();