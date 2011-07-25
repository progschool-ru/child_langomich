<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="mainTemplate" value="/WEB-INF/templates/${requestScope['mainTemplate']}"/>
<jsp:include page="${mainTemplate}"/>
