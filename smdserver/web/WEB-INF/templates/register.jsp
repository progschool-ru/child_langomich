<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<form method="post" action="<smd:smdhref url="smd://action/registr"
		        redirectSuccess="smd://page/words"
				redirectFailure="smd://page/register"
				text="" mode="url"/>">
	<table>
		<tr>
			<td>Login:</td>
			<td><input type="text" size="30"
					   name="login"></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><input  type="password" size="30"
						name="password"></td>
		</tr>
		<tr>
			<td>E-Mail:</td>
			<td><input type="text" size="30" name="email"></td
		</tr>
		<tr>
			<td colspan="2">
				About:<br/>
				<textarea name="about" cols="45"></textarea>
			</td>
		</tr>
	</table>
	<br/>
	<input type="submit" value="Register">
</form>
