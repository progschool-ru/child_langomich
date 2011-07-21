<%@attribute name="servlet" required="true" rtexprvalue="true"%>
<%@attribute name="action" required="true" rtexprvalue="true"%>
<%@attribute name="baseServlet" required="false" rtexprvalue="true"%>
<%@attribute name="encode" required="false" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>

<jsp:useBean id="smdCreator" scope="application"
			 class="org.smdserver.jsp.SmdLinkCreator"/>
<%!
	private java.util.ResourceBundle rb =
			java.util.ResourceBundle.getBundle("org.smdserver.config");
%>
<%
	SmdUrl currentUrl = (getBaseServlet() == null)
			              ? (SmdUrl)request.getAttribute("currentLink")
			              : new SmdUrl(getBaseServlet(), "");
	SmdUrl link = new SmdUrl(getServlet(), getAction(), 
			                 currentUrl, request.getContextPath(), null);

	String result = getEncode() == null
			        ? link.getURL()
			        : java.net.URLEncoder.encode(link.getURL(), "utf8");
%>
<%=result%>