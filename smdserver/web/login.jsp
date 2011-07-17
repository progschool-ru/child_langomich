<%
//TODO: 2.medium. Если пользователь залогинен, то отправлять его на главную
%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<jsp:useBean id="smdconfig" class="org.smdserver.core.SmdConfigBean" scope="application"/>
<smd:smdHTML>
	<smd:loginForm loginAction="${smdconfig.actionsPath}/login?redirect=../main.jsp"/>
</smd:smdHTML>
