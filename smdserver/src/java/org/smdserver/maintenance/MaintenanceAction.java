
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
		
		IMaintenanceConfig config = getServletContext().getDBConfig();
		
		boolean isInMaintenance = config.isMaintenanceAllowed();
		boolean isPasswordCorrect = password != null && password.equals(config.getMaintenancePassword());
		
		boolean success = isInMaintenance && isPasswordCorrect;
		setAnswerParam(ActionParams.SUCCESS, success);
		return success;
	}
	
	protected ResourceBundle getServerRB()
	{
		return serverRB;
	}
}
