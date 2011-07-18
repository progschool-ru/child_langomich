<%@ page import="org.smdserver.words.Language" %>
<%@ page import="java.util.Date" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:choose>
	<c:when test='${sessionScope["currentLogin"] == null}'>
		<jsp:useBean id="smdconfig" class="org.smdserver.core.SmdConfigBean" scope="application"/>
		<smd:smdHTML>
			<smd:loginForm loginAction="${smdconfig.actionsPath}/login?redirect=../main.jsp"/>
		</smd:smdHTML>
	</c:when>
	<c:otherwise>
		<smd:smdHTML>
			<div class="header">
				<ul class="menu">
					<li class="mainPage"><smd:ahref text="Play" url="main.jsp"/></li>
					<li><span class="current">AllWords</span></li>
				</ul>
				<ul class="user">
					<li class="profile"><smd:ahref text='${sessionScope["currentLogin"]}' url="profile.jsp"/></li>
					<li class="logout"><smd:ahref text="Logout" url="${smdconfig.actionsPath}/logout?redirect=../main.jsp"/></li>
				</ul>
			</div>
			<div class="main">
				<div class="content">
					<smd:words/>
				</div>
				<div class="toolbar">
					<smd:toolbar/>
				</div>
			</div>
		</smd:smdHTML>
	</c:otherwise>
</c:choose>