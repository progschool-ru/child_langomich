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
	
	var appendWord = function ($table, word, language)
	{
		var $tr = $("<tr/>");
		$table.append($tr);	
		$tr.append($("<td/>").append(word.foreign))
			.append($("<td/>").append(word.nativ))
			.append($("<td/>").append(language))
			.append($("<td/>").append(word.rating));
			
		var $delTd = $("<td/>");$tr.append($delTd);
		var $delA = $("<a/>");$delTd.append($delA)
		$delA.addClass("words-delete").append("delete");

		var deleteHref="../deleteWords?languageId=47ae64ed-c67c-4d8f-ac08-fdeb7db0621b&words=[%22привет%22]&redirect=page%2Fwords"
		$delA.attr("href", deleteHref);


		$table.org_omich_lang_isEven = !$table.org_omich_lang_isEven;
		$tr.addClass($table.org_omich_lang_isEven ? "even" : "odd");
	};
	
	var appendLanguages = function ($table, languages)
	{
		for(var i = 0; i < languages.length; ++i)
		{
			var lan = languages[i];
			for(var j = 0; j < lan.words.length; ++j)
			{
				var w = lan.words[j];
				appendWord($table, {foreign:w.original, nativ: w.translation, rating:w.rating}, lan.name);
			}
		}
	};

	ns.ModuleWordsPanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
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
			var $table = this._$table;
			$table.empty();
			appendTableHeader($table);

			ns.ServerApi.callGetWords(function(result)
			{
				appendLanguages($table, result.languages);
			});
		}
	});
})();