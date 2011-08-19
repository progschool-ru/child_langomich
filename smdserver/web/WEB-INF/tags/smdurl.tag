<%@attribute name="servlet" required="true" rtexprvalue="true"%>
<%@attribute name="action" required="true" rtexprvalue="true"%>
<%@attribute name="baseServlet" required="false" rtexprvalue="true"%>
<%@attribute name="encode" required="false" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="org.smdserver.jsp.*"%>
<jsp:useBean id="pageBean" scope="request" class="org.smdserver.jsp.PagesBean"/>
<%
	String baseServlet = (String)jspContext.getAttribute("baseServlet");
	String servlet = (String)jspContext.getAttribute("servlet");
	String action = (String)jspContext.getAttribute("action");
	Object encode = (String)jspContext.getAttribute("encode");

	SmdUrl currentUrl = (baseServlet == null)
			              ? pageBean.getCurrentUrl()
			              : new SmdUrl(baseServlet, "");
	SmdUrl link = new SmdUrl(servlet, action,
			                 currentUrl, request.getContextPath(), null);

	String result = (encode == null)
			        ? link.getURL()
			        : java.net.URLEncoder.encode(link.getURL(), (String)request.getAttribute("web.charset"));
%>
<%=result%>