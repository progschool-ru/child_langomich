<%@page contentType="text/javascript" pageEncoding="UTF-8"%>
<jsp:include page="/js/Smd/Smd.Core.js"/>
<jsp:include page="/js/Smd/Server/Smd.Server.js"/>
<jsp:include page="/js/Smd/AddWords/Smd.AddWords.js"/>
<jsp:include page="/js/Smd/Smd.Unicode2Java.js"/>
<%
	String configPath = getServletContext().getInitParameter("config");
	java.util.ResourceBundle rb = java.util.ResourceBundle.getBundle(configPath);
	String linkPathAction = rb.getString("link.path.action");
%>

function initModules()
{
	Smd.Core.addModule("server", Smd.Server.Module, 
					{
						servletPaths : {action:"<%=linkPathAction%>"},
						basePath : "<%=request.getContextPath()%>"
					});
	Smd.Core.addModule("addWords", Smd.AddWords.Module, "server");
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
