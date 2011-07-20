<%@attribute name="links" type="java.util.List" rtexprvalue="true"%>
<%@attribute name="ulClass" %>
<%@ tag trimDirectiveWhitespaces="true"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test='${ulClass != null and ulClass != ""}'>
	<c:set var="classAttr" value=' class="${ulClass}"'/>
</c:if>

<ul${classAttr}>
	<c:forEach var="item" items="${links}">
		<li>${item.HTML}</li>
	</c:forEach>
</ul>