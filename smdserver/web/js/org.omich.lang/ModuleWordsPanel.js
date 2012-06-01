(function ()
{
	var ns = org.omich.nsSelf("lang");
	
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
			.append($("<td/>").append(word.nativee))
			.append($("<td/>").append(language))
			.append($("<td/>").append(word.rating));
			
		var $delTd = $("<td/>"); $tr.append($delTd);
		var $delA = $("<a/>"); $delTd.append($delA)
		$delA.addClass("words-delete").append("delete");

		var deleteHref="../deleteWords?languageId=47ae64ed-c67c-4d8f-ac08-fdeb7db0621b&words=[%22привет%22]&redirect=page%2Fwords"
		$delA.attr("href", deleteHref);


		$table.org_omich_lang_isEven = !$table.org_omich_lang_isEven;
		$tr.addClass($table.org_omich_lang_isEven ? "even" : "odd");
	};

	ns.ModuleWordsPanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			this._$table = $("<table/>").addClass("words");
			var $table = this._$table;
			appendTableHeader($table);
			appendWord($table, {foreign:"hi", nativee: "привет", rating:"0"}, "en");
			appendWord($table, {foreign:"fifth", nativee: "пятый", rating:"0"}, "en");
			appendWord($table, {foreign:"third", nativee: "третий", rating:"0"}, "en");

			if($div)
			{
				this.appendTo($div);
			}
		},
		appendTo: function ($div){$div.append(this._$table);},
		refresh: function (){}
	});
})();