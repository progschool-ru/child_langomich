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
}