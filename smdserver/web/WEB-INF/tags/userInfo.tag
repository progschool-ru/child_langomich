<%@attribute name="ulClass" rtexprvalue="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd" %>

<c:if test='${sessionScope["currentUserId"] != null}'><%--TODO: (3.low) Use something better, than session--%>
	<ul class="${ulClass}">
		<li class="${ulClass}-profile"><smd:ahref text='${sessionScope["currentLogin"]}' url="smd://page/profile"/></li>
		<li class="${ulClass}-logout"><smd:smdhref text="Logout" url="smd://action/logout" redirect="smd://page/login"/></li>
	</ul>
</c:if>
