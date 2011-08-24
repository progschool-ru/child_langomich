<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="linkCreator" scope="application" class="org.smdserver.jsp.LinkCreator"/>
<jsp:useBean id="pageBean" scope="request" class="org.smdserver.jsp.PagesBean"/>
<%
	IUrl url = linkCreator.createUrl((String)jspContext.getAttribute("url"),
					                 pageBean.getCurrentUrl());
%>
<%=url.getURL()%>