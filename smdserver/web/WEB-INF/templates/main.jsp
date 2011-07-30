<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="mainTemplate" value="/WEB-INF/templates/${requestScope['mainTemplate']}"/>

<smd:smdHTML title="${requestScope['title']}" menu="${requestScope['menu']}">
	<jsp:include page="${mainTemplate}"/>
</smd:smdHTML>
