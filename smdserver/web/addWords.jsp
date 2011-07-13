<%@ page import="org.smdserver.words.Language" %>
<%@ page contentType="text/html; charset=utf8" pageEncoding="UTF-8"%>
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
function hexdigit(v) {
hexdigitSymbs = "0123456789ABCDEF";
return hexdigitSymbs.charAt(v & 0x0f);
}
function hexval(v) {
return hexdigit(v >>> 12) + hexdigit(v >>> 8) + hexdigit(v >>> 4) + hexdigit(v);
}
function uni2j(val) {
if (val == 10) return "\\n"
else if (val == 13) return "\\r"
else if (val == 92) return "\\\\"
else if (val == 34) return "\\\""
else if (val < 32 || val > 126) return "\\u" + hexval(val)
else return String.fromCharCode(val);
}
function uni2java(uni) {
var lit = '';
for (var i = 0; i < uni.length; i++) {
var v = uni.charCodeAt(i);
lit = lit + uni2j(v);
}
return lit;
}




            var hellow = "Hellow";
            function addWord(form) {
                var date = new Date().getTime();
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
                if(form.original.value == "" || form.translation.value == "" ||
                    (form.newLen.value=="" && form.language.value=="")) {
                    alert("This form was completed incorrectly");
                    return false;
                }
                var data = "{languages:[{name:"+t+l+t;
                data = data+",words:[{original:"+t+form.original.value+t;
                data = data+",translation:"+t+form.translation.value+t+",rating:";              
                data = data+r+",modified:"+t+date+t+"}]}]}";
                form.data.value = uni2java(data);
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
                            <td width="300">
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
                            <td width="400">
                                <p>Original:<input type="text" name="original"></p>
                                <p>Translation:<input type="text" name="translation"></p>
                                Knowledge:<br>
                                <input type="radio" name="rating" value="0" checked="checked" />I don't know <br>                               
                                <input type="radio" name="rating" value="2"/>Bad<br>
                                <input type="radio" name="rating" value="4"/>Normally<br>
                                <input type="radio" name="rating" value="6"/>Good<br>
                                <input type="radio" name="rating" value="8"/>Very good
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <input type="submit" name="test" value="  AddWord  " onClick="return(addWord(this.form));">
            </form>
    </body>
</html>