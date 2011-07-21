package org.smdserver.jsp;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import java.util.Map;

public class LinkCreator
{
	public IUrl createUrl(String internalUrl, SmdUrl currentLink, String basePath)
	{
		ICreator creator = getCreator(internalUrl);
		return creator.createUrl(internalUrl, currentLink, basePath);
	}

	public ILink createLink(String internalUrl, String text, String basePath)
	{
		return createLink(internalUrl, text, null, basePath, null);
	}

	public ILink createLink(String internalUrl, String text,
			                SmdUrl currentLink,
							String basePath, Map<String, Object> parameters)
	{
		ICreator creator = getCreator(internalUrl);
		return creator.createLink(internalUrl, text,
				                        currentLink, basePath, parameters);
	}

	private ICreator getCreator(String internalUrl)
	{
		internalUrl.split("://");
		RegularExpression re = new RegularExpression("((\\w+)(://))?(.*)");
		Match match = new Match();
		re.matches(internalUrl, match);

		String linkType = match.getBeginning(2) > -1
				          ? internalUrl.substring(match.getBeginning(2), match.getEnd(2))
						  : "";

		return linkType.equals("smd") ? new SmdLinkCreator() : new SimpleCreator();
	}

	private static interface ICreator
	{
		public IUrl createUrl(String url, SmdUrl currentUrl, String basePath);
		
		public ILink createLink(String url, String text,
				                SmdUrl currentLink,
				                String basePath,
				                Map<String, Object> parameters);
	}

	private static class SmdLinkCreator implements ICreator
	{
		public IUrl createUrl(String url, SmdUrl currentUrl, String basePath)
		{
			return new SmdUrl(url, currentUrl, basePath);
		}
		
		public SmdLink createLink(String internalUrl, String text,
								SmdUrl currentLink,
								String basePath,
								Map<String, Object> parameters)
		{
			return new SmdLink(internalUrl, text,
							   currentLink, basePath, parameters);
		}
	}

	private static class SimpleCreator implements ICreator
	{
		public IUrl createUrl(String url, SmdUrl currentUrl, String basePath)
		{
			return new SimpleLink(url, null);
		}

		public ILink createLink(String url, String text,
				                SmdUrl currentLink,
				                String basePath,
				                Map<String, Object> parameters)
		{
			return new SimpleLink(url, text);
		}
	}
}