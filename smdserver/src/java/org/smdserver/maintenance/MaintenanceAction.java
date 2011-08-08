
package org.smdserver.maintenance;

import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.core.SmdAction;

public abstract class MaintenanceAction extends SmdAction
{
	private ResourceBundle serverRB;
	@Override
	protected boolean validateContext (HttpServletRequest request)
	{
		String password = request.getParameter(ActionParams.PASSWORD);
		
		ResourceBundle rb = ResourceBundle.getBundle(getServletContext().getConfigResourceName());
		serverRB = ResourceBundle.getBundle(rb.getString("server.properties.file"));
		
		boolean isInMaintenance = "true".equals(serverRB.getString("maintenance.allowed"));
		boolean isPasswordCorrect = password != null && password.equals(serverRB.getString("maintenance.password"));
		
		boolean success = isInMaintenance && isPasswordCorrect;
		setAnswerParam(ActionParams.SUCCESS, success);
		return success;
	}
	
	protected ResourceBundle getServerRB()
	{
		return serverRB;
	}
}
