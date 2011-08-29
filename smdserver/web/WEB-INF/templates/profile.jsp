<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:bundle basename="org.smdserver.locale" prefix="jsp.profile.">
	<form method="post"
		  action="<smd:smdhref url="smd://action/setPassword"
					 redirect="smd://page/profile" mode="url"/>">
		<table>
			<tr>
				<th><fmt:message key="password"/>:</th>
				<td><input  type="password" size="30"
							name="password"></td>
			</tr>
		</table>
		<br/>
		<input type="submit" value="<fmt:message key="ok"/>">
	</form>
</fmt:bundle>
