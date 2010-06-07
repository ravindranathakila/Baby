window.onload=function(){
    //Nifty("div.ntyBH","big same-height");
    Nifty("div.ntyB","big");
    Nifty("div.ntyBT","big top");
    Nifty("div.ntyBTL","big tl");
    Nifty("div.ntyBTR","big tr");
    Nifty("div.ntyBB","big bottom");
    Nifty("div.ntyBBL","big bl");
    Nifty("div.ntyBBR","big br");
    Nifty("div.nty");
    Nifty("div.ntyT","top");
    Nifty("div.ntyTL","tl");
    Nifty("div.ntyTR","tr");
    Nifty("div.ntyBot","bottom");
    Nifty("div.ntyBL","bl");
    Nifty("div.ntyBR","br");
    Nifty("div.ntyS","small");
    Nifty("div.ntyST","small top");
    Nifty("div.ntySTL","small tl");
    Nifty("div.ntySTR","small tr");
    Nifty("div.ntySB","small bottom");
    Nifty("div.ntySBL","small bl");
    Nifty("div.ntySBR","small br");
}

/*
tag selector	"p" "h2"
id selector	"div#header"    "h2#about"
class selector	"div.news"  "span.date" "p.comment"
descendant selector (with id)	"div#content h2"    "div#menu a"
descendant selector (with class)	"ul.news li"    "div.entry h4"
grouping (any number and combination of the previous selectors)	"h2,h3" "div#header,div#content,div#footer" "ul#menu li,div.entry li"

tl	top left corner
tr	top right corner
bl	bottom left corner
br	bottom right corner
top	upper corners
bottom	lower corners
left	left corners
right	right corners
all (default)	all the four corners
none	no corners will be rounded (to use for nifty columns)
small	small corners (2px)
normal (default)	normal size corners (5px)
big	big size corners (10px)
transparent	inner transparent, alias corners will be obtained. This option activates automatically if the element to round does not have a background-color specified.
fixed-height	to be applied when a fixed height is set via CSS
same-height	Parameter for nifty columns: all elements identified by the CSS selector of the first parameter will have the same height. If the effect is needed without rounded corners, simply use this parameter in conjuction with the keyword none.

 */