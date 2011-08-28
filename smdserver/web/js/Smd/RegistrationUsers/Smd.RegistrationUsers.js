Smd.RegistrationUsers = {
	Module : function()
	{
	},
	
	App : function(api, serverModuleName, container)
	{
		this.api = api;
		this.$ = api.$;
		this.serverModule = api.getModule(serverModuleName);
		this.$(container).append(this.createForm());
	},
	
	createForm : function()
	{
		var form = this.$("<form/>");
		var scope = this;
		
		form.submit(function(){return scope.handleSubmit(this);});
		
		form.append(this.createTextInputPair("Login:", "login", this.LOGIN_REGEX));
		form.append(this.createTextInputPair("Password:", "password"));
		form.append(this.createTextInputPair("E-Mail:", "email", this.EMAIL_REGEX));
		form.append(this.createAboutPair("About:", "about"));
		form.append(this.createInput({type: "submit", value: "Register"}));

		return form;
	},
	
	handleSubmit : function(form)
	{
		try
		{
			this.api.ajax(this.serverModule.getUrl("smd://action/register"),
				{
					async : false,
					context : this,
					data : {
						login:    form.login.value,
						password: form.password.value,
						email:    form.email.value,
						about:    form.about.value
					},
					success : function(event, textStatus, response)
					{
						var preparing = this.api.unescapeFromJavaString(response.responseText.trim());
						var answer = JSON.parse(preparing);
						
						if(!answer.success)
						{
							allert(answer.message);
						}
						else
						{
							this.handleAnswer(answer);
						}
					}
				});
		}
		catch(e){console.log(e);}
		return false;
	},
	
	handleAnswer: function(answer)
	{
		var url = this.serverModule.getUrl("smd://page/message");
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
	
	createTextInputPair: function(label, name, pattern)
	{
		return this.createPair(label, this.createInput({
			type: "text",
			name: name,
			required: "required",
			pattern: pattern
		}));
	},
	
	createAboutPair: function(label, name)
	{
		var aboutArea = this.$("<textarea/>");
		aboutArea.attr("name", name);

		return this.createPair(label, aboutArea);		
	},
	
	createPair: function(label, inputObject)
	{
		var div = this.$("<div/>");
		
		var labelContainer = this.$("<div/>");
		labelContainer.append(label);
		div.append(labelContainer);
		
		var inputContainer = this.$("<div/>");
		inputContainer.append(inputObject);
		div.append(inputContainer);
		
		return div;
	},
	
	EMAIL_REGEX: "[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}",
	LOGIN_REGEX: "^[a-zA-Z][\\w-]+"
}