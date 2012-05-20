(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.ModuleUserPanel = ns.ModuleAbstract.extend({
		init: function ($div)
		{
			this._super(null);

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
			$liLogout.append($aLogout);
			
			var $ul = $("<ul/>");
			$ul.addClass("header-user");
			$ul.append($liProfile);
			$ul.append($liLogout);
			
			this._$ul = $ul;
			
			if($div)
			{
				this.appendTo($div);
			}
		},
		
		appendTo: function ($div)
		{
			$div.append(this._$ul);
		}
	});
})();
