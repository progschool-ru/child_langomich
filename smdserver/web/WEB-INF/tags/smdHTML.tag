<%@attribute name="title" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${title != null and title != ''}">
	<c:set var="titlePrefix" value="${title} \ "/>
</c:if>

<!DOCTYPE html>
<html>
	<head>
		<title>${titlePrefix}LangOmich</title>
		<meta http-equiv="Content-Type" content="text/html; charset=<%=request.getAttribute("web.charset")%>"/>
		<link rel="stylesheet" href="<%= request.getContextPath() %>/design/smdMain.css"/>
		<script src="<%= request.getContextPath() %>/js/jQuery/jquery-1.6.2.js"></script>
		<script src="<smd:varurl url="smd://page/main.js"/>"></script>
	</head>
	<body>
		<jsp:doBody/>
		<div class="footer">
			<ul class="menu">
				<li><smd:ahref text="Main"           url="smd://page/play"/></li>
				<li><smd:ahref text="About"          url="http://code.google.com/p/s7smart-dictionary/"/></li>
				<li><smd:ahref text="Ask a question" url="http://olymp.omich.net/q2a/ask"/></li>
				<li><smd:ahref text="Downloads"      url="http://code.google.com/p/s7smart-dictionary/downloads/list"/></li>
			</ul>
			<p>
				Development: Sergei Skripnikov, <smd:ahref text="ProgSchool" url="http://progschool.ru"/>
			</p>
			<p>
				E-Mail: <smd:ahref text="admin@omich.net" url="mailto: admin@omich.net?subject=for smd"/>
			</p>
			<p class="output-date">
				ProgSchool &mdash; 2011
			</p>
		</div>
	</body>
</html>