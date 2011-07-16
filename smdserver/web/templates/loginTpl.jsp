			<form method="post" action="<%=
			org.smdserver.core.SmdConfigSingleton.getInstance().getActionsPath()
		%>/login?redirect=../main.jsp">
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
				<input type="submit" name="Login" value="  Login  ">
			</form>
			<a href="registr.jsp">Create account</a>
