<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<form method="post" action="<smd:ahref 
				url="smd://action/login" text=""
				redirect="smd://page/play"
				mode="url"/>">
	<table>
		<tr>
			<td>Login:</td>
			<td><input type="text" size="30" name="login"></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input  type="password" size="30" name="password"></td>
		</tr>
	</table>
	<br/>
	<input type="submit" value="Login">
</form>