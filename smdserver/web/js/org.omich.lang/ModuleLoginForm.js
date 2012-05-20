(function ()
{
	var ns = org.omich.nsSelf("lang");
	
	var appendInputField = function($table, text, inputSettings, tdClass)
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
	}

	ns.ModuleLoginForm = ns.ModuleAbstract.extend({
		init: function ()
		{
			var $form = $("<form/>");
			var $table = $("<table/>");
			$form.append($table);
			
			appendInputField($table, "Login", {type: "text", size: "30", name: "login"});
			appendInputField($table, "Password", {type: "password", size: "30", name: "password"});
			appendInputField($table, "&nbsp;", {type: "submit", value: "Submit"}, "submitTD");
			
			this._$form = $form;
		},

		appendTo: function ($div)
		{
			$div.append(this._$form);
		},

		refresh: function (){}
	});
})();
