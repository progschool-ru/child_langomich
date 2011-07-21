package org.smdserver.jsp;

import java.util.Map;

public class SmdLink extends SmdUrl implements ILink
{
	private String text;

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
				        ? "<span class=\"current\">%2$s</span>"
						: "<a href=\"%1$s\">%2$s</a>";

		return String.format(format, getURL(), getText());
	}

	private void construct(String text)
	{
		this.text = text;
	}
}
