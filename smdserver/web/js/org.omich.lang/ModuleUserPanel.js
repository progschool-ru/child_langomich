(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var EVT_LOGOUT = "logout";
	
	var handleLogout = function (scope)
	{
		ns.ServerApi.callLogout(function (success)
		{
			if(success)
			{
				scope._dispatcher.dispatchEvent(EVT_LOGOUT, {});
			}
			else
			{
				log("Can't logout");
			}
		});
	};

	ns.ModuleUserPanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._dispatcher = new ns.EventDispatcher(this);
			this._dispatcher.addListener(EVT_LOGOUT, settings.onLogout);
			
			if($div)
			{
				this.appendTo($div);
			}
		},
		
		appendTo: function ($div)
		{
			this._super(null);
			var scope = this;

			var $liProfile = $("<li/>");
			$liProfile.addClass("header-user-profile");
			var $aProfile = $("<a/>");
			$aProfile.attr("href", "profile");
			$aProfile.append("userName");
			$liProfile.append($aProfile);
			
			var $liLogout = $("<li/>");
			$liLogout.addClass("header-user-logout");
			var $aLogout = $("<a/>");
			$aLogout.attr("href", "logout");
			$aLogout.append("Logout");
			$aLogout.click(function(event){event.preventDefault();});
			$liLogout.append($aLogout);
			$liLogout.click(function(){handleLogout(scope);})
			
			var $ul = $("<ul/>");
			$ul.addClass("header-user");
			$ul.append($liProfile);
			$ul.append($liLogout);
			
			$div.append($ul);
		}
	});
})();
