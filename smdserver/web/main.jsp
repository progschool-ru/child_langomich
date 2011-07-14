<%@ page import="org.smdserver.words.Language" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Main</title>
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
        <jsp:useBean id="languages" scope="session" class="java.util.ArrayList"/>

            <% if(session.getAttribute("currentLogin")==null)
        {%>
            <h2>Main</h2>
            <hr size="2"/>
            <br/>
            <br/>
            <br/>
            <fieldset>
                <form method="post" action="action/login?redirect=../main.jsp">
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
            </fieldset>
        <%}
        else
        {%>
            <h2>Main - <%=session.getAttribute("currentLogin")%></h2>
			<ul>
				<li><a href="profile.jsp">Profile</a></li>
				<li><a href="action/logout?redirect=../main.jsp">Logout</a></li>
			</ul>
			
            <hr size="2"/>
			<a href="addWords.jsp">Add Word</a>
            <form method="post" action="action/getWords">
                <input type="submit" name="getWords" value="  Get Words  ">
            </form>
            <form method="post" action="action/setWords">
                <input type="text" size="30" name="data">
                <input type="submit" name="setWords" value="  Set Words  ">
            </form>
            <table frame="below" width="100%">
                <legend><b>Words</b></legend>
                <tr>
                    <th align="left">Language</th>
                    <th align="left">Original</th>
                    <th align="left">Translation</th>
                    <th align="left">Rating</th>
                    <th align="left">Modified</th>
                </tr>
                <%for(int i = 0; i < languages.size(); i++) {
                    Language l = (Language)languages.get(i);
                    for(int j = 0; j < l.getWords().size(); j++) {%>
                        <tr>
                            <td width="100"><%=l.getName()%></td>
                            <td width="100"><%=l.getWords().get(j).getOriginal()%></td>
                            <td width="100"><%=l.getWords().get(j).getTranslation()%></td>
                            <td width="100"><%=l.getWords().get(j).getRating()%></td>
                            <td width="300-"><%=new Date(l.getWords().get(j).getModified()).toString()%></td>
                        </tr>
                    <%}%>
                <%}%>
            </table>
        <%}%>
    </body>
</html>