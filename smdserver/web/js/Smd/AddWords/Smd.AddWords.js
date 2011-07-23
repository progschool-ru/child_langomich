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
		var scope = this;

		this.api = api;
		this.$ = api.$;
		this.serverModule = api.getModule(serverModuleName);

		this._pairsCounter = 0;
		this._formObject = this.createForm();

		var element = this.$(container);
		element.append(this._formObject.form);
		scope.appendPair(scope._formObject.wordsFieldset);
	},

	appendMoreLessButton : function(buttonsContainer, pairsContainer)
	{
		var scope = this;

		var less = this.$("<button type='button'/>");
		less.append("Less");
		less.click(function()
		{
			if(scope._pairsCounter <= 2)
			{
				less.hide();
			}
			scope.removePair(pairsContainer);
		});
		if(scope._pairsCounter <= 1)
		{
			less.hide();
		}
		buttonsContainer.append(less);

		var more = this.$("<button type='button'/>");
		more.append("More");
		more.click(function()
		{
			if(scope._pairsCounter >= 1)
			{
				less.show();
			}
			scope.appendPair(pairsContainer);
		});
		buttonsContainer.append(more);
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

		scope.appendMoreLessButton(form, wordsFieldset);
		form.append(scope.createSubmitButton());

		return {
			form:form,
			wordsFieldset: wordsFieldset
		};
	},

	createSubmitButton : function()
	{
		var submit = this.$("<input type='submit'/>");
		submit.attr("value","Add");
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

	removePair : function(container, callback)
	{
		if(this._lock)
			return;

		this._lock = true;
		this._pairsCounter --;
		var div = container.children(":last-child");
		var scope = this;
		scope.callOfSpeed(div, div.hide, this.ANIMATION_SPEED, function()
			{
				div.remove();
				if(callback)
					callback();
				scope._lock = false;
			});
	},

	createPair : function() {
		var pair = {
			div : this.$("<div/>"),
			inputOriginal    : this.$("<input type='text'/>"),
			inputTranslation : this.$("<input type='text'/>")
		};
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

		for(var i=0; i < form.original.length && i < form.translation.length; i++)
		{
			if(form.original[i].value != "" && form.translation[i].value != "")
			{
				words.push({
					original:form.original[i].value,
					translation:form.translation[i].value,
					rating: r,
					modified: date
				});
			}
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

	ANIMATION_SPEED : "hide",

	ADD_PAIR_BUTTON_CLASS   : "b-addWords_add-pair-button",
	INPUT_ORIGINAL_CLASS    : "b-addWords_text-input-original",
	INPUT_TRANSLATION_CLASS : "b-addWords_text-input-translation",
	PAIR_DIV_CLASS          : "b-addWords_pair-div",
	PAIR_INPUT_CLASS        : "b-addWords_pair-text-input",
	somevar : null
}
