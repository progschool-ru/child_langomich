<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%
//TODO: 2.medium Делать проверку того, что пользователь залогинен,
//если нет, то отправлять его куда подальше
%>
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
		<ul>
			<li><a href="setPassword.jsp">New Password</a></li>
			<li><a href="main.jsp">Main</a></li>
		</ul>

        <hr size="2"/>

    </body>
</html>