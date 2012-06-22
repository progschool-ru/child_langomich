org = {};
org.omich = {
	Class: Class,

	ns: function (namespace, parent)
	{
		var prevIndex = 0;
		var nextIndex = namespace.indexOf(".", 0);
		parent = parent || window;

		do
		{
			nextIndex = namespace.indexOf(".", prevIndex);
			var key = nextIndex >= 0 ? namespace.substring(prevIndex, nextIndex) : namespace.substring(prevIndex);
			parent[key] = parent[key] || {};
			parent = parent[key];
			prevIndex = nextIndex + 1;
		}
		while(nextIndex >= 0);
		
		return parent;
	},
	
	nsSelf: function (namespace) {return org.omich.ns(namespace, this);},
	log: function (value) {console.log(value);},
	
	setCookie: function (name, value, expires, path, domain, secure)
	{
		document.cookie = name + "=" + escape(value) +
			((expires) ? "; expires=" + expires : "") +
			((path) ? "; path=" + path : "") +
			((domain) ? "; domain=" + domain : "") +
			((secure) ? "; secure" : "");
	},
	
	getCookie: function (name)
	{
		var cookie = " " + document.cookie;
		var search = " " + name + "=";
		var setStr = null;
		var offset = 0;
		var end = 0;
		if (cookie.length > 0)
		{
			offset = cookie.indexOf(search);
			if (offset != -1)
			{
				offset += search.length;
				end = cookie.indexOf(";", offset)
				if (end == -1)
				{
					end = cookie.length;
				}
				setStr = unescape(cookie.substring(offset, end));
			}
		}
		return(setStr);
	}
}