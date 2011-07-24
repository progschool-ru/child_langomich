Smd.AddWords = {
	Module : function(api, serverModuleName)
	{
		this.api = api;

		this.App = function(params)
		{
			Smd.AddWords.App.call(this, api, serverModuleName, params);
		}
		this.App.prototype = Smd.AddWords;
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
		form.attr("method", "POST");
		var action = this.serverModule.getUrl("smd://action/addWords");
		var redirect = encodeURIComponent(this.api.getCurrentLocation());
		form.attr("action", action + "?redirect=" + redirect);

		var scope = this;
		form.submit(function(){return scope.handleSubmit(this)});

		var input = this.$("<input type='hidden'/>");
		input.attr("name", "data");
		form.append(input);

		var createButtonContainer = function()//It's for Google-Chrome
		{
			return scope.$("<div/>").addClass(scope.BUTTON_CONTAINER_CLASS);
		}

		new this.Words(this.api, form, createButtonContainer);

		form.append(
			createButtonContainer().append(this.createSubmitButton())
		);

		return form;
	},

	createSubmitButton : function()
	{
		var submit = this.$("<input type='submit'/>");
		submit.addClass(this.SUBMIT_BUTTON_CLASS);
		submit.attr("value",this.SUBMIT_BUTTON_LABEL);
		return submit;
	},

	handleSubmit : function (form)
	{
		var date = new Date().getTime();
		var r = 0;
		var l = "en";

		var words = [];
		var addWordFunc = function(original, translation)
		{
			if(original != "" && translation != "")
			{
				words.push({
					original: original,
					translation: translation,
					rating: r,
					modified: date
				});
			}
		}

		if(!form.original.length)
		{
			addWordFunc(form.original.value, form.translation.value);
		}
		else for(var i=0; i < form.original.length && i < form.translation.length; i++)
		{
			addWordFunc(form.original[i].value, form.translation[i].value);
		}

		if(words.length == 0)
		{
			alert("This form was completed incorrectly");
			return false;
		}
		
		var data = {
			languages:[{
					name:l,
					words:words
				}]
		};
		form.data.value = this.api.encodeToJavaString(JSON.stringify(data));
		return true;
	},

	BUTTON_CONTAINER_CLASS : "b-addWords_button-container",
	SUBMIT_BUTTON_CLASS    : "b-addWords_submit-button",
	SUBMIT_BUTTON_LABEL    : "Submit"
}
