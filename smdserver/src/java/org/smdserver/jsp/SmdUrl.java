package org.smdserver.jsp;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class SmdUrl implements IUrl
{
	private static final String ABS_PATH_PREFIX = "link.path.";
	private static final String WEB_CHARSET_KEY = "web.charset";

	private static ResourceBundle rb;

	private String action;
	private String servlet;
	private SmdUrl currentLink;
	private String basePath;
	private Map<String, Object> parameters;
	private String servletAbsPath;

	private String servletRelPath;
	private boolean isRelPathBuilt = false;
	private String paramsURI;
	private boolean isParamsURIBuilt = false;
	private String url;

	public SmdUrl(String internalUrl, SmdUrl currentLink,
			                 String basePath)
	{
		construct(internalUrl, currentLink, basePath, null);
	}

	public SmdUrl(String internalUrl, SmdUrl currentLink,
			                 String basePath,
							 Map<String, Object> parameters)
	{
		construct(internalUrl, currentLink, basePath, parameters);
	}
	
	public SmdUrl(String servlet, String action)
	{
		construct(servlet, action, null, null, null);
	}

	public SmdUrl(String servlet, String action,
					SmdUrl currentLink, String basePath,
					Map<String, Object> parameters)
	{
		construct(servlet, action, parameters, currentLink, basePath);
	}

	public static void initRB(ResourceBundle rb)
	{
		SmdUrl.rb = rb;
	}

	public void setCurrentLink(SmdUrl currentLink)
	{
		this.currentLink = currentLink;
		reset();
	}

	@Override
	public String toString()
	{
		return getURL();
	}

	public String getURL()
	{
		if(url == null)
		{
			url = createURL();
		}
		return url;
	}

	protected boolean isTheSamePath()
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

	private static ResourceBundle getRB()
	{
		return rb;
	}

	private void construct(String internalUrl, SmdUrl currentLink,
			                 String basePath,
							 Map<String, Object> parameters)
	{
		RegularExpression re = new RegularExpression("\\w+://(.*?)/(.*)");
		Match match = new Match();
		re.matches(internalUrl, match);
		String servlet = internalUrl.substring(match.getBeginning(1), match.getEnd(1));
		String action = internalUrl.substring(match.getBeginning(2));
		construct(servlet, action, parameters, currentLink, basePath);
	}

	private void construct (String servlet, String action,
					Map<String, Object> parameters,
					SmdUrl currentLink, String basePath)
	{
		this.servlet = servlet;
		this.action = action;
		this.currentLink = currentLink;
		this.basePath = basePath;
		this.parameters = parameters;

		servletAbsPath = getRB().getString(ABS_PATH_PREFIX + servlet);
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
						                  rb.getString(WEB_CHARSET_KEY));
			}
			catch(UnsupportedEncodingException e)
			{
				//TODO: (3.low) [#26067] Надо бы сюда внедрить нормальный логгер,
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