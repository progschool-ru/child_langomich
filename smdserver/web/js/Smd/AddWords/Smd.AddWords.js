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

		var element = this.$(container);
		var form = this.createForm();

		form.append(this.createPair().div);
		form.append(this.createSubmitButton());

		element.append(form);
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
//		if(form.original.value == "" || form.translation.value == "" ||
//			(form.newLen.value=="" && form.language.value=="")) {
//			alert("This form was completed incorrectly");
//			return false;
//		}
		if(form.original.value == "" || form.translation.value == "")
		{
			alert("This form was completed incorrectly");
			return false;
		}
		var data = "{languages:[{name:"+t+l+t;
		data = data+",words:[{original:"+t+form.original.value+t;
		data = data+",translation:"+t+form.translation.value+t+",rating:";
		data = data+r+",modified:"+t+date+t+"}]}]}";
		form.data.value = Smd.Unicode2Java.uni2java(data);
		return true;
	},

	PAIR_INPUT_CLASS        : "b-addWords_pair-text-input",
	INPUT_ORIGINAL_CLASS    : "b-addWords_text-input-original",
	INPUT_TRANSLATION_CLASS : "b-addWords_text-input-translation",
	PAIR_DIV_CLASS          : "b-addWords_pair-div",
	somevar : null
}
