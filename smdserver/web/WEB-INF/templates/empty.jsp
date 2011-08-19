<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="pageBean" class="org.smdserver.jsp.PagesBean" scope="request"/>
<c:set var="mainTemplate" value="/WEB-INF/templates/${pageBean.mainTemplate}"/>
<jsp:include page="${mainTemplate}"/>
