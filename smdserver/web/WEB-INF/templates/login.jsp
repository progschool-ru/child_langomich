<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<jsp:useBean id="smdconfig" class="org.smdserver.core.SmdConfigBean" scope="application"/>
<smd:loginForm loginAction="${smdconfig.actionsPath}/login?redirect=page/words"/>

