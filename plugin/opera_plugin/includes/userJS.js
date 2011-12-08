window.addEventListener( "load", function(){
	
	opera.postError("lang.omich.userJS load");
	
	window.addEventListener( 'mouseup', function(){
		opera.extension.postMessage({message:"Selection", data:window.getSelection().toString()});
		opera.postError("lang.omich.userJS mouseup");
	},false);

}, false);