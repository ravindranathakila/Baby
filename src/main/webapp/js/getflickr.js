getFlickr={
	html:[],
	tags:[],
	triggerClass:'getflickrphotos',
	loadingMessage:'loading...',
	viewerID:'flickrgetviewer',
	closeMessage:'close',
	closePhotoMessage:'click to close',
	leech:function(tag, func){
		getFlickr.func = func;
		getFlickr.tag = tag;
		
		var s = document.createElement('script');
		//s.src = 'http://flickr.com/services/feeds/photos_public.gne?tags=' + tag + '&format=json';
		s.src = 'http://flickr.com/services/feeds/photos_public.gne?tags=' + tag + '&format=json';
		document.getElementsByTagName('head')[0].appendChild(s);
	},	
	getLinks:function(){
		var links = document.getElementsByTagName('a');
		for(var i=0,j=links.length;i<j;i++){
			if(links[i].className.indexOf(getFlickr.triggerClass)!=-1){
				getFlickr.addEvent(links[i],'click',getFlickr.getData);
				// friggin fix this now Apple!
				if((/Safari|Konqueror|KHTML/gi).test(navigator.userAgent)){
					links[i].onclick=function(){return false;}
				}
			}
		}
	},
	getData:function(e){
		var x = getFlickr.getTarget(e);
		if(x.nodeName.toLowerCase() != 'a'){x = x.parentNode}
		var tag = x.href.match(/([\w|\+]+)?\/?$/);
		getFlickr.currentLink = x;
		getFlickr.currentText = x.innerHTML;
		x.innerHTML = getFlickr.loadingMessage;
		getFlickr.leech(tag,'getFlickr.feedLink');
		getFlickr.cancelClick(e);
		},
	feedLink:function(){
		getFlickr.currentLink.innerHTML = getFlickr.currentText;
		var viewer = document.getElementById(getFlickr.viewerID);
		if(viewer === null){
			var viewer = document.createElement('div');
			viewer.id = getFlickr.viewerID;
			document.body.appendChild(viewer);
		} else {
			viewer.innerHTML = '';
		}
		var closer = document.createElement('a');
		closer.href = '#';
		closer.innerHTML = getFlickr.closeMessage;
		closer.onclick = function(){
			this.parentNode.parentNode.removeChild(this.parentNode);
			return false;
		}
		viewer.appendChild(closer);
		var ul = document.createElement('ul');
		ul.innerHTML = getFlickr.html[getFlickr.tag].replace(/_m/g,'_s');
		viewer.appendChild(ul);
		 var y=0;
		if(self.pageYOffset){
			y=self.pageYOffset;
		} else if (document.documentElement && document.documentElement.scrollTop){
			y=document.documentElement.scrollTop;
		} else if(document.body){
			y=document.body.scrollTop;
		}
		viewer.style.top = y+'px'; 
		getFlickr.addEvent(ul, 'click', getFlickr.showPic);
	},
	showPic:function(e){
		var t = getFlickr.getTarget(e);
		if(t.nodeName.toLowerCase()==='img'){
			var p = document.getElementById('flickrgetviewer');
			var s = t.src.replace('_s','_m');
			var x = t.parentNode.cloneNode(false);
			var cont = document.createElement('div');
			x.innerHTML = '<img src="'+s+'" title="Click to Close" />';
			cont.appendChild(x);
			if(p.getElementsByTagName('div').length>0){
				p.replaceChild(cont,p.getElementsByTagName('div')[0]);				
			} else {
				p.appendChild(cont);				
			}
			cont.onclick = function(){
				this.parentNode.removeChild(this);
				return false;
			}
		}
		getFlickr.cancelClick(e);
	},
	returnList:function(feed){
		var x = feed.items;
		var t;
		getFlickr.html[getFlickr.tag] = '';
		getFlickr.tags[getFlickr.tag] = '';
		 for(var i=0,j=x.length;i<j;i++){
		 	getFlickr.html[getFlickr.tag]+='<li><a href="'+x[i].link+'"><img src="'+x[i].media.m+'" alt="'+x[i].title+'" /></a></li>';
			t+= x[i].tags + ' ';
		 }
			t=t.replace(/\s$/,'');
		var x = t.split(' ');
		x=x.sort();
		 for(var i=0,j=x.length;i<j;i++){
		 	if(i>0 && x[i-1]!=x[i]){
			  getFlickr.tags[getFlickr.tag]+=x[i]+' ';
			}
		 }
		 if(getFlickr.func !== undefined){
 			 eval(getFlickr.func+'()');
		}
	},	
	getTarget:function(e){
		var target = window.event ? window.event.srcElement : e ? e.target : null;
		if (!target){return false;}
		while(target.nodeType!=1 && target.nodeName.toLowerCase()!='body'){
			target=target.parentNode;
		}
		return target;
	},
	 cancelClick:function(e){
			if (window.event){
				window.event.cancelBubble = true;
				window.event.returnValue = false;
			}
			if (e && e.stopPropagation && e.preventDefault){
				e.stopPropagation();
				e.preventDefault();
			}
	},
	addEvent: function(elm, evType, fn, useCapture){
		if (elm.addEventListener){
			elm.addEventListener(evType, fn, useCapture);
			return true;
		} else if (elm.attachEvent) {
			var r = elm.attachEvent('on' + evType, fn);
			return r;
		} else {
			elm['on' + evType] = fn;
		}
	}
}
function jsonFlickrFeed(feed){
	getFlickr.returnList(feed);
}
getFlickr.addEvent(window, 'load', getFlickr.getLinks);	