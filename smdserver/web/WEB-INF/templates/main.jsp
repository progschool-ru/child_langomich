<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="pageBean" class="org.smdserver.jsp.PagesBean" scope="request"/>
<c:set var="mainTemplate" value="/WEB-INF/templates/${pageBean.mainTemplate}"/>

<smd:smdHTML title="${pageBean.title}" menu="${pageBean.menuLinks}" user="${pageBean.user}">
	<jsp:include page="${mainTemplate}"/>
</smd:smdHTML>
