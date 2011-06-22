<%@ include file='JSONObject.jsp'%>
<%@ page import="org.smdserver.words.Language" %>
<%@ page import="org.smdserver.form.Form" %>
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
        <jsp:useBean id="form" scope="request" class="org.smdserver.form.Form">
            <jsp:setProperty name = "form" property="*"/>
        </jsp:useBean>
            <% if(session.getAttribute("currentLogin")==null)
        {%>
            <h2>Main</h2>
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
        <%}
        else
        {%>
            <h2>Main - <%=session.getAttribute("currentLogin")%></h2>
            <form method="post" action="login">
                <input type="submit" name="profile" value="  Profile  ">
            </form>
            <form method="post" action="logout">
                <input type="submit" name="logout" value="  Logout  ">
            </form>

            <hr size="2"/>
            <form method="post" action="addWord">
                <p>Data:<input type="text" size="30" name="data"></p>
                <input type="submit" name="addWor" value="  addWord  ">
            </form>
            <form method="post" action="getWords">
                <input type="submit" name="addWor" value="  getWord  ">
            </form>
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
          
        <%}%>
    </body>
</html>