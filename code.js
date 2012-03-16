function showcast() {
	var newAp = document.createElement('applet');
	newAp.setAttribute('code', 'Broadcast.class');
	newAp.setAttribute('archive', 'Cast.jar');
	newAp.className = 'aplcast';
	document.body.appendChild(newAp);
	var cont = document.getElementById("cont");
	document.getElementById("gen").insertBefore(newAp, cont);
	document.getElementById("gen").removeChild(cont);
}
function showviewer() {
	var newAp = document.createElement('applet');
	newAp.setAttribute('code', 'WebViewclient.class');
	newAp.setAttribute('archive', 'View.jar');
	newAp.className = 'aplviewer';
	document.body.appendChild(newAp);
	var cont = document.getElementById("cont");
	document.getElementById("gen").insertBefore(newAp, cont);
	document.getElementById("gen").removeChild(cont);
}