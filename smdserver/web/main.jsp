<%@ page import="org.smdserver.words.Language" %>
<%@ page import="java.util.Date" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:choose>
	<c:when test='${sessionScope["currentLogin"] == null}'>
		<jsp:useBean id="smdconfig" class="org.smdserver.core.SmdConfigBean" scope="application"/>
		<smd:smdHTML>
			<smd:loginForm loginAction="${smdconfig.actionsPath}/login?redirect=../main.jsp"/>
		</smd:smdHTML>
	</c:when>
	<c:otherwise>
		<smd:smdHTML>
			<div class="header">
				<smd:menu/>
			</div>
			<div class="main">
				<h2>Main - ${sessionScope["currentLogin"]}</h2>
				<smd:toolbar/>
				<smd:words/>
			</div>
		</smd:smdHTML>
	</c:otherwise>
</c:choose>