Smd.AddWords.Words = function(api, element, createButtonContainerFunction,
								inputNameOriginal, inputNameTranslation,
								moreText)
{
	this.$ = api.$;
	this._pairsCounter = 0;
	this._original = inputNameOriginal;
	this._translation = inputNameTranslation;
	this._moreText = moreText;
	
	var wordsFieldset = this.$("<fieldset/>");
	element.append(wordsFieldset);

	var scope = this;
	element.append(createButtonContainerFunction().append(
		this.createMoreButton(function()
			{
				scope.appendPair(wordsFieldset);
			}
		)
	));

	this.appendPair(wordsFieldset);
};

Smd.AddWords.Words.prototype = {
	
	createMoreButton : function(clickHandler)
	{
		var more = this.$("<div/>");
		more.addClass(this.MORE_BUTTON_CLASS);
		more.addClass(this.ALINK_CLASS);
		more.append(this._moreText);
		more.click(clickHandler);
		return more;
	},

	appendPair : function(container, callback)
	{
		if(this._lock)
			return;

		this._lock = true;
		var div = this.createPair().div;
		div.hide();
		this._pairsCounter ++;
		container.append(div);
		var scope = this;
		scope.callOfSpeed(div, div.show, this.ANIMATION_SPEED, function()
			{
				if(callback)
					callback();
				scope._lock = false;
			});
	},

	removePair : function(div, callback)
	{
		if(this._lock)
			return;

		this._lock = true;
		this._pairsCounter --;
		var scope = this;
		scope.callOfSpeed(div, div.hide, this.ANIMATION_SPEED, function()
			{
				div.remove();
				if(callback)
					callback();
				scope._lock = false;
			});
	},

	createPair : function()
	{
		var pair = {
			div : this.$("<div/>"),
			inputOriginal    : this.$("<input type='text'/>"),
			inputTranslation : this.$("<input type='text'/>")
		};
		this.appendLessButton(pair);
		pair.div.append(pair.inputOriginal);
		pair.div.append(pair.inputTranslation);
		pair.div.addClass(this.PAIR_DIV_CLASS);
		pair.inputOriginal.addClass(this.INPUT_ORIGINAL_CLASS);
		pair.inputTranslation.addClass(this.INPUT_TRANSLATION_CLASS);
		pair.inputOriginal.addClass(this.PAIR_INPUT_CLASS);
		pair.inputTranslation.addClass(this.PAIR_INPUT_CLASS);

		pair.inputOriginal.attr("name", this._original);
		pair.inputTranslation.attr("name", this._translation);
		return pair;
	},

	appendLessButton : function(pair)
	{
		var less = this.$("<div/>");
		pair.div.append(less);
		pair.lessButton = less

		less.addClass(this.LESS_BUTTON_CLASS);
		less.addClass(this.ICON_BUTTON_CLASS);
		less.append(this.LESS_BUTTON_LABEL);
		var scope = this;
		less.click(function()
		{
			if(scope._pairsCounter > 1)
			{
				scope.removePair(pair.div);
			}
			else
			{
				pair.inputOriginal.attr("value", "");
				pair.inputTranslation.attr("value", "");
			}
		});
	},

	callOfSpeed : Smd.AddWords.callOfSpeed,

	ANIMATION_SPEED         : "fast",
	ALINK_CLASS             : Smd.AddWords.ALINK_CLASS,
	BUTTON_CONTAINER_CLASS  : Smd.AddWords.BUTTON_CONTAINER_CLASS,
	ICON_BUTTON_CLASS       : "b-addWords_icon-button",
	INPUT_ORIGINAL_CLASS    : "b-addWords_text-input-original",
	INPUT_TRANSLATION_CLASS : "b-addWords_text-input-translation",
	LESS_BUTTON_CLASS       : "b-addWords_less-button",
	LESS_BUTTON_LABEL       : "Less",
	MORE_BUTTON_CLASS       : "b-addWords_more-button",
	PAIR_DIV_CLASS          : "b-addWords_pair-div",
	PAIR_INPUT_CLASS        : "b-addWords_pair-text-input"
}
