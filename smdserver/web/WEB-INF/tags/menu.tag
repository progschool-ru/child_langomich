<%@ tag import="org.smdserver.core.SmdConfigBean" %>
<%@ tag import="org.smdserver.jsp.*" %>
<%!
	private String actionsPath = SmdConfigBean.getInstance().getActionsPath();

	private ILink [] links = {
		new SimpleLink("main.jsp", "Main"),
		new SimpleLink("profile.jsp", "Profile"),
		new SimpleLink(actionsPath + "/logout?redirect=../main.jsp", "Logout")
	};
%>
<div class="menu">
	<ul>
		<% for(ILink link : links) { %>
			<li><a href="<%= link.getURL() %>"><%= link.getText() %></a></li>
		<% } %>
	</ul>
</div>