(function ()
{
	var ns = org.omich.nsSelf("lang");
	var isArray = jQuery.isArray;
	
	ns.ModelWords = org.omich.Class.extend({
		EVT_LANGUAGE_ADDED: "languageAdded",
		EVT_LANGUAGE_REMOVED: "languageRemoved",
		EVT_LANGUAGES_RESET: "languagesReset",
		EVT_WORD_ADDED: "wordAdded",
		EVT_WORD_REMOVED: "wordRemoved",

		init: function ()
		{
			this._languages = [];
			this._dispatcher = new ns.EventDispatcher(this);
		},
		
		getDispatcher: function () {return this._dispatcher;},
		
		getLanguageInfoById : function (id)
		{
			var language;
			this.iterateEachLanguage(function(lan)
			{
				if(lan.id == id)
				{
					language = {id: id, name: lan.name}
				}
			})
			return language;
		},
		
		addWord: function (languageId, word)
		{
			var scope = this;
			var visited = false;
			this.iterateEachLanguage(function(lan)
			{
				if(lan.id == languageId)
				{
					var eventLan = {id: lan.id,
							name: lan.name,
							words: []};
					
					var append = function(w)
					{
						lan.words.push({
								original: w.original, 
								translation: w.translation,
								rating: w.rating
							});
						eventLan.words.push({
								original: w.original, 
								translation: w.translation,
								rating: w.rating
							});
					}

					if(isArray(word))
					{
						for(var i = 0; i < word.length; ++i)
						{
							append(word[i]);
						}
					}
					else
					{
						append(word);
					}

					visited = true;

					scope.getDispatcher().dispatchEvent(scope.EVT_WORD_ADDED,[eventLan]);

					return false;
				}
			});
			return visited;
		},
		
		removeWord: function (languageId, foreign)
		{
			var scope = this;
			this.iterateEachLanguage(function(lan)
			{
				for(var i = 0; i < lan.words.length; ++i)
				{
					if(lan.words[i].original == foreign)//TODO: Replace original by foreign
					{
						var word = lan.words[i];
						lan.words.splice(i, 1);
						scope.getDispatcher().dispatchEvent(scope.EVT_WORD_REMOVED, 
						{
							languageId: lan.id,
							word: word
						});
						return false;
					}
				}
			});
		},
		
		resetLanguages: function (languages)
		{
			this._languages = languages || [];
			this.getDispatcher().dispatchEvent(this.EVT_LANGUAGES_RESET, this);
		},
		
		iterateEachWord: function (wordHandler)
		{
			if(!this._languages)
				return;

			for(var i = 0; i < this._languages.length; ++i)
			{
				var lan = this._languages[i];
				for(var j = 0; j < lan.words.length; ++j)
				{
					var w = lan.words[j];
					if(wordHandler(lan, w) === false)
					{
						return;
					}
				}
			}
		},
		
		iterateEachLanguage: function (languageHandler)
		{
			if(!this._languages)
				return;

			for(var i = 0; i < this._languages.length; ++i)
			{
				if(languageHandler(this._languages[i]) === false)
				{
					return;
				}
			}
		}
	});
})();
