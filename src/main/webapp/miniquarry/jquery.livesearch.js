jQuery.fn.liveUpdate = function (d) {
    d = jQuery(d);
    if (d.length) {
        var c = d.children("li"), a = c.map(function () {
            return $("a", $("div", this)[0])[0].innerHTML.toLowerCase()
        });
        this.keyup(b).keyup().parents("form").submit(function () {
            return false
        })
    }
    return this;
    function b() {
        var e = jQuery.trim(jQuery(this).val().toLowerCase()), f = [];
        if (!e) {
            c.show()
        } else {
            c.hide();
            a.each(function (g) {
                var h = this.score(e);
                if (h > 0) {
                    f.push([h, g])
                }
            });
            jQuery.each(f.sort(function (h, g) {
                return g[0] - h[0]
            }), function () {
                jQuery(c[this[1]]).show()
            })
        }
    }
};
String.prototype.score = function (l, d) {
    d = d || 0;
    if (l.length == 0) {
        return 0.9
    }
    if (l.length > this.length) {
        return 0
    }
    for (var g = l.length; g > 0; g--) {
        var n = l.substring(0, g);
        var h = this.indexOf(n);
        if (h < 0) {
            continue
        }
        if (h + l.length > this.length + d) {
            continue
        }
        var m = this.substring(h + n.length);
        var f = null;
        if (g >= l.length) {
            f = ""
        } else {
            f = l.substring(g)
        }
        var e = m.score(f, d + h);
        if (e > 0) {
            var a = this.length - m.length;
            if (h != 0) {
                var b = 0;
                var k = this.charCodeAt(h - 1);
                if (k == 32 || k == 9) {
                    for (var b = (h - 2); b >= 0; b--) {
                        k = this.charCodeAt(b);
                        a -= ((k == 32 || k == 9) ? 1 : 0.15)
                    }
                } else {
                    a -= h
                }
            }
            a += e * m.length;
            a /= this.length;
            return a
        }
    }
    return 0
};
