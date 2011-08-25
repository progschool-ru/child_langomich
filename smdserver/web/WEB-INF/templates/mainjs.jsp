<%@page contentType="text/javascript" pageEncoding="UTF-8"%>
<jsp:include page="/js/Smd/Smd.Core.js"/>
<jsp:include page="/js/Smd/Server/Smd.Server.js"/>
<jsp:include page="/js/Smd/AddWords/Smd.AddWords.js"/>
<jsp:include page="/js/Smd/AddWords/Smd.AddWords.Words.js"/>
<jsp:include page="/js/Smd/RegistrationUsers/Smd.RegistrationUsers.js"/>
<jsp:include page="/js/Smd/Smd.Unicode.js"/>

<jsp:useBean id="pageBean" class="org.smdserver.jsp.PagesBean" scope="request"/>
function initModules()
{
	Smd.Core.addModule("server", Smd.Server.Module, 
					{
						servletPaths : {
							action: "${pageBean.JSPConfig.actionPath}",
							page:   "${pageBean.JSPConfig.pagePath}"
						},
						basePath : "<%=request.getContextPath()%>"
					});
	Smd.Core.addModule("addWords", Smd.AddWords.Module, "server");
	Smd.Core.addModule("registractionUsers", Smd.RegistrationUsers.Module, "server");
}

function createApps()
{
	Smd.Core.API.$(".b-addWords").each(function(index, elem)
		{
			Smd.Core.createApp("addWords", elem);
		});
	Smd.Core.API.$(".b-register").each(function(index, elem)
		{
			Smd.Core.createApp("registractionUsers", elem);
		});
}

Smd.Core.API.$(document).ready(function(){
	initModules();
	createApps();
});
