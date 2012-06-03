(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var ANIMATION_SPEED         = "fast";
	var ALINK_CLASS             = ns.ModuleAddWordsPanel.ALINK_CLASS;
//	var BUTTON_CONTAINER_CLASS  = ns.ModuleAddWordsPanel.BUTTON_CONTAINER_CLASS;
	var ICON_BUTTON_CLASS       = "b-addWords_icon-button";
	var INPUT_FOREIGN_CLASS     = "b-addWords_text-input-original";
	var INPUT_NATIVE_CLASS      = "b-addWords_text-input-translation";
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
				pair.$inputForeign.attr("value", "");
				pair.$inputNativ.attr("value", "");
			}
		});
	};
	
	var createPair = function(scope)
	{
		var pair = {
			$div : $("<div/>"),
			$inputForeign    : $("<input type='text'/>"),
			$inputNativ : $("<input type='text'/>")
		};
		appendLessButton(scope, pair);
		pair.$div.append(pair.$inputForeign);
		pair.$div.append(pair.$inputNativ);
		pair.$div.addClass(PAIR_DIV_CLASS);
		pair.$inputForeign.addClass(INPUT_FOREIGN_CLASS);
		pair.$inputNativ.addClass(INPUT_NATIVE_CLASS);
		pair.$inputForeign.addClass(PAIR_INPUT_CLASS);
		pair.$inputNativ.addClass(PAIR_INPUT_CLASS);

		pair.$inputForeign.attr("name", scope._original);
		pair.$inputNativ.attr("name", scope._translation);
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