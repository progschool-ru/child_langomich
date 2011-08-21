<%@attribute name="languages" required="true" rtexprvalue="true" type="java.util.List"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<table class="words">
	<caption>Words</caption>
	<tr>
		<th align="left">Language</th>
		<th align="left">Original</th>
		<th align="left">Translation</th>
		<th align="left">Rating</th>
	</tr>
	<c:forEach var="language" items="${languages}">
		<c:forEach var="word" items="${language.words}">
			<tr>
				<td>${language.name}</td>
				<td>${word.original}</td>
				<td>${word.translation}</td>
				<td>${word.rating}</td>
				<td><smd:smdhref url="smd://action/deleteWords?languageId=${language.id}&words=[\"${word.original}\"]"
							 text="delete" redirect="smd://page/words"/>
			</tr>
		</c:forEach>
	</c:forEach>
</table>
