<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<jsp:useBean id="pageBean" class="org.smdserver.jsp.PagesBean" scope="request"/>
<smd:words languages="${pageBean.userWords.languages}"/>
