<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div>
	<b>Words</b>
	<table>
		<tr>
			<th align="left">Language</th>
			<th align="left">Original</th>
			<th align="left">Translation</th>
			<th align="left">Rating</th>
			<th align="left">Modified</th>
		</tr>
		<jsp:useBean id="languages" scope="session" class="java.util.ArrayList"/>
		<c:forEach var="language" items="${languages}">
			<c:forEach var="word" items="${language.words}">
				<tr>
					<td>${language.name}</td>
					<td>${word.original}</td>
					<td>${word.translation}</td>
					<td>${word.rating}</td>
					<td><smd:date timeStamp="${word.modified}" /></td>
				</tr>
			</c:forEach>
		</c:forEach>
	</table>
</div>