<%@ tag import="org.smdserver.core.SmdConfigBean" %>
<%!
	private class Link {
		public String url;
		public String text;
		public Link(String url, String text)
		{
			this.url = url;
			this.text = text;
		}
	}

	private String actionsPath = SmdConfigBean.getInstance().getActionsPath();

	private Link [] links = {
		new Link("main.jsp", "Main"),
		new Link("profile.jsp", "Profile"),
		new Link(actionsPath + "/logout?redirect=../main.jsp", "Logout")
	};
%>
<div class="menu">
	<ul>
		<% for(Link link : links) { %>
			<li><a href="<%= link.url %>"><%= link.text %></a></li>
		<% } %>
	</ul>
</div>