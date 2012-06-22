(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var EVT_LOGGED_IN = "loggedIn";
	
	var appendInputField = function ($table, text, inputSettings, tdClass)
	{
		var $input = $("<input/>");
		for(var key in inputSettings)
		{
			$input.attr(key, inputSettings[key]);
		}
		
		var $th = $("<th/>").append(text);
		var $td = $("<td/>").append($input);
		if(tdClass)
		{
			$td.addClass(tdClass);
		}
		var $tr = $("<tr/>").append($th).append($td);
		
		$table.append($tr);
	};
	
	var handleSubmitFailure = function (scope)
	{
		scope._$messageDiv.append("Can't login. Try again");
	};
	
	var handleSubmit = function (scope, form)
	{
		try
		{
			scope._$messageDiv.empty();
			
			var login = form.login.value;
			var password = form.password.value;
			form.password.value = "";
			ns.ServerApi.callLogin(login, password,
				function (isLoggedIn, login)
				{
					if(isLoggedIn)
					{
						scope._dispatcher.dispatchEvent(EVT_LOGGED_IN, login);
					}
					else
					{
						handleSubmitFailure(scope);
					}
				});
		}
		catch(e)
		{
			log(e);
			handleSubmitFailure(scope);
		}
		return false;
	}

	ns.ModuleLoginForm = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);

			var $form = $("<form/>");
			this._$messageDiv = $("<div/>");
			$form.append(this._$messageDiv);
			var $table = $("<table/>");
			$form.append($table);
			
			appendInputField($table, "Login", {type: "text", size: "30", name: "login"});
			appendInputField($table, "Password", {type: "password", size: "30", name: "password"});
			appendInputField($table, "&nbsp;", {type: "submit", value: "Submit"}, "submitTD");
			this._$form = $form;
			
			
			this._dispatcher = new ns.EventDispatcher(this);
			this._dispatcher.addListener(EVT_LOGGED_IN, settings.onLogin);
			
			if($div)
			{
				this.appendTo($div);
			}
		},

		appendTo: function ($div)
		{
			var scope = this;
			this._$form.submit(function (){return handleSubmit(scope, this)});
			$div.append(this._$form);
		},

		refresh: function (){}
	});
})();
