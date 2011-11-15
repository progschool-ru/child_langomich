function sendAlert(text){
	chrome.tabs.executeScript(null, sAlert(text));
}

function sAlert(text){
	alert(text);
}

//Убираем символы экранирования
//Скорее всего тоже надо переделать
function copyStr(str){
	var NewStr = "";
		for(var i=0; i<str.length; i++){
			if(str.charAt(i) != "\\") NewStr = NewStr+str.charAt(i);
		}
	return NewStr;
}

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
	//найти причину почему не добовляет
	addWords : function(words, handler){
		$.ajax({
			url: "http://lang.omich.net/smdserver/servlet/addWords",
			type: "POST",
			data: {data: JSON.stringify(words)},
			success: function(){ 
						handler();
					},
			error: function(){ sendAlert("error");} 
		});
	}
	
	
}
