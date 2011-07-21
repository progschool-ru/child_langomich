<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@attribute name="text" required="false" rtexprvalue="true"%>
<%@attribute name="redirect" required="false" rtexprvalue="true"%>
<%@attribute name="redirectSuccess" required="false" rtexprvalue="true"%>
<%@attribute name="redirectFailure" required="false" rtexprvalue="true"%>
<%@attribute name="mode" required="false" rtexprvalue="true"%>

<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	java.util.Map<String, Object> params = null;
	if(getRedirect() != null || getRedirectSuccess() != null ||
			getRedirectFailure() != null)
	{
		params = new java.util.HashMap<String, Object>();
	}

	String basePath = request.getContextPath();

	ILink link = new SmdLink(getUrl(), getText(),
					(SmdUrl)request.getAttribute("currentLink"),
					basePath,
					params);

	SmdLink targetLink = link instanceof SmdLink ? (SmdLink)link : null;
	if(getRedirect() != null)
	{
		params.put("redirect",
				new SmdUrl(getRedirect(), targetLink, basePath));
	}
	if(getRedirectSuccess() != null)
	{
		params.put("redirectSuccess",
				new SmdUrl(getRedirectSuccess(), targetLink, basePath));
	}
	if(getRedirectFailure() != null)
	{
		params.put("redirectFailure",
				new SmdUrl(getRedirectFailure(), targetLink, basePath));
	}
%>
<c:choose>
	<c:when test="${mode == 'url'}">
		<%=link.getURL()%>
	</c:when>
	<c:otherwise>
		<%=link.getHTML()%>
	</c:otherwise>
</c:choose>
