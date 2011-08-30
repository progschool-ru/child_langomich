<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="pageBean" class="org.smdserver.jsp.PagesBean" scope="request"/>
<fmt:setBundle basename="org.smdserver.locale" var="locale"/>
<fmt:message key="${pageBean.title}" bundle="${locale}" var="title"/>
<c:set var="mainTemplate" value="/WEB-INF/templates/${pageBean.mainTemplate}"/>

<smd:smdHTML title="${title}" menu="${pageBean.menuLinks}" 
			 user="${pageBean.user}" localeName="org.smdserver.locale"
			 yandexMetrikaId="${pageBean.JSPConfig.yandexMetrikaId}">
	<jsp:include page="${mainTemplate}"/>
</smd:smdHTML>
