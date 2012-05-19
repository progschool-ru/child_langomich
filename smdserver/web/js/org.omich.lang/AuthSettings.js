(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.AuthSettings = {
		TYPE_AUTH: 0,
		TYPE_NO_AUTH: 1,
		TYPE_DOESNT_MATTER: 2,
		
		isCorresponding: function (type, isLoggedIn)
		{
			switch(type)
			{
				case this.TYPE_AUTH: return isLoggedIn;
				case this.TYPE_NO_AUTH: return !isLoggedIn;
				case this.TYPE_DOESNT_MATTER: return true;
			}
		}
	}
})();
