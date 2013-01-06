getFlickr = {html: [], tags: [], triggerClass: "getflickrphotos", loadingMessage: "loading...", viewerID: "flickrgetviewer", closeMessage: "close", closePhotoMessage: "click to close", leech: function (a, c) {
    getFlickr.func = c;
    getFlickr.tag = a;
    var b = document.createElement("script");
    b.src = "http://flickr.com/services/feeds/photos_public.gne?tags=" + a + "&format=json";
    document.getElementsByTagName("head")[0].appendChild(b)
}, getLinks: function () {
    var a = document.getElementsByTagName("a");
    for (var c = 0, b = a.length; c < b; c++) {
        if (a[c].className.indexOf(getFlickr.triggerClass) != -1) {
            getFlickr.addEvent(a[c], "click", getFlickr.getData);
            if ((/Safari|Konqueror|KHTML/gi).test(navigator.userAgent)) {
                a[c].onclick = function () {
                    return false
                }
            }
        }
    }
}, getData: function (c) {
    var b = getFlickr.getTarget(c);
    if (b.nodeName.toLowerCase() != "a") {
        b = b.parentNode
    }
    var a = b.href.match(/([\w|\+]+)?\/?$/);
    getFlickr.currentLink = b;
    getFlickr.currentText = b.innerHTML;
    b.innerHTML = getFlickr.loadingMessage;
    getFlickr.leech(a, "getFlickr.feedLink");
    getFlickr.cancelClick(c)
}, feedLink: function () {
    getFlickr.currentLink.innerHTML = getFlickr.currentText;
    var b = document.getElementById(getFlickr.viewerID);
    if (b === null) {
        var b = document.createElement("div");
        b.id = getFlickr.viewerID;
        document.body.appendChild(b)
    } else {
        b.innerHTML = ""
    }
    var d = document.createElement("a");
    d.href = "#";
    d.innerHTML = getFlickr.closeMessage;
    d.onclick = function () {
        this.parentNode.parentNode.removeChild(this.parentNode);
        return false
    };
    b.appendChild(d);
    var a = document.createElement("ul");
    a.innerHTML = getFlickr.html[getFlickr.tag].replace(/_m/g, "_s");
    b.appendChild(a);
    var c = 0;
    if (self.pageYOffset) {
        c = self.pageYOffset
    } else {
        if (document.documentElement && document.documentElement.scrollTop) {
            c = document.documentElement.scrollTop
        } else {
            if (document.body) {
                c = document.body.scrollTop
            }
        }
    }
    b.style.top = c + "px";
    getFlickr.addEvent(a, "click", getFlickr.showPic)
}, showPic: function (g) {
    var c = getFlickr.getTarget(g);
    if (c.nodeName.toLowerCase() === "img") {
        var f = document.getElementById("flickrgetviewer");
        var d = c.src.replace("_s", "_m");
        var b = c.parentNode.cloneNode(false);
        var a = document.createElement("div");
        b.innerHTML = '<img src="' + d + '" title="Click to Close" />';
        a.appendChild(b);
        if (f.getElementsByTagName("div").length > 0) {
            f.replaceChild(a, f.getElementsByTagName("div")[0])
        } else {
            f.appendChild(a)
        }
        a.onclick = function () {
            this.parentNode.removeChild(this);
            return false
        }
    }
    getFlickr.cancelClick(g)
}, returnList: function (feed) {
    var x = feed.items;
    var t;
    getFlickr.html[getFlickr.tag] = "";
    getFlickr.tags[getFlickr.tag] = "";
    for (var i = 0, j = x.length; i < j; i++) {
        getFlickr.html[getFlickr.tag] += '<li><a href="' + x[i].link + '"><img src="' + x[i].media.m + '" alt="' + x[i].title + '" /></a></li>';
        t += x[i].tags + " "
    }
    t = t.replace(/\s$/, "");
    var x = t.split(" ");
    x = x.sort();
    for (var i = 0, j = x.length; i < j; i++) {
        if (i > 0 && x[i - 1] != x[i]) {
            getFlickr.tags[getFlickr.tag] += x[i] + " "
        }
    }
    if (getFlickr.func !== undefined) {
        eval(getFlickr.func + "()")
    }
}, getTarget: function (b) {
    var a = window.event ? window.event.srcElement : b ? b.target : null;
    if (!a) {
        return false
    }
    while (a.nodeType != 1 && a.nodeName.toLowerCase() != "body") {
        a = a.parentNode
    }
    return a
}, cancelClick: function (a) {
    if (window.event) {
        window.event.cancelBubble = true;
        window.event.returnValue = false
    }
    if (a && a.stopPropagation && a.preventDefault) {
        a.stopPropagation();
        a.preventDefault()
    }
}, addEvent: function (e, d, b, a) {
    if (e.addEventListener) {
        e.addEventListener(d, b, a);
        return true
    } else {
        if (e.attachEvent) {
            var c = e.attachEvent("on" + d, b);
            return c
        } else {
            e["on" + d] = b
        }
    }
}};
function jsonFlickrFeed(a) {
    getFlickr.returnList(a)
}
getFlickr.addEvent(window, "load", getFlickr.getLinks);
