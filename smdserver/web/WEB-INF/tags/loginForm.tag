<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<form method="post" action="<smd:smdhref
				url="smd://action/login"
				redirect="smd://page/play"
				mode="url"/>">
	<table>
		<tr>
			<th>Login:</th>
			<td><input type="text" size="30" name="login"></td>
		</tr>
		<tr>
			<th>Password:</th>
			<td><input  type="password" size="30" name="password"></td>
		</tr>
	</table>
	<br/>
	<input type="submit" value="Login">
</form>