if(!window.langOmichIsActive){
	chrome.extension.onRequest.addListener(
		function (req, sender,sendResponse) {
			if(req.message == 'getSelected') {
				var str = window.getSelection().toString();
					if(str !== '') {
						sendResponse({'text' : str});
					}
			}
		}		
	);

	window.langOmichIsActive = true;
}