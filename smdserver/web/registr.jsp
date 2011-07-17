<html>
    <head>
        <title>Registr</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style type="text/css">
            body, table, hr {
                color: black;
                background: whitesmoke;
                font-family: Verdana, sans-serif;
                font-size: x-small;
            }
        </style>
    </head>
    <body>
        <h2>Registr</h2>
		<a href="main.jsp">Main</a>
        <hr size="2"/>
        <br/>
        <br/>
        <br/>
        <fieldset>
            <form method="post" action="<%=
			org.smdserver.core.SmdConfigBean.getInstance().getActionsPath()
		%>/registr?redirectSuccess=../main.jsp&redirectFailure=../registr.jsp">
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
        </fieldset>
    </body>
</html>