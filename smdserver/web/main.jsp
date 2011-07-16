<%@ page import="org.smdserver.words.Language" %>
<%@ page import="org.smdserver.core.SmdConfigSingleton" %>
<%@ page import="java.util.Date" %>
<% String actionsPath = SmdConfigSingleton.getInstance().getActionsPath(); %>
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
			<jsp:include  page="templates/loginTpl.jsp" />
        <%}
        else
        {%>
            <h2>Main - <%=session.getAttribute("currentLogin")%></h2>
			<ul>
				<li><a href="profile.jsp">Profile</a></li>
				<li><a href="<%= actionsPath %>/logout?redirect=../main.jsp">Logout</a></li>
			</ul>
			
            <hr size="2"/>
			<a href="addWords.jsp">Add Word</a>
            <form method="post" action="<%= actionsPath %>/getWords">
                <input type="submit" name="getWords" value="  Get Words  ">
            </form>
            <form method="post" action="main.jsp">
                <input type="submit" name="update" value="Обновить">
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