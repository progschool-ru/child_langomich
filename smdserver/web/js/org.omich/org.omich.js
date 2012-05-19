org = {};
org.omich = {
	ns: function (namespace, parent)
	{
		var prevIndex = 0;
		var nextIndex = namespace.indexOf(".", 0);
		var parent = parent || window;

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
	
	nsSelf: function (namespace)
	{
		return this.ns(namespace, this);
	},
	
	log: function (value) {console.log(value);},

	Class: Class,
}