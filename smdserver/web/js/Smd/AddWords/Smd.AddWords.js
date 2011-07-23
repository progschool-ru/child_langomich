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

		this.pairsCounter = 0;

		var element = this.$(container);
		var form = this.createForm();
		element.append(form);

		var x = 2;
		var scope = this;

		var appendSubmit = function()
		{
			form.append(scope.createSubmitButton());
		}
		var appendPairs = function(){
			x--;
			var cb = (x > 0) ? appendPairs : appendSubmit;
			scope.appendPair(form, cb);
		}

		appendPairs();
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

		return form;
	},

	createSubmitButton : function()
	{
		var submit = this.$("<input type='submit'/>");
		submit.append("Add");
		return submit;
	},

	appendPair : function(container, callback)
	{
		var div = this.createPair().div;
		div.css("display", "none");
		container.append(div);
		div.show("fast", callback);
		this.pairCounter ++;
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
		var t =  '"';
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

	PAIR_INPUT_CLASS        : "b-addWords_pair-text-input",
	INPUT_ORIGINAL_CLASS    : "b-addWords_text-input-original",
	INPUT_TRANSLATION_CLASS : "b-addWords_text-input-translation",
	PAIR_DIV_CLASS          : "b-addWords_pair-div",
	somevar : null
}
