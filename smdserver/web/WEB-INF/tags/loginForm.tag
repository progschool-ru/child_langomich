<%@attribute name="localeName" rtexprvalue="true"%>
<%@tag trimDirectiveWhitespaces="true"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:bundle basename="${localeName}" prefix="jsp.loginForm.">
	<form method="post" action="<smd:smdhref
					url="smd://action/login"
					redirect="smd://page/words"
					mode="url"/>">
		<table>
			<tr>
				<th><fmt:message key="login"/>:</th>
				<td><input type="text" size="30" name="login"></td>
			</tr>
			<tr>
				<th><fmt:message key="password"/>:</th>
				<td><input  type="password" size="30" name="password"></td>
			</tr>
			<tr>
				<th>&nbsp;</th>
				<td class="submitTD"><input type="submit" value="<fmt:message key="submit"/>"></td>
			</tr>
		</table>
	</form>
</fmt:bundle>