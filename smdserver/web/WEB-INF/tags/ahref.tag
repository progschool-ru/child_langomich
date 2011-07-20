<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@attribute name="text" required="true" rtexprvalue="true"%>
<%@attribute name="redirect" required="false" rtexprvalue="true"%>
<%@attribute name="redirectSuccess" required="false" rtexprvalue="true"%>
<%@attribute name="redirectFailure" required="false" rtexprvalue="true"%>
<%@attribute name="mode" required="false" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--a href="${url}">${text}</a--%>
<jsp:useBean id="linkCreator" scope="application" class="org.smdserver.jsp.LinkCreator"/>
<%!
	private ILink createLink(String url, String text,
			                  java.util.Map<String, Object> params,
							  LinkCreator linkCreator,
							  SmdLink currentLink,
							  javax.servlet.http.HttpServletRequest request)
	{
		return linkCreator.createLink(url, text,
					java.util.ResourceBundle.getBundle("org.smdserver.config"),
					params,
					currentLink,
					request.getContextPath());
	}
%>
<%
	java.util.Map<String, Object> params = null;
	if(getRedirect() != null || getRedirectSuccess() != null ||
			getRedirectFailure() != null)
	{
		params = new java.util.HashMap<String, Object>();
	}

	ILink link = createLink(getUrl(), getText(), params, linkCreator,
							(SmdLink)request.getAttribute("currentLink"),
							request);
	SmdLink targetLink = link instanceof SmdLink ? (SmdLink)link : null;
	if(getRedirect() != null)
	{
		params.put("redirect", createLink(getRedirect(), null, null, 
				     linkCreator, targetLink, request));
	}
	if(getRedirectSuccess() != null)
	{
		params.put("redirectSuccess", createLink(getRedirectSuccess(), null, null,
				     linkCreator, targetLink, request));
	}
	if(getRedirectFailure() != null)
	{
		params.put("redirectFailure", createLink(getRedirectFailure(), null, null,
				     linkCreator, targetLink, request));
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
