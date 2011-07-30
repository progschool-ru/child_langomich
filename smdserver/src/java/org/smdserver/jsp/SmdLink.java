package org.smdserver.jsp;

import java.util.Map;

public class SmdLink extends SmdUrl implements ILink
{
	private String text;
	private String cssClass;

	public SmdLink (String internalUrl, String text,
							SmdUrl currentLink,
							String basePath,
							Map<String, Object> parameters)
	{
		super(internalUrl, currentLink, basePath, parameters);
		construct(text);
	}

	public SmdLink(String servlet, String action, String text)
	{
		super(servlet, action, null, null, null);
		construct(text);
	}


	public SmdLink(String servlet, String action, String text,
					SmdUrl currentLink, String basePath,
					Map<String, Object> parameters)
	{
		super(servlet, action, currentLink, basePath, parameters);
		construct(text);
	}

	public String getText()
	{
		if(text == null)
		{
			return getURL();
		}
		return text;
	}

	public String getHTML()
	{
		String format = isTheSamePath()
				        ? (cssClass == null ? "<span class=\"current\">%2$s</span>"
							: "<span class=\"current %3$s\">%2$s</span>")
						: (cssClass == null ? "<a href=\"%1$s\">%2$s</a>"
							: "<a href=\"%1$s\" class=\"%3$s\">%2$s</a>");


		return String.format(format, getURL(), getText(), cssClass);
	}

	public void setCSSClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

	private void construct(String text)
	{
		this.text = text;
	}
}
