<%@page import="java.util.ResourceBundle"%>
<%@page contentType="text/html"%>
<%
String configPath = getServletContext().getInitParameter("config");//TODO: (3.low) move "config" to some var or const;
ResourceBundle rb = ResourceBundle.getBundle(configPath);
String serverConfigPath = rb.getString("server.properties.file");//TODO: (3.low) move "server.properties.file" to some var or const
ResourceBundle serverRB = ResourceBundle.getBundle(serverConfigPath);
boolean isMaintenanceAllowed = "true".equals(serverRB.getString("maintenance.allowed"));

if(!isMaintenanceAllowed)
{
	%>error<%
	return;
}

%>

<%
	String linkPathAction = rb.getString("link.path.action");
%>
<c:set var="title" value="/WEB-INF/templates/${requestScope['title']}"/>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>${title}</title>
		<script src="<%= request.getContextPath() %>/js/lib/jquery-1.6.2.js"></script>
		<script src="<%= request.getContextPath() %>/js/Smd/Smd.Unicode.js"></script>
		<script src="<%= request.getContextPath() %>/js/Smd/Server/Smd.Server.js"></script>
		<script>
			
function handleFormSubmit(form, server)
{
	var action = $("#smdAction")[0].value;
	var url = server.getUrl("smd://action/" + action);
	
	var dataStr = "password=" + $("#maintenancePassword")[0].value;
	var anyHost = $("#anyHost")[0].checked;
	
	if(action == "maintenanceCreateDB" || action == "maintenanceDropDB")
	{
		dataStr += "&dbPassword=" + $("#dbPassword")[0].value;
		dataStr += "&dbUser=" + $("#dbUser")[0].value;
		
		if(anyHost)
		{
			dataStr += "&anyHost=true";
		}
	}
	
	jQuery.ajax(url, {
		async : true,
		type: "POST",
		data: dataStr,
		success : function(event, textStatus, response)
		{
			var prepared = Smd.Unicode.quickUnescapeFromUtf16(response.responseText);
			var object = JSON.parse(prepared);
			if(!object.success)
			{
				alert("failure");
			}
			else
			{
				alert("success");
			}
		}
	});
}
			
$(document).ready(function()
{
	var server = new Smd.Server.Module(null, {
						servletPaths : {action:"<%=linkPathAction%>"},
						basePath : "<%=request.getContextPath()%>"
					}, true);

	$("#maintenanceForm").submit(function(){handleFormSubmit(this, server); return false;});
});
		</script>
    </head>
    <body>
        <form id="maintenanceForm">
			Maintenance password: <input id="maintenancePassword" type="password" name="password"/><br/>
			Root db user: <input id="dbUser" type="text" name="dbUser"/><br/>
			Root db password: <input id="dbPassword" type="password" name="dbPassword"/><br/>
			Allow any host for db user: <input type="checkbox" id="anyHost" name="anyHost" value="true" checked/><br/>
			Action: <select id="smdAction" name="action">
				<option value="maintenanceCreateDB">create DB</option>
				<option value="maintenanceDropDB">drop DB</option>
				<option value="maintenanceDeployDBTables">deploy Tables</option>
				<option value="maintenanceDropDBTables">drop Tables</option>
			</select><br/>
			<input type="submit"/>
		</form>
    </body>
</html>
