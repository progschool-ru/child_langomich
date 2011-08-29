Smd.RegistrationUsers = {
	App : function(api, params, container)
	{
		this.api = api;
		this.$ = api.$;
		this.serverModule = api.getModule(params.serverModuleName);
		this.localeModule = api.getModule(params.localeModuleName);
		this.$(container).append(this.createForm());
	},
	
	createForm : function()
	{
		var form = this.$("<form/>");
		var container = this.$("<table/>");
		form.append(container);
		var scope = this;
		
		form.submit(function(){return scope.handleSubmit(this);});
		
		container.append(this.createTextInputPair("Login:", "login", this.LOGIN_REGEX, "<p>Must contain at least 2 symbols. Only english letters and digits are allowed.</p>"));
		container.append(this.createTextInputPair("Password:", "password"));
		container.append(this.createTextInputPair("E-Mail:", "email", this.EMAIL_REGEX));
		container.append(this.createAboutPair("About:", "about", "<p>Who are you and how did you know about LangOmich?</p><p>This information won't be published. We are just interested to know.</p>"));
		form.append(this.$("<br/>"));
		form.append(this.createInput({type: "submit", value: "Register"}));

		return form;
	},
	
	handleSubmit : function(form)
	{
		try
		{
			this.serverModule.ajax(this.REGISTER_URL,
				{
					async : true,
					context : this,
					data : {
						login:    form.login.value,
						password: form.password.value,
						email:    form.email.value,
						about:    form.about.value
					},
					success : this.handleAnswer,
					error : function(answer)
					{
						alert(answer.message);
					}
				});
		}
		catch(e){console.log(e);}
		return false;
	},
	
	handleAnswer: function(answer)
	{
		var url = this.serverModule.getUrl(this.MESSAGE_URL);
		location = url + "?key=" + answer.key;
	},
	
	createInput: function(attributes)
	{
		var input = this.$("<input>");
		for(var i in attributes)
		{
			input.attr(i, attributes[i]);
		}
		return input;
	},
	
	createTextInputPair: function(label, name, pattern, description)
	{
		var input = this.createInput({
				type: "text",
				name: name,
				required: "required",
				pattern: pattern
			});
		input.addClass(this.TEXT_INPUT_CLASS);
		return this.createPair(label, input, description);
	},
	
	createAboutPair: function(label, name, description)
	{
		var aboutArea = this.$("<textarea/>");
		aboutArea.attr("name", name);
		aboutArea.addClass(this.AREA_CLASS);

		return this.createPair(label, aboutArea, description);		
	},
	
	createPair: function(label, inputObject, description)
	{
		var div = this.$("<tr/>");
		
		var labelContainer = this.$("<th/>");
		labelContainer.append(label);
		div.append(labelContainer);
		
		var inputContainer = this.$("<td/>");
		inputContainer.append(inputObject);
		div.append(inputContainer);
		
		var descriptionContainer = this.$("<td/>");
		descriptionContainer.addClass(this.DESCRIPTION_CLASS);
		descriptionContainer.append(description || "&nbsp;");
		div.append(descriptionContainer);
		return div;
	},
	
	TEXT_INPUT_CLASS:  "b-register_input",
	AREA_CLASS:        "b-register_area",
	DESCRIPTION_CLASS: "b-register_description",
	
	MESSAGE_URL:  "smd://page/message",
	REGISTER_URL: "smd://action/register",
	
	EMAIL_REGEX: "[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}",
	LOGIN_REGEX: "^[a-zA-Z][\\w-]+"
}