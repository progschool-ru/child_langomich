<%@attribute name="url" required="true" rtexprvalue="true"%>
<%@attribute name="text" required="true" rtexprvalue="true"%>
<%@attribute name="cssClass" required="false" %>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<jsp:useBean id="linkCreator" scope="application" class="org.smdserver.jsp.LinkCreator"/>
<jsp:useBean id="pageBean" scope="request" class="org.smdserver.jsp.PagesBean"/>
<%
	String basePath = request.getContextPath();

	ILink link = linkCreator.createLink(getUrl(), getText(),
			        pageBean.getCurrentUrl(),
					null);
	link.setCSSClass(getCssClass());
%>
<%=link.getHTML()%>