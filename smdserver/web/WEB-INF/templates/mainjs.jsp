<%@page contentType="text/javascript" pageEncoding="UTF-8"%>
<jsp:include page="/js/Smd/Smd.Core.js"/>
<jsp:include page="/js/Smd/AddWords/Smd.AddWords.js"/>

function initModules()
{
	Smd.Core.addModule("addWords", Smd.AddWords);
}

function createApps()
{
	Smd.Core.API.$(".b-addWords").each(function(index, elem)
		{
			Smd.Core.createApp("addWords", elem);
		});
}

Smd.Core.API.$(document).ready(function(){
	initModules();
	createApps();
});
