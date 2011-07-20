<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<form method="post"
	  action="<smd:ahref url="smd://action/setPassword" text=""
				 redirect="smd://page/profile" mode="url"/>">
	<table>
	<tr>
			<td>Password:</td>
			<td><input  type="password" size="30"
						name="password"></td>
	</tr>
	</table>
	<br/>
	<input type="submit" value="OK">
</form>
