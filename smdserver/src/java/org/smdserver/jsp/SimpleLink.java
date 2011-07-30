package org.smdserver.jsp;

public class SimpleLink implements ILink
{
		private String url;
		private String text;
		private String cssClass;

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
			if(cssClass == null)
				return String.format("<a href=\"%1$s\">%2$s</a>", url, text);

			return String.format("<a href=\"%1$s\" class=\"%2$s\">%2$s</a>", url, text, cssClass);
		}

		public void setCSSClass(String cssClass)
		{
			this.cssClass = cssClass;
		}
}
