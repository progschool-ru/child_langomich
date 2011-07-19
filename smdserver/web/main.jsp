<%@ page import="org.smdserver.words.Language" %>
<%@ page import="java.util.Date" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="mainTemplate" value="/WEB-INF/templates/${requestScope['mainTemplate']}"/>
<c:set var="title" value="${requestScope['title']}"/>

<smd:smdHTML title="${title}">
	<div class="header">
		<ul class="menu">
			<li class="mainPage"><smd:ahref text="Play" url="words"/></li>
			<li><span class="current">All Words</span></li>
		</ul>
		<smd:userInfo/>
	</div>
	<div class="main">
		<jsp:include page="${mainTemplate}"/>
	</div>
</smd:smdHTML>
