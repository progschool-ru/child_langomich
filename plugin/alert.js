function SendAlert(text){
	chrome.tabs.executeScript(null, SAlert(text));
}

function SAlert(text){
	alert(text);
}
