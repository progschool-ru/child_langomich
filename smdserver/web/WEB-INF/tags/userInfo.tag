<%@attribute name="ulClass" rtexprvalue="true"%>
<%@attribute name="user" rtexprvalue="true" type="org.smdserver.users.User" %>
<%@attribute name="localeName" rtexprvalue="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<fmt:setBundle basename="${localeName}" var="locale"/>
<fmt:message key="jsp.userinfo.logout" var="lLogout" bundle="${locale}"/>

<c:if test='${user != null}'>
	<ul class="${ulClass}">
		<li class="${ulClass}-profile"><smd:ahref text='${user.login}' url="smd://page/profile"/></li>
		<li class="${ulClass}-logout"><smd:smdhref text="${lLogout}" url="smd://action/logout" redirect="smd://page/login"/></li>
	</ul>
</c:if>
