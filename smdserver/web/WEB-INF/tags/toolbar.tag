<%@ tag import="org.smdserver.core.SmdConfigBean" %>
<%@ tag import="org.smdserver.jsp.*" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<div class="menu">
	<h3>Toolbar</h3>
	<ul>
		<li><smd:ahref text="Add Word" url="../../addWords.jsp"/></li>
		<li><smd:ahref text="Get Words JSON" url="smd://action/getWords"/></li>
		<li><smd:ahref text="Refresh" url="javascript:location.reload(true)"/></li>
	</ul>
</div>