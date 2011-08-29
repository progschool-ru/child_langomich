<%@attribute name="languages" required="true" rtexprvalue="true" type="java.util.List"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="i" value="1"/>

<table class="words">
	<caption>Words</caption>
	<tr>
		<th align="left">Original</th>
		<th align="left">Translation</th>
		<th align="left">Language</th>
		<th align="left">Rating</th>
	</tr>
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
