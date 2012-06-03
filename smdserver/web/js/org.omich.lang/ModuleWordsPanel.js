(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var appendTableHeader = function ($table)
	{
		$table  .append($("<caption/>").append("Слова"));
		var create$Th = function(){return $("<th/>").attr("align", "left")};
		
		var $tr = $("<tr/>");
		$table.append($tr);	
		$tr.append(create$Th().append("Иностранное"))
			.append(create$Th().append("Родное"))
			.append(create$Th().append("Язык"))
			.append(create$Th().append("Вес"));
	};
	
	var appendWord = function ($table, model, word, languageName, languageId)
	{
		var $tr = $("<tr/>");
		$table.append($tr);	
		$tr.append($("<td/>").append(word.foreign))
			.append($("<td/>").append(word.nativ))
			.append($("<td/>").append(languageName))
			.append($("<td/>").append(word.rating));
			
		var $delTd = $("<td/>");$tr.append($delTd);
		var $delA = $("<a/>");$delTd.append($delA)
		$delA.addClass("words-delete").append("delete");
		var deleteHref="#deleteWords?languageId=" + languageId 
			+ "&words=[%22" + word.foreign + "%22]";
		$delA.attr("href", deleteHref);
		$delA.click(function(event){event.preventDefault();});

		$delTd.click(function(event)
		{
			$tr.hide();
			ns.ServerApi.callDeleteWords(word.foreign, languageId,
				function(success)
				{
					if(!success)
					{
						$tr.show();
					}
					else
					{
						$tr.remove();
						model.removeWord(languageId, word.foreign);
					}
				});
		});

		$table.org_omich_lang_isEven = !$table.org_omich_lang_isEven;
		$tr.addClass($table.org_omich_lang_isEven ? "even" : "odd");
	};

	var appendLanguages = function ($table, model)
	{
		model.iterateEachWord( function (lan, w)
		{
				appendWord($table, model,
					{foreign:w.original, nativ: w.translation, rating:w.rating}, 
					lan.name, lan.id);
		});
	};
	
	var handleLanguagesReset = function (scope)
	{
		var $table = scope._$table;
		appendLanguages($table, scope._model);
	};
	
	var handleAddedWords = function (scope, event)
	{
		for(var i = 0; i < event.length; ++i)
		{
			var lan = event[i];
			for(var j = 0; j < lan.words.length; ++j)
			{
				var w = lan.words[j];
				appendWord(scope._$table, scope._model,
					{foreign:w.original, nativ: w.translation, rating:w.rating}, 
					lan.name, lan.id);
			}
		}
	}

	ns.ModuleWordsPanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			var scope = this;
			
			var m = settings.model
			this._model = m;
			m.getDispatcher().addListener(m.EVT_LANGUAGES_RESET, 
				function (){handleLanguagesReset(scope)});
			m.getDispatcher().addListener(m.EVT_WORD_ADDED,
				function (event)
				{
					handleAddedWords(scope, event);
				}
			);
			
			this._$table = $("<table/>").addClass("words");
			this.refresh();

			if($div)
			{
				this.appendTo($div);
			}
		},
		appendTo: function ($div){$div.append(this._$table);},
		refresh: function ()
		{
			var scope = this;
			var $table = this._$table;
			$table.empty();
			appendTableHeader($table);

			ns.ServerApi.callGetWords(function(result)
			{
				scope._model.resetLanguages(result.languages);
			});
		}
	});
})();