<%@attribute name="timeStamp" required="true" rtexprvalue="true" type="java.lang.Long" %>
<%@tag trimDirectiveWhitespaces="true"%>
<%@tag import="java.util.Date" %>
<%
Date date = new Date((Long)jspContext.getAttribute("timeStamp"));
%>
<%= date.toString() %>