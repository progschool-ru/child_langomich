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
			this._resultArray = [];
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
			if(!this._result)
			{
				this._result = this._resultArray.join("");
			}
			return this._result;
		},

		_handleAny : function()
		{
			var next = this._readNextChar();
			switch (next)
			{
				case "\\":return this._handleSlash;
				case false: return this._handleFinish;
				default :
				{
					this._appendToResult(next);
					return this._handleAny;
				}
			}
		},
		
		_handleSlash : function()
		{
			var next = this._readNextChar();
			var sym = this._specialChars[next];

			if(sym)
			{
				this._appendToResult(sym);
				return this._handleAny;
			}
			switch (next)
			{
				case "u" : return this._handleUni;
				default  : return this._handleError;
			}
		},
		
		_handleUni : function()
		{
			var hex = "";
			var next;
			for(var i = 0; i < this._charSize; i++)
			{
				next = this._readNextChar();
				if(!next || !next.match("^[0-9a-fA-F]"))
				{
					return this._handleError;
				}
				hex += next;
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


		_readNextChar : function()
		{
			this._currentIndex ++;
			return this._currentIndex < this._string.length
				? this._string.charAt(this._currentIndex) : false;
		},

		_appendToResult : function(item)
		{
			this._resultArray.push(item);
		},

		_specialChars : {
			b: "\b",
			f: "\f",
			n: "\n",
			r: "\r",
			t: "\t",
			"\\": "\\",
			"\"": "\""
		}
	}
};

Smd.Unicode.UniToString.Converter.prototype = Smd.Unicode.UniToString;
