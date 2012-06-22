(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var createForm = function (scope)
	{
		var $table = $("<table/>");
		var $tr = $("<tr/>");
		$table.append($tr);
		var $th = $("<th/>").append("Password:");
		$tr.append($th);
		
		var $passInput = $("<input/>").attr({
			type:"password",
			size:"30",
			name:"password"});
		var $td = $("<td/>").append($passInput);
		$tr.append($td);
		
		$tr = $("<tr/>");
		$table.append($tr);
		$tr.append($("<th/>"));
		
		var $submitInput = $("<input/>").attr({
			type:"submit",
			value:"OK"
		});
		$td = $("<td/>").addClass("submitTD");
		$td.append($submitInput);
		$tr.append($td);
		
		var $form = $("<form/>");
		scope._$messageDiv = $("<div/>");
		$form.append(scope._$messageDiv);
		$form.append($table);
		return $form;
	}
	
	var handleSubmit = function (scope, form)
	{
		try
		{
			scope._$messageDiv.empty();

			var password = form.password.value;
			form.password.value = "";

			ns.ServerApi.callSetPassword(password,
					function (success)
					{
						if(!success)
						{
							scope._$messageDiv.append("Something wrong");
						}
					});
		}
		catch(e)
		{
			log(e);
		}
		return false;
	}

	ns.ModuleProfileForm = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			this._$form = createForm(this);
			
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

