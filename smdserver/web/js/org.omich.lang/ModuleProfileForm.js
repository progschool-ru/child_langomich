(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var createForm = function ()
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
		$form.append($table);
		return $form;
	}
	
	var handleSubmit = function (scope, form)
	{
		console.log(form);
		return false;
	}

	ns.ModuleProfileForm = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			this._$form = createForm();
			
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

