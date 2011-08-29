<%@attribute name="timeStamp" required="true" rtexprvalue="true" type="java.lang.Long" %>
<%@tag trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:formatDate type="both"	value="<%= new java.util.Date((Long)jspContext.getAttribute("timeStamp")) %>"/>