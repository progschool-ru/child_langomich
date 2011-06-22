<%@ include file='JSONObject.jsp'%>
<html>
    <head>
        <title>New Password</title>
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
        <h2>New Password - <%=session.getAttribute("currentLogin")%></h2>
        <hr size="2"/>
        <br/>
        <br/>
        <br/>
        <fieldset>
            <form method="post" action="setPassword">
                <tr>
                        <td>Password:</td>
                        <td><input  type="password" size="30"
                                    name="password"></td>
                </tr>
                <br/>
                <input type="submit" name="ok" value="  OK  ">
            </form>
        </fieldset>
    </body>
</html>