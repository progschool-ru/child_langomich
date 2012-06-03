(function ()
{
	var ns = org.omich.nsSelf("lang");
	var log = org.omich.log;
	
	var resizeIFrame = function ($div, $iframe)
	{
		var parentWidth = $div.parent().width();
		var strLeft = $div.css("margin-left");
		var strRight = $div.css("margin-right");
		
		var getPx = function(str)
		{
			var percIndex = str.indexOf("%");
			if(percIndex >= 0)
			{
				return parentWidth * Number(str.substring(0, percIndex)) * .01;
			}

			var pxIndex = str.indexOf("px");
			var substr = pxIndex < 0 ? str : str.substring(0, pxIndex);
			return Number(substr);
		}

		var width = parentWidth - getPx(strLeft) - getPx(strRight);
		$iframe.css("width", width);
		
		var fixSize = function ()
		{
			var scrollWidth = $iframe[0].contentWindow.document.body.scrollWidth;
			var width = $iframe.width();
			
			if(width < scrollWidth)
			{
				$iframe.css("width", scrollWidth + 4);
				setTimeout(fixSize, 100);
			}
			else
			{
				var clientHeight = $iframe[0].contentWindow.document.body.clientHeight;
				$iframe.css("height", clientHeight + 50 + "px");
				var scrollHeight = $iframe[0].contentWindow.document.body.scrollHeight;
				$iframe.css("height", scrollHeight + 5 + "px");
			}
		}
		
		setTimeout(fixSize, 100);
	};

	ns.ModuleIFramePanel = ns.ModuleAbstract.extend({
		init: function (settings, $div)
		{
			this._super(settings);
			this._$iframe = $("<iframe/>").attr("src", settings.src)
				.attr("width", "100%")
				.css("width", "100%");
			
			if(!settings || !settings.message)
				return;

			if($div)
			{
				this.appendTo($div);
			}
		},
		appendTo: function ($div)
		{
			var $iframe = this._$iframe;
			resizeIFrame($div, $iframe);
			$div.append($iframe);
			
			if(this.sizeListener)
			{
				this._$div.getDispatcher().removeListener("resized", this.sizeListener);
				this.sizeListener = null;
				this._$div = null;
			}

			if($div.getDispatcher())
			{
				this.sizeListener = function(){resizeIFrame($div, $iframe);}
				this._$div = $div;
				$div.getDispatcher().addListener("resized", this.sizeListener);
			}
		},
		refresh: function (){}
	});
})();