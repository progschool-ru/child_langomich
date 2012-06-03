(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	var isArray = jQuery.isArray;
	var unescapeFromJavaString = org.omich.Unicode.quickUnescapeFromUtf16;
	var escapeToJavaString = org.omich.Unicode.escapeToUtf16;
	
	var PREFIX = "/smdserver/servlet/";
	var ACTION_ADD_WORDS    = "addWords";
	var ACTION_DELETE_WORDS = "deleteWords";
	var ACTION_GET_WORDS    = "getWords";
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
	};
	
	var parseJson = function (response)
	{
		var prepared = unescapeFromJavaString(response.responseText.trim());
		return JSON.parse(prepared);
	};

	ns.ServerApi = {
		callIsLoggedIn: function (onResult)
		{
			callRequest(ACTION_IS_LOGGED_IN, null,
				function (event, textStatus, response)
				{
					try
					{
						var obj = parseJson(response);
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
						var obj = parseJson(response);
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
						var obj = parseJson(response);
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
		
		callDeleteWords: function (wordForeign, languageId, onResult)
		{
			var wordsParam = isArray(wordForeign) ? wordForeign : [wordForeign];
			var strWordsParam = escapeToJavaString(JSON.stringify(wordsParam));
			
			callRequest(ACTION_DELETE_WORDS, {words:strWordsParam, languageId:languageId},
					function(event, textStatus, response)
					{
						var obj = parseJson(response);
						onResult(obj.success);
					},
					function(e)
					{
						onResult(false);
					})
		},
		
		callAddWords: function (data, onResult)
		{
			var dataStr = escapeToJavaString(JSON.stringify(data));

			callRequest(ACTION_ADD_WORDS, {data: dataStr},
					function(event, textStatus, response)
					{
						var obj = parseJson(response);
						onResult(obj.success);
					},
					function(e)
					{
						onResult(false);
					})
		},
		
		callGetWords: function (onResult)
		{
			callRequest(ACTION_GET_WORDS, null,
				function (event, textStatus, response)
				{
					try
					{
						var obj = parseJson(response);
						onResult(obj.success ? obj : false);
					}
					catch (e)
					{
						log(e);
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
						var obj = parseJson(response);
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

