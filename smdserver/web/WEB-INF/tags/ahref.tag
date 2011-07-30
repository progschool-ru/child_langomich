<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@attribute name="text" required="true" rtexprvalue="true"%>
<%@attribute name="cssClass" required="false" %>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<jsp:useBean id="linkCreator" scope="application" class="org.smdserver.jsp.LinkCreator"/>
<%
	String basePath = request.getContextPath();

	ILink link = linkCreator.createLink(getUrl(), getText(),
					(SmdUrl)request.getAttribute("currentLink"),
					basePath,
					null);
	link.setCSSClass(getCssClass());
%>
<%=link.getHTML()%>