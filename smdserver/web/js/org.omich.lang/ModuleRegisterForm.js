(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	
	var TEXT_INPUT_CLASS  = "b-register_input";
	var AREA_CLASS        = "b-register_area";
	var DESCRIPTION_CLASS = "b-register_description";
	var SUBMIT_CONTAINER_CLASS = "submitTD";
	
	var MESSAGE_URL  = "smd://page/message";
	
	var EMAIL_REGEX = "[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}";
	var LOGIN_REGEX = "^[a-zA-Z][\\w-]+";
	
	var i18n = function (key) {return key;};
	
	var createPair = function(label, inputObject, description)
	{
		var div = $("<tr/>");
		
		var labelContainer = $("<th/>");
		labelContainer.append(label);
		div.append(labelContainer);
		
		var inputContainer = $("<td/>");
		inputContainer.append(inputObject);
		div.append(inputContainer);
		
		var descriptionContainer = $("<td/>");
		descriptionContainer.addClass(DESCRIPTION_CLASS);
		descriptionContainer.append(description || "&nbsp;");
		div.append(descriptionContainer);
		return div;
	};
	
	var createInput = function(attributes)
	{
		var input = $("<input>");
		for(var i in attributes)
		{
			input.attr(i, attributes[i]);
		}
		return input;
	};
	
	var createTextInputPair = function(label, name, type, pattern, description)
	{
		var input = createInput({
				type: type,
				name: name,
				required: "required",
				pattern: pattern
			});
		input.addClass(TEXT_INPUT_CLASS);
		return createPair(label, input, description);
	};
	
	var createAboutPair = function(label, name, description)
	{
		var aboutArea = $("<textarea/>");
		aboutArea.attr("name", name);
		aboutArea.addClass(AREA_CLASS);

		return createPair(label, aboutArea, description);		
	};
	
	var handleAnswer = function(scope, success, key)
	{
		log(success);
		log(key);
//		var url = scope.serverModule.getUrl(this.MESSAGE_URL);
//		location = url + "?key=" + answer.key;
	};
	
	var handleSubmit = function(scope, form)
	{
		ns.ServerApi.callRegister(form.login.value, form.password.value,
			form.email.value, form.about.value, 
			function(success, key){handleAnswer(scope, success, key);});

		return false;
	};
	
	var createForm = function(scope)
	{
		var form = $("<form/>");
		var container = $("<table/>");
		form.append(container);
		
		form.submit(function(){return handleSubmit(scope, this);});
		
		container.append(createTextInputPair(i18n("RegistrationUsers.LOGIN"), "login", "text", LOGIN_REGEX, i18n("RegistrationUsers.LOGIN_DESCRIPTION")));
		container.append(createTextInputPair(i18n("RegistrationUsers.PASSWORD"), "password", "password"));
		container.append(createTextInputPair(i18n("RegistrationUsers.EMAIL"), "email", "text", EMAIL_REGEX));
		container.append(createAboutPair(i18n("RegistrationUsers.ABOUT"), "about", i18n("RegistrationUsers.ABOUT_DESCRIPTION")));
		form.append($("<br/>"));
		
		var submit = createInput({type: "submit", value: i18n("RegistrationUsers.SUBMIT")});
		var submitContainer = createPair("", submit );
		submitContainer.addClass(SUBMIT_CONTAINER_CLASS);
		container.append(submitContainer);

		return form;
	};
	

	ns.ModuleRegisterForm = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			
			if($div)
			{
				this.appendTo($div);
			}
		},

		appendTo: function ($div)
		{
			$div.append(createForm(this));
			//var scope = this;
			//this._$form.submit(function (){return handleSubmit(scope, this)});
			//$div.append(this._$form);
		},

		refresh: function (){}
	});
})();
