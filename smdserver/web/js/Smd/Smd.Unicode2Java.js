/**
 * Source (modified here a bit): http://www.snible.org/java2/uni2java.html
 * Original license is not quite clear:
 *   "Copyright 2001 by Ed Snible (esnible@acm.org) and released under an appropriate open source license."
 */

Smd = window.Smd || {}; 
Smd.Unicode2Java = {
	_symbs :    "0123456789ABCDEF",

	uni2java :  function (uni)
	{
		var lit = "";
		for (var i = 0; i < uni.length; i++)
		{
			var v = uni.charCodeAt(i);
			lit = lit + this._uni2j(v);
		}
		return lit;
	},

	_hexdigit : function (v)
	{
		return this._symbs.charAt(v & 0x0f);
	},

	_hexval :   function (v)
	{
		return this._hexdigit(v >>> 12) + this._hexdigit(v >>> 8) + this._hexdigit(v >>> 4) + this._hexdigit(v);
	},

	_uni2j :    function (val)
	{
		if (val == 10) return "\\n";
		else if (val == 13) return "\\r";
		else if (val == 92) return "\\\\";
		else if (val == 34) return "\\\"";
		else if (val < 32 || val > 126) return "\\u" + this._hexval(val);
		else return String.fromCharCode(val);
	}
};
