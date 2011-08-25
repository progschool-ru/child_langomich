<jsp:useBean id="pageBean" class="org.smdserver.jsp.PagesBean" scope="request"/>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<fmt:setLocale value="<%=request.getLocale()%>"/>
<fmt:setBundle basename="org.smdserver.pages.message" var="messages"/>
<fmt:message key="${param.key}" bundle="${messages}" var="message"/>

<c:choose>
	<c:when test="${fn:startsWith(message, '??')}">
		<smd:redirect url="smd://page/404"/>
	</c:when>
	<c:otherwise>
		${message}
	</c:otherwise>
</c:choose>
