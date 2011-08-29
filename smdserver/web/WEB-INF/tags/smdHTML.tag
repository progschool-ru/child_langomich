<%@attribute name="localeName" rtexprvalue="true"%>
<%@attribute name="title" rtexprvalue="true"%>
<%@attribute name="menu" rtexprvalue="true" type="java.util.List"%>
<%@attribute name="user" rtexprvalue="true" type="org.smdserver.users.User" %>
<%@tag trimDirectiveWhitespaces="true"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${title != null and title != ''}">
	<c:set var="titlePrefix" value="${title} \ "/>
</c:if>

<fmt:bundle basename="${localeName}" prefix="jsp.smdHTML.">
	<fmt:message key="footerMenu.main" var="lFooterMenuMain"/>
	<fmt:message key="footerMenu.about" var="lFooterMenuAbout"/>
	<fmt:message key="footerMenu.ask" var="lFooterMenuAsk"/>
	<fmt:message key="footerMenu.downloads" var="lFooterMenuDownloads"/>
	<fmt:message key="progSchool" var="lProgSchool"/>
	<fmt:message key="toolbarTitle" var="lToolbarTitle"/>
	<fmt:message key="jsLocale" var="lJSLocale"/>
<!DOCTYPE html>
<html>
	<head>
		<title>${titlePrefix}<fmt:message key="title"/></title>
		<meta charset="<%=request.getAttribute("web.charset")%>"/>
		<link rel="stylesheet" href="<%= request.getContextPath() %>/design/smdMain.css"/>
		<script src="<%= request.getContextPath() %>/js/lib/jquery-1.6.2.js"></script>
		<script src="<smd:varurl url="smd://page/main.js"/>"></script>
		<script src="<%= request.getContextPath() %>/js/Smd/Locale/Smd.Locale.${lJSLocale}"></script>
	</head>
	<body>
		<div class="header">
			<smd:ahref url="smd://page/play" text="" cssClass="header-logo"/>
			<smd:menu links="${menu}" ulClass="header-menu"/>
			<smd:userInfo ulClass="header-user" user="${user}" localeName="${localeName}"/>
		</div>
		<div class="main">
			<div class="content">
				<jsp:doBody/>
			</div>
	<c:if test='${user != null}'>
			<div class="toolbar">
				<smd:toolbar cssClass="toolbar-menu" title="${lToolbarTitle}"/>
			</div>
	</c:if>
		</div>
		<div class="footer">
			<ul class="footer-menu">
				<li><smd:ahref text="${lFooterMenuMain}"           url="smd://page/play"/></li>
				<li><smd:ahref text="${lFooterMenuAbout}"          url="http://code.google.com/p/s7smart-dictionary/"/></li>
				<li><smd:ahref text="${lFooterMenuAsk}"            url="http://olymp.omich.net/q2a/ask"/></li>
				<li><smd:ahref text="${lFooterMenuDownloads}"      url="http://code.google.com/p/s7smart-dictionary/downloads/list"/></li>
			</ul>
			<p>
				<fmt:message key="development"/>: <fmt:message key="sergeiSkripnikov"/>, <smd:ahref text="${lProgSchool}" url="http://progschool.ru"/>
			</p>
			<p>
				<fmt:message key="design"/>: <a href="http://cat-terrorist.livejournal.com"><img width="14px" height="14px" style="vertical-align: middle; border: 0; padding-right: 1px;" src="http://l-stat.livejournal.com/img/userinfo.gif?v=3"/></a><a href="http://cat-terrorist.livejournal.com"><b>cat_terrorist</b></a>
			</p>
			<p>
				<fmt:message key="email"/>: <smd:ahref text="admin@omich.net" url="mailto: admin@omich.net?subject=for smd"/>
			</p>
			<p class="footer-output-date">
				${lProgSchool} &mdash; 2011
			</p>
		</div>
	</body>
</html>
</fmt:bundle>