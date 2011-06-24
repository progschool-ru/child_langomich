<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <title>Profile</title>
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