package org.smdserver.jsp;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import java.util.Map;
import java.util.ResourceBundle;

public class LinkCreator
{
	public ILink createLink(String internalUrl, String text, ResourceBundle rb,
			                SmdLink currentLink,
			                String basePath)
	{
		return createLink(internalUrl, text, rb, null, currentLink, basePath);
	}

	public ILink createLink(String internalUrl, String text, ResourceBundle rb,
			                Map<String, Object> parameters,
			                SmdLink currentLink,
							String basePath)
	{
		internalUrl.split("://");
		RegularExpression re = new RegularExpression("((\\w+)(://))?(.*)");
		Match match = new Match();
		re.matches(internalUrl, match);

		String linkType = match.getBeginning(2) > -1
				          ? internalUrl.substring(match.getBeginning(2), match.getEnd(2))
						  : "";

		ICreator creator = linkType.equals("smd") ? new SmdCreator() : new SimpleCreator();

		ILink link = creator.createLink(internalUrl, text, rb, parameters,
				                        currentLink, basePath);
		return link;
	}

	private interface ICreator
	{
		public ILink createLink(String url, String text, ResourceBundle rb,
				                Map<String, Object> parameters,
				                SmdLink currentLink,
				                String basePath);
	}

	private class SmdCreator implements ICreator
	{
		public ILink createLink(String url, String text, ResourceBundle rb,
				                Map<String, Object> parameters,
				                SmdLink currentLink,
				                String basePath)
		{
			url = url.substring(6); // Remove "smd://" from url
			String [] urlParts = url.split("/");
			return new SmdLink(urlParts[0], urlParts[1], text, rb, parameters,
					           currentLink, basePath);
		}
	}

	private class SimpleCreator implements ICreator
	{
		public ILink createLink(String url, String text, ResourceBundle rb,
				                Map<String, Object> parameters,
				                SmdLink currentLink,
				                String basePath)
		{
			return new SimpleLink(url, text);
		}
	}
}