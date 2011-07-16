<%
//TODO: 2.medium. Если пользователь залогинен, то отправлять его на главную
%>
<html>
    <head>
        <title>Login</title>
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
            <h2>Login</h2>
			<a href="main.jsp">Main</a>
            <hr size="2"/>
 <%@ include file="templates/loginTpl.jsp" %>
    </body>
</html>