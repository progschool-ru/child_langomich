<jsp:useBean id="smdconfig" class="org.smdserver.core.SmdConfigBean" scope="application"/>
<form method="post" 
   action="${smdconfig.actionsPath}/setPassword?redirect=page/profile">
	<table>
	<tr>
			<td>Password:</td>
			<td><input  type="password" size="30"
						name="password"></td>
	</tr>
	</table>
	<br/>
	<input type="submit" name="ok" value="OK">
</form>
