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
	private ISmdServletContext servletContext;
	private Map<String, Object> map = new HashMap<String, Object>();

	abstract protected String doAction(HttpServletRequest request);
	
	public void initServletContext(ISmdServletContext context)
	{
		if(servletContext == null)
			servletContext = context;
	}

	public String perform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String url = doAction(request);
		JSONObject object = new JSONObject(map);

		PrintWriter writer = response.getWriter();
		writer.println(object.toString());
		writer.close();
		return url;
	}

	protected ISmdServletContext getServletContext()
	{
		return servletContext;
	}

	protected void setAnswerParam(String key, Object value)
	{
		map.put(key, value);
	}
}
