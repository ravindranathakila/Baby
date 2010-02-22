var yoxviewPath = "/js/jquery.yoxview/";

document.write('<link rel="Stylesheet" type="text/css" href="' + yoxviewPath + 'yoxview.css" />');

function LoadScript( url )
{
	document.write( '<scr' + 'ipt type="text/javascript" src="' + url + '"><\/scr' + 'ipt>' ) ;
}
//LoadScript("http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js");
//LoadScript(yoxviewPath + "jquery.timers-1.2.min.js");
//LoadScript(yoxviewPath + "jquery.yoxview-0.9.min.js");

// Remove the next line's comment to apply yoxview without knowing jQuery to all containers with class 'yoxview':
//LoadScript(yoxviewPath + "yoxview-nojquery.js");