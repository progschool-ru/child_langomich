Smd.AddWords = {
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
						this.INPUT_NAME_TRANSLATION,
						this.i18n(this.MORE_BUTTON_LABEL));

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

		this._languages = {};
		for(var i in languages)
		{
			var name = languages[i].name;
			var id = languages[i].id;
			var option = this.$("<option/>");
			option.attr("value", id);
			option.append(name);
			select.append(option);
			this._languages[id] = name;
		}
		comboboxS.append(select);

		var newLangButton = this.$("<div/>");
		newLangButton.append(this.i18n(this.NEW_LANGUAGE_BUTTON_LABEL));
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
		existLangButton.append(this.i18n(this.EXISTING_LANGUAGE_BUTTON_LABEL));
		existLangButton.addClass(this.ALINK_CLASS);
		existLangButton.addClass(this.LANGUAGE_BUTTON_CLASS);
		comboboxN.hide();
		comboboxN.append(existLangButton);

		var scope = this;
		newLangButton.click(function(){
			newLangInput.attr("value", scope.i18n(scope.LANGUAGE_INPUT_TEXT));
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
		submit.attr("value",this.i18n(this.SUBMIT_BUTTON_LABEL));
		return submit;
	},

	handleSubmit : function (form)
	{
		try
		{
			var data = this.getDataValue(form);
			this.sendRequest(data);
		}
		catch(e)
		{
			this.api.log(e);
		}
		return false;
	},
	
	sendRequest : function(data)
	{
		this.serverModule.ajax("smd://action/addWords",
			{
				async : true,
				context : this,
				data : {data:data},
				success : function()
				{
					document.location.reload();
				},
				error : function(answer)
				{
					alert("Error during request: " + answer.message);
				}
			});		
	},
	
	getDataValue : function(form)
	{
		var date = new Date().getTime();
		var r = 0;

		var languageName;
		var languageId = null;
		if(form.newLanguage.value!= "")
		{
			languageName = form.newLanguage.value;
		}
		else
		{
			languageId = form.language.value;
			languageName = this._languages[languageId];
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
			alert("This form was filled in incorrectly");
			return false;
		}
		
		var data = {
			currentDeviceTime: date,
			languages:[{
					id: languageId,
					name:languageName,
					words:words
				}]
		};
		return this.api.escapeToJavaString(JSON.stringify(data));
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
	
	i18n: function(key)
	{
		return this.localeModule.get(key);
	},

	INPUT_NAME_ORIGINAL    : "original",
	INPUT_NAME_TRANSLATION : "translation",
	ALINK_CLASS            : "alink",
	
	BUTTON_CONTAINER_CLASS : "b-addWords_button-container",
	SUBMIT_BUTTON_CLASS    : "b-addWords_submit-button",
	SUBMIT_BUTTON_LABEL    : "AddWords.SUBMIT",

	LANGUAGE_BUTTON_CLASS  : "b-addWords_language-button",
	LANGUAGE_INPUT_CLASS   : "b-addWords_language-input",
	LANGUAGE_INPUT_TEXT    : "AddWords.NEW_LANGUAGE_INPUT",
	LANGUAGE_CONTAINER_CLASS : "b-addWords_language-container",
	MORE_BUTTON_LABEL              : "AddWords.MORE",
	NEW_LANGUAGE_BUTTON_LABEL      : "AddWords.NEW_LANGUAGE",
	EXISTING_LANGUAGE_BUTTON_LABEL : "AddWords.EXISTING_LANGUAGE"
}
