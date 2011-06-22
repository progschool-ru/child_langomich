<%@ include file='JSONObject.jsp'%>
<%@ page import="org.smdserver.words.Language" %>
<%@ page import="org.smdserver.form.Form" %>
<html>
    <head>
        <title>AddWords</title>
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
        <jsp:useBean id="form" scope="request" class="org.smdserver.form.Form">
            <jsp:setProperty name = "form" property="*"/>
        </jsp:useBean>
            <h2>AddWords - <%=session.getAttribute("currentLogin")%></h2>
            <form method="post" action="enter">
                <input type="submit" name="newPassword" value="  Main  ">
            </form>
            <hr size="2"/>
            <form>
                NAME:<input type="text" name="name" value="{<%=form.getName()%>}">
                <input type="submit" name="test">
            </form>
            <%if(!form.getName().equals("")) {%>
                <form method="post" action="setWords">
                    <p>Data:<input type="hidden" name="data" value="<%=form.getName()%>"></p>
                   
                </form>
            <%}%>
            DATA:
            <br/>
            <p>{languages:[{name:"english",words:[{original:<br/>
               "????"  ,translation:"mother",rating:3,modified:<br/>
               "2010-04-28 20:05:23 UTC+7"}]}]}
            </p>
                    
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
                                    <td width="300-"><%=l.getWords().get(j).getModified().toString()%></td>
                                </tr>
                            <%}%>
                        <%}%>
                    </table>
    </body>
</html>