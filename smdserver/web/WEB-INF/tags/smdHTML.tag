<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Main</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<link rel="stylesheet" href="<%= request.getContextPath() %>/css/smdMain.css"/>
	</head>
	<body>
		<jsp:doBody/>
		<div class="footer">
			<ul class="menu">
				<li><smd:ahref text="Main"           url="main.jsp"/></li>
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