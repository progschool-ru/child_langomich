<%@attribute name="ulClass" rtexprvalue="true"%>
<%@attribute name="user" rtexprvalue="true" type="org.smdserver.users.User" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd" %>

<c:if test='${user != null}'>
	<ul class="${ulClass}">
		<li class="${ulClass}-profile"><smd:ahref text='${user.login}' url="smd://page/profile"/></li>
		<li class="${ulClass}-logout"><smd:smdhref text="Logout" url="smd://action/logout" redirect="smd://page/login"/></li>
	</ul>
</c:if>
