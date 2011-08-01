/**
 * Source of escapeToUni16() modified a bit:
 *            http://www.snible.org/java2/uni2java.html
 * Original license is not quite clear:
 *   "Copyright 2001 by Ed Snible (esnible@acm.org) and released under an appropriate open source license."
 *
 * Source of unescapeFromUni16 is created by ProgSchool.
 */

Smd = window.Smd || {}; 
Smd.Unicode = {
	escapeToUtf16 :  function (uni)
	{
		var lit = "";
		for (var i = 0; i < uni.length; i++)
		{
			var v = uni.charCodeAt(i);
			lit = lit + this.StringToUni16._uni2j(v);
		}
		return lit;
	},

	unescapeFromUtf16 : function (value)
	{
		var converter = new this.UniToString.Converter(value, 4);
		return converter.parse();
	},

	quickUnescapeFromUtf16 : function (value)
	{
		var prep = "[\"" + value + "\"]";
		var jsonArr = JSON.parse(prep);
		return jsonArr[0];
	},

	StringToUni16 : {
		_symbs :    "0123456789ABCDEF",

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
	},

	UniToString : {

		Converter : function(string, charSize)
		{
			this._currentIndex = -1;
			this._result = "";
			this._string = string;
			this._charSize = charSize;
		},

		parse : function()
		{
			var state = this._handleAny;
			var isOk = true;

			while(isOk)
			{
				state = state.call(this);
				isOk = state != this._handleError && state != this._handleFinish;
			}

			return state.call(this);
		},

		getResult : function()
		{
			return this._result;
		},

		_handleAny : function()
		{
			var next = this._readNextCharCode();
			switch (next)
			{
				case this._cases.slash : return this._handleSlash();
				case false             : return this._handleFinish;
				default                :
				{
					this._appendToResult(String.fromCharCode(next));
					return this._handleAny;
				}
			}
		},
		
		_handleSlash : function()
		{
			var next = this._readNextCharCode();
			var sym = this._specialChars[next];

			if(sym)
			{
				this._appendToResult(sym);
				return this._handleAny;
			}
			switch (next)
			{
				case    this._cases.u : return this._handleUni(); //117 == 'u';
				default               : return this._handleError;
			}
		},
		
		_handleUni : function()
		{
			var hex = "";
			var nextCharCode;
			var cc = this._charCodes;
			for(var i = 0; i < this._charSize; i++)
			{
				nextCharCode = this._readNextCharCode();
				if(	(nextCharCode < cc.min1 || nextCharCode > cc.max1) &&
					(nextCharCode < cc.min2 || nextCharCode > cc.max2) &&
					(nextCharCode < cc.min3 || nextCharCode > cc.max3))
				{
					return this._handleError;
				}
				hex += String.fromCharCode(nextCharCode);
			}

			var code = parseInt(hex, 16);
			this._appendToResult(String.fromCharCode(code));
			return this._handleAny;
		},

		_handleError : function()
		{
			console.log(this._string);
			console.log(this._currentIndex + ": " + 
				this._string.substr(this._currentIndex, this.currentIndex + 20));
			return null;
		},

		_handleFinish : function()
		{
			return this.getResult();
		},

		_readNextCharCode : function()
		{
			this._currentIndex ++;
			return this._currentIndex < this._string.length
				? this._string.charCodeAt(this._currentIndex) : false;
		},

		_appendToResult : function(item)
		{
			this._result += item;
		},

		_specialChars : {
			34 : "\"",
			92 : "\\",
			98 : "\b",
			102: "\f",
			110: "\n",
			114: "\r",
			116: "\t",
		},

		_charCodes : {
			min1 : 48, //'0'.charCodeAt(0),
			max1 : 57, //'9'.charCodeAt(0),
			min2 : 97, //'a'.charCodeAt(0),
			max2 : 102,//'f'.charCodeAt(0),
			min3 : 65, //'A'.charCodeAt(0),
			max3 : 70, //'F'.charCodeAt(0)
		},

		_cases : {
			slash : 92, //"\\".charCodeAt(0),
			u : 117,    // "u".charCodeAt(0)
		}
	}
};

Smd.Unicode.UniToString.Converter.prototype = Smd.Unicode.UniToString;
