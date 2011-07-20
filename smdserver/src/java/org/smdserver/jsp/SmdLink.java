package org.smdserver.jsp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.smdserver.core.SmdConfigBean;

public class SmdLink implements ILink
{
	private static final String ABS_PATH_PREFIX = "link.path.";

	private String action;
	private String servlet;
	private String text;
	private SmdLink currentLink;
	private String basePath;
	private Map<String, Object> parameters;
	private String servletAbsPath;

	private String servletRelPath;
	private boolean isRelPathBuilt = false;
	private String paramsURI;
	private boolean isParamsURIBuilt = false;
	private String url;

	public SmdLink(String servlet, String action, ResourceBundle rb, Map<String, Object> parameters)
	{
		construct(servlet, action, null, rb, parameters, null, null);
	}

	public SmdLink(String servlet, String action, String text,
					ResourceBundle rb, Map<String, Object> parameters,
					SmdLink currentLink, String basePath)
	{
		construct(servlet, action, text, rb, parameters, currentLink, basePath);
	}

	public void setCurrentLink(SmdLink currentLink)
	{
		this.currentLink = currentLink;
		reset();
	}

	@Override
	public String toString()
	{
		return getURL();
	}

	public String getText()
	{
		if(text == null)
		{
			return getURL();
		}
		return text;
	}

	public String getURL()
	{
		if(url == null)
		{
			url = createURL();
		}
		return url;
	}

	public String getHTML()
	{
		String format = isTheSamePath() 
				        ? "<span class=\"current\">%2$s</span>"
						: "<a href=\"%1$s\">%2$s</a>";
		
		return String.format(format, getURL(), getText());
	}

	private void construct (String servlet, String action, String text,
					ResourceBundle rb, Map<String, Object> parameters,
					SmdLink currentLink, String basePath)
	{
		this.servlet = servlet;
		this.action = action;
		this.text = text;
		this.currentLink = currentLink;
		this.basePath = basePath;
		this.parameters = parameters;

		servletAbsPath = rb.getString(ABS_PATH_PREFIX + servlet);
	}

	private void reset()
	{
		servletRelPath = null;
		isRelPathBuilt = false;
		paramsURI = null;
		isParamsURIBuilt = false;
		url = null;
	}

	private String getServletRelPath()
	{
		if(!isRelPathBuilt)
		{
			servletRelPath = createServletRelPath();
			isRelPathBuilt = true;
		}
		return servletRelPath;
	}

	private String createServletRelPath()
	{
		if(currentLink == null)
			return null;

		String currentAbsPath = currentLink.servletAbsPath;
		String [] currentFolders = currentAbsPath.split("/");
		String [] servletFolders = servletAbsPath.split("/");
		int i = 0;
		while(i < currentFolders.length && i < servletFolders.length &&
				currentFolders[i].equals(servletFolders[i]))
		{
			i++;
		}
		int relLength = currentFolders.length - i;
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < relLength; j++)
		{
			sb.append("../");
		}
		for(int j = i; j < servletFolders.length; j++)
		{
			sb.append(servletFolders[j]);
			sb.append("/");
		}
		return sb.toString();
	}

	private boolean isTheSamePath()
	{
		return currentLink != null &&
				currentLink.servlet.equals(servlet) &&
				currentLink.action.equals(action) &&
				(
					currentLink.parameters == parameters
				||
					currentLink.parameters != null && parameters != null &&
					currentLink.getParamsURI().equals(getParamsURI())
				);
	}

	private String getParamsURI()
	{
		if(!isParamsURIBuilt)
		{
			paramsURI = createParamsURI();
			isParamsURIBuilt = true;
		}
		return paramsURI;
	}
	
	private String createParamsURI()
	{
		if(parameters == null)
		{
			return null;
		}
		
		Set<Map.Entry<String, Object> > set = parameters.entrySet();
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;

		for(Map.Entry<String, Object> entry : set)
		{
			if(isFirst)
			{
				isFirst = false;
			}
			else
			{
				sb.append('&');
			}
			sb.append(entry.getKey());
			sb.append('=');

			String value;
			try
			{
				value = URLEncoder.encode(entry.getValue().toString(),
							SmdConfigBean.getInstance().getWebCharset());
			}
			catch(UnsupportedEncodingException e)
			{
				//TODO: (3.low) Надо бы сюда внедрить нормальный логгер,
				// вместо вывода в System.out.
				System.out.println("As we use correct encoding, this message: " + e.getMessage());
				value = entry.getValue().toString();
			}
			sb.append(value);
		}
		return sb.toString();
	}

	private String createURL()
	{
		StringBuilder sb = new StringBuilder();
		if(getServletRelPath() == null)
		{
			sb.append(basePath);
			sb.append(servletAbsPath);
			sb.append('/');
		}
		else
		{
			sb.append(servletRelPath);
		}
		sb.append(action);

		if(getParamsURI() != null)
		{
			sb.append(action.indexOf('?') == -1 ? '?' : '&');
			sb.append(paramsURI);
		}

		return sb.toString();
	}
}
