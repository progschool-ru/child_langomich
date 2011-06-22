<%@ include file='JSONObject.jsp'%>
<html>
    <head>
        <title>Login</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
            <h2>Login</h2>
            <form method="post" action="enter">
                <input type="submit" name="newPassword" value="  Main  ">
            </form>
            <hr size="2"/>
            <br/>
            <br/>
            <br/>
            <fieldset>
                <form method="post" action="login">
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
                <form method="post" action="registr">
                    <input type="submit" name="registr" value="  Registr  ">
                </form>
            </fieldset>
    </body>
</html>