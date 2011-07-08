<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Profile</title>
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
        <h2>Profile - <%=session.getAttribute("currentLogin")%></h2>
        <form method="post" action="setPassword">
            <input type="submit" name="newPassword" value="  New Password  ">
        </form>

        <form method="post" action="enter">
            <input type="submit" name="newPassword" value="  Main  ">
        </form>

        <hr size="2"/>

    </body>
</html>