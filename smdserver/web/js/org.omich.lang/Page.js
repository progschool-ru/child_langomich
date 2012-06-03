(function ()
{
	var ns = org.omich.nsSelf("lang");
	
	var appendSidePanel = function (panel, $div)
	{
		var $menu = $("<div/>").addClass("toolbar-menu");
		panel.appendTo($menu);
		$div.append($menu);
	};

	ns.Page = org.omich.Class.extend({
		init: function (settings)
		{			
			if(settings && settings.contentPanelConstructor)
			{
				this._contentPanel = new settings.contentPanelConstructor(settings.contentPanelSettings);
			}
			
			if(settings && settings.sidePanelConstructor)
			{
				this._sidePanel = new settings.sidePanelConstructor(settings.sidePanelSettings);
			}
		},
		appendTo: function ($contentDiv, $sideDiv)
		{
			if($contentDiv && this._contentPanel)
			{
				this._contentPanel.appendTo($contentDiv);
			}
			if($sideDiv && this._sidePanel)
			{
				appendSidePanel(this._sidePanel, $sideDiv);
			}
		},
		getContentPanel: function () {return this._contentPanel;},
		refresh: function (){}
	});
})();
