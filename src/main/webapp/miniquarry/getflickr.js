getFlickr = {html: [], tags: [], triggerClass: "getflickrphotos", loadingMessage: "loading...", viewerID: "flickrgetviewer", closeMessage: "close", closePhotoMessage: "click to close", leech: function (e, f) {
    getFlickr.func = f;
    getFlickr.tag = e;
    var d = document.createElement("script");
    d.src = "http://flickr.com/services/feeds/photos_public.gne?tags=" + e + "&format=json";
    document.getElementsByTagName("head")[0].appendChild(d)
}, getLinks: function () {
    var e = document.getElementsByTagName("a");
    for (var f = 0, d = e.length; f < d; f++) {
        if (e[f].className.indexOf(getFlickr.triggerClass) != -1) {
            getFlickr.addEvent(e[f], "click", getFlickr.getData);
            if ((/Safari|Konqueror|KHTML/gi).test(navigator.userAgent)) {
                e[f].onclick = function () {
                    return false
                }
            }
        }
    }
}, getData: function (f) {
    var d = getFlickr.getTarget(f);
    if (d.nodeName.toLowerCase() != "a") {
        d = d.parentNode
    }
    var e = d.href.match(/([\w|\+]+)?\/?$/);
    getFlickr.currentLink = d;
    getFlickr.currentText = d.innerHTML;
    d.innerHTML = getFlickr.loadingMessage;
    getFlickr.leech(e, "getFlickr.feedLink");
    getFlickr.cancelClick(f)
}, feedLink: function () {
    getFlickr.currentLink.innerHTML = getFlickr.currentText;
    var e = document.getElementById(getFlickr.viewerID);
    if (e === null) {
        var e = document.createElement("div");
        e.id = getFlickr.viewerID;
        document.body.appendChild(e)
    } else {
        e.innerHTML = ""
    }
    var g = document.createElement("a");
    g.href = "#";
    g.innerHTML = getFlickr.closeMessage;
    g.onclick = function () {
        this.parentNode.parentNode.removeChild(this.parentNode);
        return false
    };
    e.appendChild(g);
    var f = document.createElement("ul");
    f.innerHTML = getFlickr.html[getFlickr.tag].replace(/_m/g, "_s");
    e.appendChild(f);
    var h = 0;
    if (self.pageYOffset) {
        h = self.pageYOffset
    } else {
        if (document.documentElement && document.documentElement.scrollTop) {
            h = document.documentElement.scrollTop
        } else {
            if (document.body) {
                h = document.body.scrollTop
            }
        }
    }
    e.style.top = h + "px";
    getFlickr.addEvent(f, "click", getFlickr.showPic)
}, showPic: function (i) {
    var l = getFlickr.getTarget(i);
    if (l.nodeName.toLowerCase() === "img") {
        var j = document.getElementById("flickrgetviewer");
        var k = l.src.replace("_s", "_m");
        var e = l.parentNode.cloneNode(false);
        var h = document.createElement("div");
        e.innerHTML = '<img src="' + k + '" title="Click to Close" />';
        h.appendChild(e);
        if (j.getElementsByTagName("div").length > 0) {
            j.replaceChild(h, j.getElementsByTagName("div")[0])
        } else {
            j.appendChild(h)
        }
        h.onclick = function () {
            this.parentNode.removeChild(this);
            return false
        }
    }
    getFlickr.cancelClick(i)
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
}, getTarget: function (c) {
    var d = window.event ? window.event.srcElement : c ? c.target : null;
    if (!d) {
        return false
    }
    while (d.nodeType != 1 && d.nodeName.toLowerCase() != "body") {
        d = d.parentNode
    }
    return d
}, cancelClick: function (b) {
    if (window.event) {
        window.event.cancelBubble = true;
        window.event.returnValue = false
    }
    if (b && b.stopPropagation && b.preventDefault) {
        b.stopPropagation();
        b.preventDefault()
    }
}, addEvent: function (h, i, f, g) {
    if (h.addEventListener) {
        h.addEventListener(i, f, g);
        return true
    } else {
        if (h.attachEvent) {
            var j = h.attachEvent("on" + i, f);
            return j
        } else {
            h["on" + i] = f
        }
    }
}};
function jsonFlickrFeed(b) {
    getFlickr.returnList(b)
}
getFlickr.addEvent(window, "load", getFlickr.getLinks);
