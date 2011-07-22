<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<div class="toolbar-menu">
	<h3>Add Words</h3>
	<div class="b-addWords"></div>
	<ul>
		<li><smd:ahref text="Add Word" url="../../addWords.jsp"/></li>
		<li><smd:ahref text="Get Words JSON" url="smd://action/getWords"/></li>
		<li><smd:ahref text="Refresh" url="javascript:location.reload(true)"/></li>
	</ul>
</div>