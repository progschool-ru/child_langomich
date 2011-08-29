<%@attribute name="languages" required="true" rtexprvalue="true" type="java.util.List"%>
<%@attribute name="localeName" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="i" value="1"/>

<table class="words">
	<fmt:bundle basename="${localeName}" prefix="jsp.words.">
		<caption><fmt:message key="words"/></caption>
		<tr>
			<th align="left"><fmt:message key="original"/></th>
			<th align="left"><fmt:message key="translation"/></th>
			<th align="left"><fmt:message key="language"/></th>
			<th align="left"><fmt:message key="rating"/></th>
		</tr>
	</fmt:bundle>
	<c:forEach var="language" items="${languages}">
		<c:forEach var="word" items="${language.words}">
			<c:choose>
				<c:when test="${i % 2 == 0}">
					<c:set var="rowClass" value="even"/>
				</c:when>
				<c:otherwise>
					<c:set var="rowClass" value="odd"/>
				</c:otherwise>
			</c:choose>
			<c:set var="i" value="${i+1}"/>
			<tr class="${rowClass}">
				<td>${word.original}</td>
				<td>${word.translation}</td>
				<td>${language.name}</td>
				<td>${word.rating}</td>
				<td><smd:smdhref url="smd://action/deleteWords?languageId=${language.id}&words=[\"${word.original}\"]"
							 text="delete" redirect="smd://page/words"
							 cssClass="words-delete"/>
			</tr>
		</c:forEach>
	</c:forEach>
</table>
