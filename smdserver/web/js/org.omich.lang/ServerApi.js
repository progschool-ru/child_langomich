(function ()
{
	var ns = org.omich.nsSelf("lang");	
	var unescapeFromJavaString = org.omich.Unicode.quickUnescapeFromUtf16;
	
	var PREFIX = "/smdserver/servlet/";
	var ACTION_IS_LOGGED_IN = "isLoggedIn";
	var ACTION_LOGIN        = "login";
	var ACTION_LOGOUT       = "logout";
	
	var callRequest = function (action, data, onSuccess, onError)
	{
		var url = PREFIX + action;
		jQuery.ajax({
			type: "POST",
			url: url,
			data: data,
			success: onSuccess,
			error: onError
		});
	}

	ns.ServerApi = {
		callIsLoggedIn: function (onResult)
		{
			callRequest(ACTION_IS_LOGGED_IN, null,
				function (event, textStatus, response)
				{
					var prepared = unescapeFromJavaString(response.responseText.trim());
					var obj = JSON.parse(prepared);
					onResult(obj.isLoggedIn);
				}, 
				function (){onResult(false);});
		},
		
		callLogin: function (login, password, onResult)
		{
			callRequest(ACTION_LOGOUT, {login: login, password: password},
				function (event, textStatus, response)
				{
					var prepared = unescapeFromJavaString(response.responseText.trim());
					var obj = JSON.parse(prepared);
					onResult(obj.success);
				},
				function (){onResult(false);});
		},
		
		callLogout: function (onResult)
		{
			callRequest(ACTION_LOGOUT, null,
				function (event, textStatus, response)
				{
					var prepared = unescapeFromJavaString(response.responseText.trim());
					var obj = JSON.parse(prepared);
					onResult(obj.success);
				},
				function (){onResult(false);});
		}
	};
})();

