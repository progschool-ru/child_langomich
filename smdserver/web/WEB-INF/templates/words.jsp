<%@ taglib tagdir="/WEB-INF/tags" prefix="smd" %>
<div class="content">
	<smd:words/>
</div>
<div class="toolbar">
	<smd:toolbar/>
	<ul class="appendix">
		<li><smd:ahref text="Get Words JSON" url="smd://action/getWords"/></li>
		<li><smd:ahref text="Refresh" url="javascript:location.reload(true)"/></li>
	</ul>
</div>
