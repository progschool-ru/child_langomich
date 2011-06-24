<%@ include file='JSONObject.jsp'%>
<%@ page import="org.smdserver.words.Language" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <title>AddWords</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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

        <script language="JavaScript">
            var hellow = "Hellow";
            function addWord(form) {
                var t =  '"';
                var r;
                for(i=0; i < form.rating.length; i++){
                    if (form.rating[i].checked == true){
                        r = form.rating[i].value;
                    }
                }
                if(form.newLen.value== "")
                    l = form.language.value;
                else
                    l = form.newLen.value;
                if(form.original.value == "" || form.translation.value == "" ) {
                    alert("This form was completed incorrectly");
                    return false;
                }
                data = "{languages:[{name:"+t+l+t;
                data = data+",words:[{original:"+t+form.original.value+t;
                data = data+",translation:"+t+form.translation.value+t+",rating:";
                <%
                    String date = "2010-04-28 20:05:23 UTC+7";
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZ");
                    Date d = new Date();
                    date = s.format(d).toString();
                %>                
                data = data+r+",modified:"+t+"<%=date%>"+t+"}]}]}";
                form.data.value = data;
                return true;
            }
        </script>
        <h2>AddWords - <%=session.getAttribute("currentLogin")%></h2>
            <form method="post" action="enter">
                <input type="submit" name="newPassword" value="  Main  ">
            </form>
            <hr size="2"/>
            <form method="post" action="addWord">
                <input type="hidden" name="data">
                <fieldset>
                    <table width="50%">
                        <tr>
                            <th>Language</th>
                            <th>Word</th>
                        </tr>
                        <tr>
                            <td width="200">
                                Select language:<br>
                                <select name="language" size="1">
                                    <%
                                    for(int i = 0; i < languages.size(); i++) {
                                        Language l = (Language)languages.get(i); %>
                                        <option value="<%=l.getName()%>">
                                            <%=l.getName()%>
                                        </option>
                                    <%}%>
                                </select><br><br>
                                New language:<input type="text" name="newLen">
                            </td>
                            <td width="200">
                                Original:<input type="text" name="original">
                                Translation:<input type="text" name="translation">
                                Knowledge:<br>
                                <input type="radio" name="rating" value="0" checked="checked" />I don't know <br>                               
                                <input type="radio" name="rating" value="2"/>Bad<br>
                                <input type="radio" name="rating" value="4"/>Normally<br>
                                <input type="radio" name="rating" value="6"/>Good<br>
                                <input type="radio" name="rating" value="6"/>Very good
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <input type="submit" name="test" value="  AddWord  " onClick="return(addWord(this.form));">
            </form>
    </body>
</html>