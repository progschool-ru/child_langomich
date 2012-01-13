function sAlert(text){
	alert(text);
};
function sendAlert(text){
	chrome.tabs.executeScript(null, sAlert(text));
};

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

SMDServer = {
	login: "",
	password: "",
	
	setAuthData: function(Login, Password){
		login = Login;
		password = Password;
	},
	
	auth : function(hendler){
		var param = {
			url: "http://lang.omich.net/smdserver/servlet/login",
			type: "POST",
			data: "login="+login+"&password="+password,
			success: function(response){
				if(hendler) hendler(JSON.parse(copyStr(response)))} 
		};
		  return $.ajax(param);
	},
	
	loadLanguages : function(hendler){
		this.auth(null).done(function(){
			$.ajax({
				url: "http://lang.omich.net/smdserver/servlet/getLanguages",
				success: function(response){ if(hendler) hendler(JSON.parse(copyStr(response))) } 
			});
		});
	},

	sendWords : function(words, hendler){
		console.log("sendWords");
			$.ajax({
				url: "http://lang.omich.net/smdserver/servlet/addWords",
				type: "POST",
				data: {data: JSON.stringify(words)},
				success: function(response){ console.log(response); if(hendler)  hendler(JSON.parse(copyStr(response)));}, 
		});
	}
};

Words = {

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
					name : this.name,
					words : this.words 
				}]
		}
		if(this.id != null) data.languages[0].id = this.id;
		return data;
	},
	
	isCorrect: function(){
		return (this.words.length !=0) && (this.name != "");
	}
};

//класс который генерирует html код формы
var Form = function(container){
	var newLang = false;
	var inputCounter = 0;
	//languages
	var languagesDiv = $('<div>');
	languagesDiv.addClass('languages');
	container.append(languagesDiv);
	//select
	var select = $('<select>', { id: "languages"});
	languagesDiv.append(select);
	//new lang
	var newLangInput = $('<input>', {size: 2});
	newLangInput.hide();
	languagesDiv.append(newLangInput);
	//span lang
	var langSpan = $('<span>');
	langSpan.html("новый язык");
	languagesDiv.append(langSpan);
	langSpan.click(function(){
		if(newLang){
			newLangInput.hide();
			select.show();
			newLang = false;
			langSpan.html("новый язык");
		}else{
			select.hide();
			newLangInput.show();
			newLang = true;
			langSpan.html("cуществующий язык");
		}
	});
	
	//столбец ввода
	var inputContainer = $('<div>');
	container.append(inputContainer);
	
	var addContainer = $('<div>');
	addContainer.addClass("add");
	container.append(addContainer);
	var addSpan = $('<span>');
	addContainer.append(addSpan);
	addSpan.append('Еще');
				
	addSpan.click(function(){
		addInputStr();
	});
	
	var saveDiv = $('<div>');
	saveDiv.addClass("save");
	container.append(saveDiv);
	var button = $('<input>', { type:"button", value:"Добавить" })
	saveDiv.append(button);
	
	button.click(function(){
		if(newLang){
			localStorage["languades"] = newLangInput.attr('value');
			Words.setLanguage("",newLangInput.attr('value'));
		}else{
			localStorage["languades"] = $('#languages :selected').text();
		    Words.setLanguage($('#languages :selected').val(),$('#languages :selected').text());
		}
			var original = "";
			var translate = "";
			for(var i=0; i<$('.original:input').length; i++){
				original = $('.original:input')[i].value;
				translate = $('.translate:input')[i].value;
				if (original != "" && translate != ""){
					console.log(original+" "+translate);
					Words.addWord(translate,original);
				}
			}
		if(Words.isCorrect()){
			SMDServer.sendWords(Words.getData(), function(response){window.close();}); 
		}
		
		Words.clearWords();
	});
	
	addInputStr();
	//как изменить размер popup ?
	function addInputStr(){
		inputCounter++;
		var div = $('<div>', {id : 'input'});
		div.addClass('inputSt');
		inputContainer.append(div);
		
					
		var original  = $('<input>');
		original.addClass('original');
		var translate = $('<input>');
		translate.addClass('translate');
		div.append(original);
		div.append(translate);
		
		var divDel = $('<div>');
		divDel.addClass('Del');
			div.append(divDel);
			divDel.append('X');				
		    divDel.click(function(){
				if(inputCounter > 1){
					div.remove();
					inputCounter--;
				}
			})
	};
	
};
