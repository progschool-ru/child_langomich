<%
//TODO: 2.medium Делать проверку того, что пользователь залогинен,
//если нет, то отправлять его куда подальше
%>
<html>
    <head>
        <title>New Password</title>
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
        <h2>New Password - <%=session.getAttribute("currentLogin")%></h2>
        <hr size="2"/>
        <br/>
        <br/>
        <br/>
        <fieldset>
            <form method="post" 
			   action="<%=
			org.smdserver.core.SmdConfigBean.getInstance().getActionsPath()
		%>/setPassword?redirectSuccess=../profile.jsp&redirectFailure=../setPassword.jsp">
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