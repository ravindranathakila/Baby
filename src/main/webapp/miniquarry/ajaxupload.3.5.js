(function () {
    var v = document, r = window;

    function A(a) {
        if (typeof a == "string") {
            a = v.getElementById(a)
        }
        return a
    }

    function y(b, c, e) {
        if (r.addEventListener) {
            b.addEventListener(c, e, false)
        } else {
            if (r.attachEvent) {
                var a = function () {
                    e.call(b, r.event)
                };
                b.attachEvent("on" + c, a)
            }
        }
    }

    var z = function () {
        var a = v.createElement("div");
        return function (c) {
            a.innerHTML = c;
            var b = a.childNodes[0];
            a.removeChild(b);
            return b
        }
    }();

    function x(a, b) {
        return a.className.match(new RegExp("(\\s|^)" + b + "(\\s|$)"))
    }

    function w(a, b) {
        if (!x(a, b)) {
            a.className += " " + b
        }
    }

    function q(a, c) {
        var b = new RegExp("(\\s|^)" + c + "(\\s|$)");
        a.className = a.className.replace(b, " ")
    }

    if (document.documentElement.getBoundingClientRect) {
        var p = function (e) {
            var k = e.getBoundingClientRect(), g = e.ownerDocument, j = g.body, c = g.documentElement, l = c.clientTop || j.clientTop || 0, i = c.clientLeft || j.clientLeft || 0, f = 1;
            if (j.getBoundingClientRect) {
                var a = j.getBoundingClientRect();
                f = (a.right - a.left) / j.clientWidth
            }
            if (f > 1) {
                l = 0;
                i = 0
            }
            var h = k.top / f + (window.pageYOffset || c && c.scrollTop / f || j.scrollTop / f) - l, b = k.left / f + (window.pageXOffset || c && c.scrollLeft / f || j.scrollLeft / f) - i;
            return{top: h, left: b}
        }
    } else {
        var p = function (c) {
            if (r.jQuery) {
                return jQuery(c).offset()
            }
            var a = 0, b = 0;
            do {
                a += c.offsetTop || 0;
                b += c.offsetLeft || 0
            } while (c = c.offsetParent);
            return{left: b, top: a}
        }
    }
    function B(e) {
        var b, f, c, g;
        var a = p(e);
        b = a.left;
        c = a.top;
        f = b + e.offsetWidth;
        g = c + e.offsetHeight;
        return{left: b, right: f, top: c, bottom: g}
    }

    function t(a) {
        if (!a.pageX && a.clientX) {
            var b = 1;
            var e = document.body;
            if (e.getBoundingClientRect) {
                var c = e.getBoundingClientRect();
                b = (c.right - c.left) / e.clientWidth
            }
            return{x: a.clientX / b + v.body.scrollLeft + v.documentElement.scrollLeft, y: a.clientY / b + v.body.scrollTop + v.documentElement.scrollTop}
        }
        return{x: a.pageX, y: a.pageY}
    }

    var u = function () {
        var a = 0;
        return function () {
            return"ValumsAjaxUpload" + a++
        }
    }();

    function d(a) {
        return a.replace(/.*(\/|\\)/, "")
    }

    function s(a) {
        return(/[.]/.exec(a)) ? /[^.]+$/.exec(a.toLowerCase()) : ""
    }

    Ajax_upload = AjaxUpload = function (b, e) {
        if (b.jquery) {
            b = b[0]
        } else {
            if (typeof b == "string" && /^#.*/.test(b)) {
                b = b.slice(1)
            }
        }
        b = A(b);
        this._input = null;
        this._button = b;
        this._disabled = false;
        this._submitting = false;
        this._justClicked = false;
        this._parentDialog = v.body;
        if (window.jQuery && jQuery.ui && jQuery.ui.dialog) {
            var a = jQuery(this._button).parents(".ui-dialog");
            if (a.length) {
                this._parentDialog = a[0]
            }
        }
        this._settings = {action: "upload.php", name: "userfile", data: {}, autoSubmit: true, responseType: false, onChange: function (g, f) {
        }, onSubmit: function (g, f) {
        }, onComplete: function (f, g) {
        }};
        for (var c in e) {
            this._settings[c] = e[c]
        }
        this._createInput();
        this._rerouteClicks()
    };
    AjaxUpload.prototype = {setData: function (a) {
        this._settings.data = a
    }, disable: function () {
        this._disabled = true
    }, enable: function () {
        this._disabled = false
    }, destroy: function () {
        if (this._input) {
            if (this._input.parentNode) {
                this._input.parentNode.removeChild(this._input)
            }
            this._input = null
        }
    }, _createInput: function () {
        var c = this;
        var e = v.createElement("input");
        e.setAttribute("type", "file");
        e.setAttribute("name", this._settings.name);
        var a = {position: "absolute", margin: "-5px 0 0 -175px", padding: 0, width: "220px", height: "30px", fontSize: "14px", opacity: 0, cursor: "pointer", display: "none", zIndex: 2147483583};
        for (var b in a) {
            e.style[b] = a[b]
        }
        if (!(e.style.opacity === "0")) {
            e.style.filter = "alpha(opacity=0)"
        }
        this._parentDialog.appendChild(e);
        y(e, "change", function () {
            var f = d(this.value);
            if (c._settings.onChange.call(c, f, s(f)) == false) {
                return
            }
            if (c._settings.autoSubmit) {
                c.submit()
            }
        });
        y(e, "click", function () {
            c.justClicked = true;
            setTimeout(function () {
                c.justClicked = false
            }, 3000)
        });
        this._input = e
    }, _rerouteClicks: function () {
        var c = this;
        var b, e = {top: 0, left: 0}, a = false;
        y(c._button, "mouseover", function (f) {
            if (!c._input || a) {
                return
            }
            a = true;
            b = B(c._button);
            if (c._parentDialog != v.body) {
                e = p(c._parentDialog)
            }
        });
        y(document, "mousemove", function (g) {
            var h = c._input;
            if (!h || !a) {
                return
            }
            if (c._disabled) {
                q(c._button, "hover");
                h.style.display = "none";
                return
            }
            var f = t(g);
            if ((f.x >= b.left) && (f.x <= b.right) && (f.y >= b.top) && (f.y <= b.bottom)) {
                h.style.top = f.y - e.top + "px";
                h.style.left = f.x - e.left + "px";
                h.style.display = "block";
                w(c._button, "hover")
            } else {
                a = false;
                if (!c.justClicked) {
                    h.style.display = "none"
                }
                q(c._button, "hover")
            }
        })
    }, _createIframe: function () {
        var a = u();
        var b = z('<iframe src="javascript:false;" name="' + a + '" />');
        b.id = a;
        b.style.display = "none";
        v.body.appendChild(b);
        return b
    }, submit: function () {
        var g = this, c = this._settings;
        if (this._input.value === "") {
            return
        }
        var f = d(this._input.value);
        if (!(c.onSubmit.call(this, f, s(f)) == false)) {
            var e = this._createIframe();
            var a = this._createForm(e);
            a.appendChild(this._input);
            a.submit();
            v.body.removeChild(a);
            a = null;
            this._input = null;
            this._createInput();
            var b = false;
            y(e, "load", function (h) {
                if (e.src == "javascript:'%3Chtml%3E%3C/html%3E';" || e.src == "javascript:'<html></html>';") {
                    if (b) {
                        setTimeout(function () {
                            v.body.removeChild(e)
                        }, 0)
                    }
                    return
                }
                var i = e.contentDocument ? e.contentDocument : frames[e.id].document;
                if (i.readyState && i.readyState != "complete") {
                    return
                }
                if (i.body && i.body.innerHTML == "false") {
                    return
                }
                var j;
                if (i.XMLDocument) {
                    j = i.XMLDocument
                } else {
                    if (i.body) {
                        j = i.body.innerHTML;
                        if (c.responseType && c.responseType.toLowerCase() == "json") {
                            if (i.body.firstChild && i.body.firstChild.nodeName.toUpperCase() == "PRE") {
                                j = i.body.firstChild.firstChild.nodeValue
                            }
                            if (j) {
                                j = window["eval"]("(" + j + ")")
                            } else {
                                j = {}
                            }
                        }
                    } else {
                        var j = i
                    }
                }
                c.onComplete.call(g, f, j);
                b = true;
                e.src = "javascript:'<html></html>';"
            })
        }
    }, _createForm: function (c) {
        var e = this._settings;
        var b = z('<form method="post" enctype="multipart/form-data"></form>');
        b.style.display = "none";
        b.action = e.action;
        b.target = c.name;
        v.body.appendChild(b);
        for (var a in e.data) {
            var f = v.createElement("input");
            f.type = "hidden";
            f.name = a;
            f.value = e.data[a];
            b.appendChild(f)
        }
        return b
    }}
})();
