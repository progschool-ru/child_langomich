<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@attribute name="text" required="false" rtexprvalue="true"%>
<%@attribute name="redirect" required="false" rtexprvalue="true"%>
<%@attribute name="redirectSuccess" required="false" rtexprvalue="true"%>
<%@attribute name="redirectFailure" required="false" rtexprvalue="true"%>
<%@attribute name="mode" required="false" rtexprvalue="true"%>

<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String redirect = (String)jspContext.getAttribute("redirect");
	String redirectSuccess = (String)jspContext.getAttribute("redirectSuccess");
	String redirectFailure = (String)jspContext.getAttribute("redirectFailure");

	java.util.Map<String, Object> params = null;
	if(redirect != null || redirectSuccess != null ||
			redirectFailure != null)
	{
		params = new java.util.HashMap<String, Object>();
	}

	String basePath = request.getContextPath();

	ILink link = new SmdLink((String)jspContext.getAttribute("url"),
			                 (String)jspContext.getAttribute("text"),
					         (SmdUrl)request.getAttribute("currentLink"),
					         basePath,
					         params);

	SmdLink targetLink = link instanceof SmdLink ? (SmdLink)link : null;
	if(redirect != null)
	{
		params.put("redirect",
				new SmdUrl(redirect, targetLink, basePath));
	}
	if(redirectSuccess != null)
	{
		params.put("redirectSuccess",
				new SmdUrl(redirectSuccess, targetLink, basePath));
	}
	if(redirectFailure != null)
	{
		params.put("redirectFailure",
				new SmdUrl(redirectFailure, targetLink, basePath));
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
