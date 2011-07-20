<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@attribute name="text" required="true" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<%--a href="${url}">${text}</a--%>
<jsp:useBean id="linkCreator" scope="application" class="org.smdserver.jsp.LinkCreator"/>
<%
	ILink link = linkCreator.createLink(getUrl(), getText(), 
				java.util.ResourceBundle.getBundle("org.smdserver.config"),
				(SmdLink)request.getAttribute("currentLink"),
				request.getContextPath());
%>
<%=link.getHTML()%>