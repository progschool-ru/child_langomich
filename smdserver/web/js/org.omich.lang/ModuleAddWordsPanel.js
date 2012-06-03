(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;

	var INPUT_NAME_FOREIGN = "foreign";
	var INPUT_NAME_NATIV = "nativ";
	var ALINK_CLASS = "alink";

	var BUTTON_CONTAINER_CLASS = "b-addWords_button-container";
	var SUBMIT_BUTTON_CLASS = "b-addWords_submit-button";
	var SUBMIT_BUTTON_LABEL = "AddWords.SUBMIT";

	
	var LANGUAGE_BUTTON_CLASS = "b-addWords_language-button";
	var LANGUAGE_CONTAINER_CLASS = "b-addWords_language-container";
	var LANGUAGE_INPUT_CLASS = "b-addWords_language-input";
	var LANGUAGE_INPUT_TEXT = "AddWords.NEW_LANGUAGE_INPUT";
	var MORE_BUTTON_LABEL = "AddWords.MORE";
	var NEW_LANGUAGE_BUTTON_LABEL = "AddWords.NEW_LANGUAGE";
	var EXISTING_LANGUAGE_BUTTON_LABEL = "AddWords.EXISTING_LANGUAGE";
	
	var getDataValue = function(scope, form)
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
			languageName = scope._model.getLanguageInfoById(languageId).name;
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

		if(!form[INPUT_NAME_FOREIGN].length)
		{
			addWordFunc(form[INPUT_NAME_FOREIGN].value,
						form[INPUT_NAME_NATIV].value);
		}
		else for(var i=0;
				i < form[INPUT_NAME_FOREIGN].length
					&& i < form[INPUT_NAME_NATIV].length;
				i++)
		{
			addWordFunc(form[INPUT_NAME_FOREIGN][i].value,
						form[INPUT_NAME_NATIV][i].value);
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
		return data;
	};

	var handleSubmit = function (scope, form)
	{
		try
		{
			var data = getDataValue(scope, form);
			ns.ServerApi.callAddWords(data, function(success)
			{
				for(var i = 0; i < data.languages.length; ++i)
				{
					var lan = data.languages[i];
					if(lan.id)
					{
						scope._model.addWord(lan.id, lan.words);
					}
					else
					{
						scope._resetLanguages();
					}
				}
				scope._words.refreshFields();
			});
		}
		catch(e)
		{
			log(e);
		}
		return false;
	};
	
	var createSubmitButton = function ()
	{
		var submit = $("<input type='submit'/>");
		submit.addClass(SUBMIT_BUTTON_CLASS);
		submit.attr("value", SUBMIT_BUTTON_LABEL);
		return submit;
	};
	
	var callOfSpeed = function(element, func, speed, callback)
	{
		func.call(element, speed, callback);
		if(!speed && callback)
			callback();
	};
	
	var switchElements = function(hideElem, showElem, speed, callback)
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
		callOfSpeed(hideElem, hideElem.hide, speed, fun);
		callOfSpeed(showElem, showElem.show, speed, fun);
	};
	
	var fillLanguagesBox = function ($combobox, model)
	{
		var $comboboxS = $("<div/>");
		$comboboxS.addClass(LANGUAGE_CONTAINER_CLASS);
		$combobox.append($comboboxS);

		var $select = $("<select/>");
		$select.attr("name", "language");
		$select.addClass(LANGUAGE_INPUT_CLASS);

		model.iterateEachLanguage(function(lan)
		{
			var name = lan.name;
			var id = lan.id;
			var $option = $("<option/>");
			$option.attr("value", id);
			$option.append(name);
			$select.append($option);
		});
		$comboboxS.append($select);

		var $newLangButton = $("<div/>");
		$newLangButton.append(NEW_LANGUAGE_BUTTON_LABEL);
		$newLangButton.addClass(ALINK_CLASS);
		$newLangButton.addClass(LANGUAGE_BUTTON_CLASS);
		$comboboxS.append($newLangButton);

		var $comboboxN = $("<div/>");
		$comboboxN.addClass(LANGUAGE_CONTAINER_CLASS);
		$combobox.append($comboboxN);

		var $newLangInput = $("<input type='text'/>");
		$newLangInput.attr("name", "newLanguage");
		$newLangInput.addClass(LANGUAGE_INPUT_CLASS);
		$comboboxN.append($newLangInput);

		var $existLangButton = $("<div/>");
		$existLangButton.append(EXISTING_LANGUAGE_BUTTON_LABEL);
		$existLangButton.addClass(ALINK_CLASS);
		$existLangButton.addClass(LANGUAGE_BUTTON_CLASS);
		$comboboxN.hide();
		$comboboxN.append($existLangButton);

		$newLangButton.click(function(){
			$newLangInput.attr("value", LANGUAGE_INPUT_TEXT);
			switchElements($comboboxS, $comboboxN, null,
				function(){$newLangInput.focus().select();});
		})

		$existLangButton.click(function(){
			$newLangInput.attr("value", "");
			switchElements($comboboxN, $comboboxS, null, function(){$select.focus();});
		})
	}
	
	var createForm = function (scope)
	{
		var $form = $("<form/>");

		scope._$languagesBox = $("<div/>");
		fillLanguagesBox(scope._$languagesBox, scope._model);
		$form.append(scope._$languagesBox);

		$form.submit(function(){return handleSubmit(scope, this)});

		var $input = $("<input/>").attr("name", "data").attr("type", "hidden");
		$form.append($input);

		var createButtonContainer = function()//It's for Google-Chrome
		{
			return $("<div/>").addClass(BUTTON_CONTAINER_CLASS);
		}

		scope._words = new ns.ModuleAddWordsPanel.Words(//this.api, 
						$form, createButtonContainer,
						INPUT_NAME_FOREIGN,
						INPUT_NAME_NATIV,
						MORE_BUTTON_LABEL);

		$form.append(
			createButtonContainer().append(createSubmitButton())
		);

		return $form;
	};
	
	var rebuildLanguages = function (scope)
	{
		scope._$languagesBox.empty();
		fillLanguagesBox(scope._$languagesBox, scope._model);
	};

	ns.ModuleAddWordsPanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			var scope = this;
			var m = settings.model;
			this._resetLanguages = settings && settings.onResetLanguages;
			this._model = m;
			var rlFun = function(){rebuildLanguages(scope);};
			m.getDispatcher().addListener(m.EVT_LANGUAGES_RESET, rlFun);
			m.getDispatcher().addListener(m.EVT_LANGUAGE_ADDED, rlFun);
			m.getDispatcher().addListener(m.EVT_LANGUAGES_RESET, rlFun);
			
			this._$form = createForm(this);

			if($div)
			{
				this.appendTo($div);
			}
		},
		appendTo: function ($div)
		{
			$div.append(this._$form);
		},
		refresh: function (){},
	});
	
	ns.ModuleAddWordsPanel.ALINK_CLASS = ALINK_CLASS;
	ns.ModuleAddWordsPanel.BUTTON_CONTAINER_CLASS = BUTTON_CONTAINER_CLASS;
	
	ns.ModuleAddWordsPanel.callOfSpeed = callOfSpeed;
})();
