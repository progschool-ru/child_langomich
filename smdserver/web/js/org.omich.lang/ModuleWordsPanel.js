(function ()
{
	var ns = org.omich.nsSelf("lang");

	ns.ModuleWordsPanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			this._$table = $("<table/>");
			this._$table.append('<table class="words">\
				<caption>Слова</caption>\
		<tr>\
			<th align="left">Слово</th>\
			<th align="left">Перевод</th>\
			<th align="left">Язык</th>\
			<th align="left">Вес</th>\
		</tr>\
	<tr class="odd">\
				<td>привет</td>\
				<td>hi</td>\
				<td>en</td>\
				<td>0</td>\
				<td><a href="../deleteWords?languageId=47ae64ed-c67c-4d8f-ac08-fdeb7db0621b&words=[%22привет%22]&redirect=page%2Fwords" class="words-delete">delete</a></tr>\
		<tr class="even">\
				<td>пятый</td>\
				<td>fifth</td>\
				<td>en</td>\
				<td>0</td>\
				<td><a href="../deleteWords?languageId=47ae64ed-c67c-4d8f-ac08-fdeb7db0621b&words=[%22пятый%22]&redirect=page%2Fwords" class="words-delete">delete</a></tr>\
		<tr class="odd">\
				<td>третий</td>\
				<td>third</td>\
				<td>en</td>\
				<td>0</td>\
				<td><a href="../deleteWords?languageId=47ae64ed-c67c-4d8f-ac08-fdeb7db0621b&words=[%22третий%22]&redirect=page%2Fwords" class="words-delete">delete</a></tr>\
		<tr class="even">\
				<td>четвёртый</td>\
				<td>fourth</td>\
				<td>en</td>\
				<td>0</td>\
				<td><a href="../deleteWords?languageId=47ae64ed-c67c-4d8f-ac08-fdeb7db0621b&words=[%22четвёртый%22]&redirect=page%2Fwords" class="words-delete">delete</a></tr>\
		</table>\
');

			if($div)
			{
				this.appendTo($div);
			}
		},
		appendTo: function ($div){$div.append(this._$table);},
		refresh: function (){}
	});
})();