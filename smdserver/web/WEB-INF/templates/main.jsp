<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="mainTemplate" value="/WEB-INF/templates/${requestScope['mainTemplate']}"/>
<c:set var="title" value="${requestScope['title']}"/>
<c:set var="menu" value="${requestScope['menu']}"/>

<smd:smdHTML title="${title}">
	<div class="header">
		<smd:menu links="${menu}" ulClass="menu"/>
		<smd:userInfo/>
	</div>
	<div class="main">
		<jsp:include page="${mainTemplate}"/>
	</div>
</smd:smdHTML>
