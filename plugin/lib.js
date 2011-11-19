function sendAlert(text){
	chrome.tabs.executeScript(null, sAlert(text));
};

function sAlert(text){
	alert(text);
};
//Надо навести порядок с функциями кодирования
//Убираем символы экранирования
//Скорее всего тоже надо переделать
function copyStr(str){
	var NewStr = "";
		for(var i=0; i<str.length; i++){
			if(str.charAt(i) != "\\") NewStr = NewStr+str.charAt(i);
		}
	return NewStr;
};
//позаимствовал с кода сервера
function escapeToUtf16(str){
	var lit = "";
	for (var i = 0; i < str.length; i++)
		{
			var v = str.charCodeAt(i);
			lit = lit + StringToUni16._uni2j(v);
		}
	return lit;
	
};
//позаимствовал с кода сервера
var StringToUni16 = {
		_symbs :    "0123456789ABCDEF",

		_hexdigit : function (v){
			return this._symbs.charAt(v & 0x0f);
		},

		_hexval :   function (v){
			return this._hexdigit(v >>> 12) + this._hexdigit(v >>> 8) + this._hexdigit(v >>> 4) + this._hexdigit(v);
		},

		_uni2j :    function (val){
			if (val == 10) return "\\n";
			else if (val == 13) return "\\r";
			else if (val == 92) return "\\\\";
			else if (val == 34) return "\\\"";
			else if (val < 32 || val > 126) return "\\u" + this._hexval(val);
			else return String.fromCharCode(val);
		}
};

var SMDServer = {
	login: "",
	password: "",
	
	setAuthData: function(Login, Password){
		login = Login;
		password = Password;
	},
	
	auth : function(handler){
		var authData = "login="+login+"&password="+password;
		 $.ajax({
			url: "http://lang.omich.net/smdserver/servlet/login",
			type: "POST",
			data: authData,
			success: function(data){ 
						var аuthdata = JSON.parse(copyStr(data));
						handler(аuthdata.success);
					},
			error: function(){ sendAlert("error");} 
		});
	},
	
	loadLanguages : function(handler){
		$.ajax({
			url: "http://lang.omich.net/smdserver/servlet/getLanguages",
			success: function(data){ 
						var res = JSON.parse(copyStr(data));
						handler(res);
					},
			error: function(){ sendAlert("error");} 
		});
		
	},

	sendWords : function(words, handler){
		$.ajax({
			url: "http://lang.omich.net/smdserver/servlet/addWords",
			type: "POST",
			data: {data: JSON.stringify(words)},
			success: function(res){
						handler();
					},
			error: function(){ sendAlert("error");} 
		});
	}
	
	
};

var Words = {

	words : [],
	id : "",
	name : "",
	
	setLanguage: function(id, name){
		this.id = id;
		this.name = name;
	},
	
	addWord: function(_original, _translation ){
		var date = new Date().getTime();
		this.words.push({
					translation : _translation, 
				    original : escapeToUtf16(_original), 
				    rating :0,
					modified: date
		});
	},
	
	clearWords: function(){
		this.words.length = 0;
		this.id = "";
		this.name = "";
	},
	
	getData: function(){
		var date = new Date().getTime();
		var data = {
			currentDeviceTime: date,
				languages :[{
					id : this.id,
					name : this.name,
					words : this.words 
				}]
		}
		return data;
	}
}
