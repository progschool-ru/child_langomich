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

		this._pairsCounter = 0;

		var formObject = this.createForm();
		this.$(container).append(formObject.form);
		this.appendPair(formObject.wordsFieldset);
	},

	createMoreButton : function(clickHandler)
	{
		var more = this.$("<button type='button'/>");
		more.addClass(this.MORE_BUTTON_CLASS);
		more.addClass(this.ICON_BUTTON_CLASS);
		more.append(this.MORE_BUTTON_LABEL);
		more.click(clickHandler);
		return more;
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

		var wordsFieldset = this.$("<fieldset/>");
		form.append(wordsFieldset);

		form.append(this.createMoreButton(function()
				{
					scope.appendPair(wordsFieldset);
				}));
		form.append(this.createSubmitButton());

		return {
			form:form,
			wordsFieldset: wordsFieldset
		};
	},

	createSubmitButton : function()
	{
		var submit = this.$("<input type='submit'/>");
		submit.addClass(this.SUBMIT_BUTTON_CLASS);
		submit.attr("value",this.SUBMIT_BUTTON_LABEL);
		return submit;
	},

	appendPair : function(container, callback)
	{
		if(this._lock)
			return;

		this._lock = true;
		var div = this.createPair().div;
		div.hide();
		this._pairsCounter ++;
		container.append(div);
		var scope = this;
		scope.callOfSpeed(div, div.show, this.ANIMATION_SPEED, function()
			{
				if(callback)
					callback();
				scope._lock = false;
			});
	},

	removePair : function(div, callback)
	{
		if(this._lock)
			return;

		this._lock = true;
		this._pairsCounter --;
		var scope = this;
		scope.callOfSpeed(div, div.hide, this.ANIMATION_SPEED, function()
			{
				div.remove();
				if(callback)
					callback();
				scope._lock = false;
			});
	},

	createPair : function()
	{
		var pair = {
			div : this.$("<div/>"),
			inputOriginal    : this.$("<input type='text'/>"),
			inputTranslation : this.$("<input type='text'/>")
		};
		this.appendLessButton(pair);
		pair.div.append(pair.inputOriginal);
		pair.div.append(pair.inputTranslation);
		pair.div.addClass(this.PAIR_DIV_CLASS);
		pair.inputOriginal.addClass(this.INPUT_ORIGINAL_CLASS);
		pair.inputTranslation.addClass(this.INPUT_TRANSLATION_CLASS);
		pair.inputOriginal.addClass(this.PAIR_INPUT_CLASS);
		pair.inputTranslation.addClass(this.PAIR_INPUT_CLASS);

		pair.inputOriginal.attr("name", "original");
		pair.inputTranslation.attr("name", "translation");
		return pair;
	},

	appendLessButton : function(pair)
	{
		var less = this.$("<button type='button'/>");
		pair.div.append(less);
		pair.lessButton = less

		less.addClass(this.LESS_BUTTON_CLASS);
		less.addClass(this.ICON_BUTTON_CLASS);
		less.append(this.LESS_BUTTON_LABEL);
		var scope = this;
		less.click(function()
		{
			if(scope._pairsCounter > 1)
			{
				scope.removePair(pair.div);
			}
			else
			{
				pair.inputOriginal.attr("value", "");
				pair.inputTranslation.attr("value", "");
			}
		});
	},

	handleSubmit : function (form)
	{
		var date = new Date().getTime();
		var r = 0;
//		for(var i=0; i < form.rating.length; i++){
//			if (form.rating[i].checked == true){
//				r = form.rating[i].value;
//			}
//		}
		var l = "en";
//		if(form.newLen.value== "")
//			l = form.language.value;
//		else
//			l = form.newLen.value;

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

	callOfSpeed : function(element, func, speed, callback)
	{
		func.call(element, speed, callback);
		if(!speed && callback)
			callback();
	},

	SUBMIT_BUTTON_LABEL : "Submit",
	LESS_BUTTON_LABEL   : "Less",
	MORE_BUTTON_LABEL   : "More",

	ANIMATION_SPEED : "hide",

	ICON_BUTTON_CLASS   : "b-addWords_icon-button",
	SUBMIT_BUTTON_CLASS : "b-addWords_submit-button",
	LESS_BUTTON_CLASS   : "b-addWords_less-button",
	MORE_BUTTON_CLASS   : "b-addWords_more-button",
	INPUT_ORIGINAL_CLASS    : "b-addWords_text-input-original",
	INPUT_TRANSLATION_CLASS : "b-addWords_text-input-translation",
	PAIR_DIV_CLASS          : "b-addWords_pair-div",
	PAIR_INPUT_CLASS        : "b-addWords_pair-text-input",
	somevar : null
}
