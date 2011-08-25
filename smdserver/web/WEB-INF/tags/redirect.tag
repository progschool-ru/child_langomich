<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@tag import="org.smdserver.jsp.IUrl"%>
<jsp:useBean id="linkCreator" scope="application" class="org.smdserver.jsp.LinkCreator"/>
<jsp:useBean id="pageBean" scope="request" class="org.smdserver.jsp.PagesBean"/>
<%
	IUrl url = linkCreator.createUrl(getUrl(), pageBean.getCurrentUrl());
	response.sendRedirect(url.getURL());
%>
<script>window.location="<%=url.getURL()%>";</script>