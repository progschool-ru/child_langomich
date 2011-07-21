<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="linkCreator" scope="application" class="org.smdserver.jsp.LinkCreator"/>
<%
	IUrl url = linkCreator.createUrl((String)jspContext.getAttribute("url"),
					(SmdUrl)request.getAttribute("currentLink"),
					request.getContextPath());
%>
<%=url.getURL()%>