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

		form.append(this.createLanguages());

		var scope = this;
		form.submit(function(){return scope.handleSubmit(this)});

		var input = this.$("<input type='hidden'/>");
		input.attr("name", "data");
		form.append(input);

		var createButtonContainer = function()//It's for Google-Chrome
		{
			return scope.$("<div/>").addClass(scope.BUTTON_CONTAINER_CLASS);
		}

		new this.Words(this.api, form, createButtonContainer,
						this.INPUT_NAME_ORIGINAL,
						this.INPUT_NAME_TRANSLATION);

		form.append(
			createButtonContainer().append(this.createSubmitButton())
		);

		return form;
	},

	createLanguages : function()
	{
		var combobox = this.$("<div/>");

		var comboboxS = this.$("<div/>");
		comboboxS.addClass(this.LANGUAGE_CONTAINER_CLASS);
		combobox.append(comboboxS);

		var select = this.$("<select/>");
		select.attr("name", "language");
		select.addClass(this.LANGUAGE_INPUT_CLASS);
		var languages = this.serverModule.getLanguages();

		for(var i in languages)
		{
			var option = this.$("<option/>");
			option.attr("value", languages[i]);
			option.append(languages[i]);
			select.append(option);
		}
		comboboxS.append(select);

		var newLangButton = this.$("<div/>");
		newLangButton.append(this.NEW_LANGUAGE_BUTTON_LABEL);
		newLangButton.addClass(this.ALINK_CLASS);
		newLangButton.addClass(this.LANGUAGE_BUTTON_CLASS);
		comboboxS.append(newLangButton);


		var comboboxN = this.$("<div/>");
		comboboxN.addClass(this.LANGUAGE_CONTAINER_CLASS);
		combobox.append(comboboxN);

		var newLangInput = this.$("<input type='text'/>");
		newLangInput.attr("name", "newLanguage");
		newLangInput.addClass(this.LANGUAGE_INPUT_CLASS);
		comboboxN.append(newLangInput);

		var existLangButton = this.$("<div/>");
		existLangButton.append(this.EXISTING_LANGUAGE_BUTTON_LABEL);
		existLangButton.addClass(this.ALINK_CLASS);
		existLangButton.addClass(this.LANGUAGE_BUTTON_CLASS);
		comboboxN.hide();
		comboboxN.append(existLangButton);

		var scope = this;
		newLangButton.click(function(){
			newLangInput.attr("value", scope.LANGUAGE_INPUT_TEXT);
			scope.switchElements(comboboxS, comboboxN, null,
				function(){newLangInput.focus().select();});
		})

		existLangButton.click(function(){
			newLangInput.attr("value", "");
			scope.switchElements(comboboxN, comboboxS, null,
				function(){select.focus();});
		})

		return combobox;
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

		var l;
		if(form.newLanguage.value!= "")
		{
			l = form.newLanguage.value;
		}
		else
		{
			l = form.language.value;
		}

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

		if(!form[this.INPUT_NAME_ORIGINAL].length)
		{
			addWordFunc(form[this.INPUT_NAME_ORIGINAL].value,
						form[this.INPUT_NAME_TRANSLATION].value);
		}
		else for(var i=0;
				i < form[this.INPUT_NAME_ORIGINAL].length
					&& i < form[this.INPUT_NAME_TRANSLATION].length;
				i++)
		{
			addWordFunc(form[this.INPUT_NAME_ORIGINAL][i].value,
						form[this.INPUT_NAME_TRANSLATION][i].value);
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
		form.data.value = this.api.escapeToJavaString(JSON.stringify(data));
		return true;
	},

	callOfSpeed : function(element, func, speed, callback)
	{
		func.call(element, speed, callback);
		if(!speed && callback)
			callback();
	},

	switchElements : function(hideElem, showElem, speed, callback)
	{
		var count = true;
		var fun = function()
		{
			if(count)
			{
				count = false;
			}
			else if(callback)
			{
				callback();
			}
		}
		this.callOfSpeed(hideElem, hideElem.hide, speed, fun);
		this.callOfSpeed(showElem, showElem.show, speed, fun);
	},

	INPUT_NAME_ORIGINAL    : "original",
	INPUT_NAME_TRANSLATION : "translation",
	ALINK_CLASS            : "alink",
	
	BUTTON_CONTAINER_CLASS : "b-addWords_button-container",
	SUBMIT_BUTTON_CLASS    : "b-addWords_submit-button",
	SUBMIT_BUTTON_LABEL    : "Submit",

	LANGUAGE_BUTTON_CLASS  : "b-addWords_language-button",
	LANGUAGE_INPUT_CLASS   : "b-addWords_language-input",
	LANGUAGE_INPUT_TEXT    : "langName",
	LANGUAGE_CONTAINER_CLASS : "b-addWords_language-container",
	NEW_LANGUAGE_BUTTON_LABEL : "New Language",
	EXISTING_LANGUAGE_BUTTON_LABEL : "Existing Language",
}