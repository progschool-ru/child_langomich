<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@attribute name="text" required="false" rtexprvalue="true"%>
<%@attribute name="redirect" required="false" rtexprvalue="true"%>
<%@attribute name="redirectSuccess" required="false" rtexprvalue="true"%>
<%@attribute name="redirectFailure" required="false" rtexprvalue="true"%>
<%@attribute name="mode" required="false" rtexprvalue="true"%>

<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:useBean id="pageBean" scope="request" class="org.smdserver.jsp.PagesBean"/>

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

	ILink link = new SmdLink((String)jspContext.getAttribute("url"),
			                 (String)jspContext.getAttribute("text"),
					         pageBean.getCurrentUrl(),
					         params);

	SmdLink targetLink = link instanceof SmdLink ? (SmdLink)link : null;
	if(redirect != null)
	{
		params.put("redirect",
				new SmdUrl(redirect, targetLink));
	}
	if(redirectSuccess != null)
	{
		params.put("redirectSuccess",
				new SmdUrl(redirectSuccess, targetLink));
	}
	if(redirectFailure != null)
	{
		params.put("redirectFailure",
				new SmdUrl(redirectFailure, targetLink));
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
