(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var ANIMATION_SPEED         = "fast";
	var ALINK_CLASS             = ns.ModuleAddWordsPanel.ALINK_CLASS;
	var BUTTON_CONTAINER_CLASS  = ns.ModuleAddWordsPanel.BUTTON_CONTAINER_CLASS;
	var ICON_BUTTON_CLASS       = "b-addWords_icon-button";
	var INPUT_ORIGINAL_CLASS    = "b-addWords_text-input-original";
	var INPUT_TRANSLATION_CLASS = "b-addWords_text-input-translation";
	var LESS_BUTTON_CLASS       = "b-addWords_less-button";
	var LESS_BUTTON_LABEL       = "Less";
	var MORE_BUTTON_CLASS       = "b-addWords_more-button";
	var PAIR_DIV_CLASS          = "b-addWords_pair-div";
	var PAIR_INPUT_CLASS        = "b-addWords_pair-text-input";
	
	var callOfSpeed = ns.ModuleAddWordsPanel.callOfSpeed;
	
	var createMoreButton = function(scope, clickHandler)
	{
		var $more = $("<div/>");
		$more.addClass(MORE_BUTTON_CLASS);
		$more.addClass(ALINK_CLASS);
		$more.append(scope._moreText);
		$more.click(clickHandler);
		return $more;
	};
	
	var removePair = function(scope, $div, callback)
	{
		if(scope._lock)
			return;

		scope._lock = true;
		scope._pairsCounter --;

		callOfSpeed($div, $div.hide, ANIMATION_SPEED, function()
			{
				$div.remove();
				if(callback)
					callback();
				scope._lock = false;
			});
	};
	
	var appendLessButton = function(scope, pair)
	{
		var $less = $("<div/>");
		pair.$div.append($less);
		pair.$lessButton = $less

		$less.addClass(LESS_BUTTON_CLASS);
		$less.addClass(ICON_BUTTON_CLASS);
		$less.append(LESS_BUTTON_LABEL);

		$less.click(function()
		{
			if(scope._pairsCounter > 1)
			{
				removePair(scope, pair.$div);
			}
			else
			{
				pair.$inputOriginal.attr("value", "");
				pair.$inputTranslation.attr("value", "");
			}
		});
	};
	
	var createPair = function(scope)
	{
		var pair = {
			$div : $("<div/>"),
			$inputOriginal    : $("<input type='text'/>"),
			$inputTranslation : $("<input type='text'/>")
		};
		appendLessButton(scope, pair);
		pair.$div.append(pair.$inputOriginal);
		pair.$div.append(pair.$inputTranslation);
		pair.$div.addClass(PAIR_DIV_CLASS);
		pair.$inputOriginal.addClass(INPUT_ORIGINAL_CLASS);
		pair.$inputTranslation.addClass(INPUT_TRANSLATION_CLASS);
		pair.$inputOriginal.addClass(PAIR_INPUT_CLASS);
		pair.$inputTranslation.addClass(PAIR_INPUT_CLASS);

		pair.$inputOriginal.attr("name", scope._original);
		pair.$inputTranslation.attr("name", scope._translation);
		return pair;
	};
	
	var appendPair = function(scope, $container, callback)
	{
		if(this._lock)
			return;

		scope._lock = true;
		var $div = createPair(scope).$div;
		$div.hide();
		scope._pairsCounter ++;
		$container.append($div);

		callOfSpeed($div, $div.show, ANIMATION_SPEED, function()
			{
				if(callback)
					callback();
				scope._lock = false;
			});
	};

	ns.ModuleAddWordsPanel.Words = org.omich.Class.extend({
		init: function ($element, createButtonContainerFunction,
								inputNameOriginal, inputNameTranslation,
								moreText)
		{
			this._pairsCounter = 0;
			this._original = inputNameOriginal;
			this._translation = inputNameTranslation;
			this._moreText = moreText;

			var $wordsFieldset = $("<fieldset/>");
			$element.append($wordsFieldset);

			var scope = this;
			$element.append(createButtonContainerFunction().append(
					createMoreButton(this, function()
					{
						appendPair(scope, $wordsFieldset);
					})
			));

			appendPair(this, $wordsFieldset);
		}
	});
})();