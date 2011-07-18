<%@ tag import="org.smdserver.core.SmdConfigBean" %>
<%@ tag import="org.smdserver.jsp.*" %>
<%@ tag pageEncoding="UTF-8" %>
<%-- TODO: (2.medium) Объединить код с menu.tag --%>
<%!
	private String actionsPath = SmdConfigBean.getInstance().getActionsPath();

	private ILink [] links = {
		new SimpleLink("addWords.jsp", "Add Word"),
		new SimpleLink(actionsPath + "/getWords", "Get Words JSON"),
		new SimpleLink("javascript:location.reload(true)", "Refresh"),
	};
%>
<div class="menu">
	<ul>
		<% for(ILink link : links) { %>
			<li><a href="<%= link.getURL() %>"><%= link.getText() %></a></li>
		<% } %>
	</ul>
</div>