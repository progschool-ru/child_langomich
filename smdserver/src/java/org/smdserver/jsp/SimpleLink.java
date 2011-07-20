package org.smdserver.jsp;

public class SimpleLink implements ILink
{
		private String url;
		private String text;

		public SimpleLink(String url, String text)
		{
			this.url = url;
			this.text = text;
		}

		public String getURL()
		{
			return url;
		}

		public String getText()
		{
			return text;
		}

		public String getHTML()
		{
			return String.format("<a href=\"%1$s\">%2$s</a>", url, text);
		}
}
