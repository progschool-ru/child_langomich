<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>

<jsp:useBean id="smdconfig" class="org.smdserver.core.SmdConfigBean" scope="application"/>
<c:if test='${sessionScope["currentLogin"] != null}'>
	<ul class="user">
		<li class="profile"><smd:ahref text='${sessionScope["currentLogin"]}' url="profile"/></li>
		<li class="logout"><smd:ahref text="Logout" url="${smdconfig.actionsPath}/logout?redirect=page/login"/></li>
	</ul>
</c:if>
