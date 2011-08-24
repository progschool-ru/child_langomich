package org.smdserver.maintenance;

import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.core.actions.SmdAction;

abstract class UMaintenanceAction extends SmdAction
{
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
}
