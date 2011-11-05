//отправляем на фоновую странцу выделеный текст по событию mouseup
document.addEventListener("mouseup", function (b) {
	chrome.extension.sendRequest({
		message : "SendSelection",
		text : "" + window.getSelection()
		});
}, true);