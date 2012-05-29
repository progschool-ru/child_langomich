(function ()
{
	var ns = org.omich.nsSelf("lang");	
	var unescapeFromJavaString = org.omich.Unicode.quickUnescapeFromUtf16;
	
	var PREFIX = "/smdserver/servlet/";
	var ACTION_IS_LOGGED_IN = "isLoggedIn";
	var ACTION_LOGIN        = "login";
	var ACTION_LOGOUT       = "logout";
	var ACTION_REGISTER     = "register";
	
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
					try
					{
						var prepared = unescapeFromJavaString(response.responseText.trim());
						var obj = JSON.parse(prepared);
						onResult(obj.isLoggedIn);
					}
					catch (e)
					{
						log(e);
						onResult(false);
					}
				}, 
				function (){onResult(false);});
		},
		
		callLogin: function (login, password, onResult)
		{
			callRequest(ACTION_LOGIN, {login: login, password: password},
				function (event, textStatus, response)
				{
					try
					{
						var prepared = unescapeFromJavaString(response.responseText.trim());
						var obj = JSON.parse(prepared);
						onResult(obj.success);
					}
					catch (e)
					{
						log(e);
						onResult(false);
					}
				},
				function (){onResult(false);});
		},
		
		callLogout: function (onResult)
		{
			callRequest(ACTION_LOGOUT, null,
				function (event, textStatus, response)
				{
					try
					{
						var prepared = unescapeFromJavaString(response.responseText.trim());
						var obj = JSON.parse(prepared);
						onResult(obj.success);
					}
					catch(e)
					{
						log(e)
						onResult(false);
					}
				},
				function (){onResult(false);});
		},
		
		callRegister: function (login, password, email, about, onResult)
		{
			callRequest(ACTION_REGISTER, {
					login:    login,
					password: password,
					email:    email,
					about:    about
				},
				function (event, textStatus, response)
				{
					try
					{
						var prepared = unescapeFromJavaString(response.responseText.trim());
						var obj = JSON.parse(prepared);
						onResult(obj.success, obj.key);
					}
					catch (e)
					{
						log(e);
						onResult(false);
					}
				},
				function (){onResult(false);});
		}
	};
})();

