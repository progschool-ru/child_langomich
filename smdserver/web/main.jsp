<%@ page import="org.smdserver.words.Language" %>
<%@ page import="java.util.Date" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="mainTemplate" value="/WEB-INF/templates/${requestScope['mainTemplate']}"/>

<smd:smdHTML>
	<div class="header">
		<ul class="menu">
			<li class="mainPage"><smd:ahref text="Play" url="words"/></li>
			<li><span class="current">AllWords</span></li>
		</ul>
		<smd:userInfo/>
	</div>
	<div class="main">
		<jsp:include page="${mainTemplate}"/>
	</div>
</smd:smdHTML>
