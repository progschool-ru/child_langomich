<%@ tag import="org.smdserver.core.SmdConfigBean" %>
<%@ tag pageEncoding="UTF-8" %>
<%-- TODO: (2.medium) Объединить код с menu.tag --%>
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
		new Link("addWords.jsp", "Add Word"),
		new Link(actionsPath + "/getWords", "Get Words JSON"),
		new Link("javascript:location.reload(true)", "Refresh"),
	};
%>
<div class="menu">
	<ul>
		<% for(Link link : links) { %>
			<li><a href="<%= link.url %>"><%= link.text %></a></li>
		<% } %>
	</ul>
</div>