package org.smdserver.actionssystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public abstract class Action implements IAction
{
	private Map<String, Object> map = new HashMap<String, Object>();

	abstract protected String doAction (HttpServletRequest request);

	public String perform (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String url = null;
		if(!validateParams(request) || !validateContext(request))
		{
			setAnswerParam(ActionParams.SUCCESS, false);
                        System.out.println("ddsafghfdasgh1111");
		}
		else
		{
			try
			{
				url = doAction(request);
			}
			catch(Exception e)
			{
				setAnswerParam(ActionParams.SUCCESS, false);
                                System.out.println("ddsafghfdasgh22222");
			}
		}
                if (url == null) {
                    JSONObject object = new JSONObject(map);

                    PrintWriter writer = response.getWriter();
                    writer.println(object.toString());
                    writer.close();
                }
                return url;
	}

	protected void setAnswerParam (String key, Object value)
	{
		map.put(key, value);
	}

	protected boolean validateParams (HttpServletRequest request)
	{
		return true;
	}

	protected boolean validateContext (HttpServletRequest request)
	{
		return true;
	}
}
