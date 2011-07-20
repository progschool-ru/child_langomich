<jsp:useBean id="smdconfig" class="org.smdserver.core.SmdConfigBean" scope="application"/>
<form method="post" action="${smdconfig.actionsPath}/registr?redirectSuccess=page/words&redirectFailure=page/register">
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
	</table>
	<br/>
	<input type="submit" name="registr" value="  Registr  ">
</form>
